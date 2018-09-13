package net.acomputerdog.securitycheckup;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;
import net.acomputerdog.jwmi.JWMI;
import net.acomputerdog.jwmi.WMIException;
import net.acomputerdog.jwmi.nat.ReleasableVariant;
import net.acomputerdog.jwmi.wbem.EnumWbemClassObject;
import net.acomputerdog.jwmi.wbem.WbemClassObject;
import net.acomputerdog.jwmi.wbem.WbemLocator;
import net.acomputerdog.jwmi.wbem.WbemServices;
import net.acomputerdog.securitycheckup.ex.UnknownHiveException;
import net.acomputerdog.securitycheckup.ex.UnsupportedPlatformException;
import net.acomputerdog.securitycheckup.test.Test;
import net.acomputerdog.securitycheckup.test.TestEnvironment;
import net.acomputerdog.securitycheckup.test.TestResult;
import net.acomputerdog.securitycheckup.test.types.WMITestMulti;
import net.acomputerdog.securitycheckup.test.types.WMITestPropBoolean;

import java.util.ArrayList;
import java.util.List;


/**
 * Main class for command line usage
 */
public class CLIMain implements AutoCloseable {
    /**
     * Path to the WMI namespace CIMV2
     */
    public static final String WMI_NAMESPACE_CIMV2 = "root\\CIMV2";

    /**
     * Shared test environment
     */
    private TestEnvironment testEnvironment;

    private List<Test> tests;

    /**
     * CLI constructor, should only be called from main()
     */
    private CLIMain() {

    }

    /**
     * Sets up the program to begin testing
     */
    private void setup() throws UnsupportedPlatformException, WMIException {
        if (!"Windows 10".equals(System.getProperty("os.name"))) {
            throw new UnsupportedPlatformException("OS is not windows 10");
        }

        WbemLocator locator = JWMI.getInstance().createWbemLocator();
        this.testEnvironment = new TestEnvironment(locator);

        tests = new ArrayList<>();

        //TODO load from some type of config file

        // Check if defender is turned on
        tests.add(new WMITestPropBoolean(
                "windows_defender_enabled",
                "Windows Defender Enabled",
                "Verifies that Windows Defender is enabled.",
                "ROOT\\Microsoft\\SecurityClient",
                "SELECT * FROM ProtectionTechnologyStatus",
                "Enabled",
                true
                ));

        // Check for 3rd party AV
        tests.add(new WMITestMulti(
                "av_installed",
                "Antivirus Software Installed",
                "Verifies that an antivirus product is installed.",
                "ROOT\\SecurityCenter2",
                "SELECT * FROM AntiVirusProduct",
                false) {
            @Override
            public int testObj(WbemClassObject obj) {
                if (!"Windows Defender".equals(obj.getString("displayName"))) {
                    return PASS;
                } else {
                    return CONTINUE;
                }
            }
        });
    }

    /**
     * Runs the security check and prints results
     */
    private void run() {
        System.out.println("Running tests...");

        float scoreTotal = 0.0f;
        float scoreCount = 0f;

        List<TestResult> testResults = new ArrayList<>();
        for (Test test : tests) {
            TestResult result = test.runTest(testEnvironment);
            testResults.add(result);

            scoreTotal += result.getScore();
            scoreCount++;
        }

        float overallScore = scoreCount == 0 ? 0.0f : (scoreTotal / scoreCount);

        System.out.println("Test complete!");
        System.out.printf("Overall system score: %2.0f%%\n", overallScore * 100);

        System.out.println("Individual test results:");
        for (TestResult result : testResults) {
            System.out.println("----------------------");
            System.out.printf("|%7s %s\n", result.getResultString(), result.getTest().getID());
            System.out.printf("|Name:  %s\n", result.getTest().getName());
            System.out.printf("|Desc:  %s\n", result.getTest().getDescription());
            System.out.printf("|Score: %1.0f%%\n", result.getScore() * 100);
            System.out.printf("|State: %s\n", result.getState().name());
            if (result.getMessage() != null) {
                System.out.printf("|Message:   %s\n", result.getMessage());
            }
            if (result.getException() != null) {
                System.out.printf("|Exception: %s\n", result.getException().toString());
                result.getException().printStackTrace(System.out);
            }
        }
    }

    /**
     * Shuts down the program and releases any resources
     */
    public void close() {
        // clear tests
        if (tests != null) {
            tests.clear();
            tests = null;
        }

        // clear test environment
        if (testEnvironment != null) {
            testEnvironment.clearSharedResources();

            try {
                testEnvironment.getWbemLocator().release();
            } catch (Throwable t) {
                System.out.flush();
                System.err.println("Exception releasing WbemLocator");
                t.printStackTrace();
            }

            testEnvironment = null;
        }

        // GC to close out any native objects
        System.gc();
    }

    /**
     * Program version string
     */
    public static final String VERSION_STRING = "Security Checkup CLI 0.1.0";

    /**
     * CLI entry point
     *
     * @param args Program arguments
     */
    public static void main(String[] args) {
        try {
            // Print version string above rest of output
            printVersionString();
            System.out.println();

            // Parse args
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("/?")) {
                    System.out.println("\nSupported arguments:");
                    System.out.println("/?              Show this help");
                    System.out.println("/version        Show version string and exit");
                    System.out.println("/debug_reg      DEBUG: Print the value of a registry key");
                    System.out.println("    <hive>          The hive to access");
                    System.out.println("    <key>           The key in hive");
                    System.out.println("    [value]         The value to print");
                    System.out.println("/debug_wmi      DEBUG: Query WMI");
                    System.out.println("    <namespace>     The namespace to connect to");
                    System.out.println("    <query>         The query to execute");
                    System.out.println("    <property>      The property to print from query results");

                    exitOK();
                } else if (args[0].equalsIgnoreCase("/version")) {
                    // version was already printed
                    exitOK();
                } else if (args[0].equalsIgnoreCase("/debug_reg")) {
                    if (args.length >= 3) {

                        // Value can be null for default value
                        String value = null;
                        if (args.length >= 4) {
                            value = args[3];
                        }

                        debugPrintReg(args[1], args[2], value);
                    } else {
                        System.out.println("You must specify the hive and path to the key to read");
                        exitBadArg();
                    }
                } else if (args[0].equalsIgnoreCase("/debug_wmi")) {
                    if (args.length == 4) {

                        debugQueryWMI(args[1], args[2], args[3]);
                    } else {
                        System.out.println("You must specify the namespace, query, and property");
                        exitBadArg();
                    }
                } else {
                    System.out.println("Unknown argument \"" + args[0] + "\".  Use /? for help.");
                    exitBadArg();
                }
            }

            try (CLIMain main = new CLIMain()) {

                main.setup(); // Open registry, connect to WMI, etc.

                main.run(); // Run the test

                exitOK();
            } catch (UnsupportedPlatformException e) {
                System.err.println("This system or software environment is not supported: " + e.getMessage() + ".");
                System.err.println("Security Checkup cannot run.");
                //e.printStackTrace();
                exitIncompatible();
            } catch (WMIException e) {
                System.err.println("WMI exception while loading, hresult = 0x" + Long.toHexString(e.getHresult() == null ? 0 : e.getHresult().longValue()));
                e.printStackTrace();
                exitWMIError();
            }
        } catch (Exception e) {
            System.err.println("Unhandled exception occurred");
            e.printStackTrace();
            exitException();
        }
    }

    /**
     * Prints out the version of the program
     */
    private static void printVersionString() {
        System.out.println(VERSION_STRING);
    }

    /**
     * Reads and prints the value of a registry key.  A specific value can be specified,
     * or the value can be null for the default value.
     *
     * @param hive  The registry hive to open
     * @param path  The path to the key
     * @param value The value in the key (null for default)
     */
    private static void debugPrintReg(String hive, String path, String value) {
        WinReg.HKEY hkey = hiveToHKEY(hive);

        // make sure key exists
        if (Advapi32Util.registryKeyExists(hkey, path)) {
            if (Advapi32Util.registryValueExists(hkey, path, value)) {
                // print value
                System.out.println(Advapi32Util.registryGetValue(hkey, path, value));
            } else {
                System.out.println("Error: registry value does not exist.");
            }
        } else {
            System.out.println("Error: registry key does not exist.");
        }
    }

    /**
     * Finds an HKEY by name.
     * The name is case-insensitive, and the common "shorthand" key names are supported:
     * * HKCR = HKEY_CLASSES_ROOT
     * * HKCU = HKEY_CURRENT_USER
     * * HKLM = HKEY_LOCAL_MACHINE
     * * HKCC = HKEY_CURRENT_CONFIG
     *
     * @param hive the name of the HKEY
     * @return return the HKEY that matches the provided name
     * @throws UnknownHiveException if the name could not be resolved
     */
    private static WinReg.HKEY hiveToHKEY(String hive) {
        switch (hive.toUpperCase()) {
            case "HKEY_CLASSES_ROOT":
            case "HKCR":
                return WinReg.HKEY_CLASSES_ROOT;
            case "HKEY_CURRENT_USER":
            case "HKCU":
                return WinReg.HKEY_CURRENT_USER;
            case "HKEY_LOCAL_MACHINE":
            case "HKLM":
                return WinReg.HKEY_LOCAL_MACHINE;
            case "HKEY_USERS":
                return WinReg.HKEY_USERS;
            case "HKEY_PERFORMANCE_DATA":
                return WinReg.HKEY_PERFORMANCE_DATA;
            case "HKEY_PERFORMANCE_TEXT":
                return WinReg.HKEY_PERFORMANCE_TEXT;
            case "HKEY_PERFORMANCE_NLSTEXT":
                return WinReg.HKEY_PERFORMANCE_NLSTEXT;
            case "HKEY_CURRENT_CONFIG":
            case "HKCC":
                return WinReg.HKEY_CURRENT_CONFIG;
            case "HKEY_DYN_DATA":
                return WinReg.HKEY_DYN_DATA;
            default:
                throw new UnknownHiveException("Unknown registry hive: " + hive);
        }
    }

    private static void debugQueryWMI(String namespace, String query, String property) {
        try {
            try (WbemLocator locator = JWMI.getInstance().createWbemLocator()) {
                try (WbemServices services = locator.connectServer(namespace)) {
                    try (EnumWbemClassObject enumClsObj = services.execQuery(query)) {
                        while (enumClsObj.hasNext()) {
                            try (WbemClassObject clsObj = enumClsObj.next()) {
                                try (ReleasableVariant variant = clsObj.get(property)) {
                                    System.out.println(variant.stringValue());
                                }
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("WMI error occurred");
            e.printStackTrace();
        }
    }

    /**
     * Exits with an OK exit code (0).
     */
    private static void exitOK() {
        System.exit(0);
    }

    /**
     * Exits with an exit code indicating a bad argument (-1).
     */
    private static void exitBadArg() {
        System.exit(-1);
    }

    /**
     * Exits with an exit code indicating an exception occurred (-2).
     */
    private static void exitException() {
        System.exit(-2);
    }

    /**
     * Exits with an exit code indicating that the system is incompatible with the program (-3).
     */
    private static void exitIncompatible() {
        System.exit(-3);
    }

    /**
     * Exits with an exit code indicating that an unknown WMI error occurred
     */
    private static void exitWMIError() {
        System.exit(-4);
    }
}
