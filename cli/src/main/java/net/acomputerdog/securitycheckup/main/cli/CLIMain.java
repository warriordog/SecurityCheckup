package net.acomputerdog.securitycheckup.main.cli;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;
import net.acomputerdog.jwmi.JWMI;
import net.acomputerdog.jwmi.ex.NativeException;
import net.acomputerdog.jwmi.ex.NativeHresultException;
import net.acomputerdog.jwmi.ex.WMIException;
import net.acomputerdog.jwmi.nat.ReleasableVariant;
import net.acomputerdog.jwmi.wbem.EnumWbemClassObject;
import net.acomputerdog.jwmi.wbem.WbemClassObject;
import net.acomputerdog.jwmi.wbem.WbemLocator;
import net.acomputerdog.jwmi.wbem.WbemServices;
import net.acomputerdog.securitycheckup.ex.UnsupportedPlatformException;
import net.acomputerdog.securitycheckup.test.Profile;
import net.acomputerdog.securitycheckup.test.Test;
import net.acomputerdog.securitycheckup.test.TestEnvironment;
import net.acomputerdog.securitycheckup.test.TestResult;
import net.acomputerdog.securitycheckup.test.registry.Bundle;
import net.acomputerdog.securitycheckup.test.registry.TestRegistry;
import net.acomputerdog.securitycheckup.util.RegUtil;
import net.acomputerdog.securitycheckup.util.gson.JsonUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Main class for command line usage
 */
@Deprecated // Needs to be rewritten or thrown out
public class CLIMain implements AutoCloseable {
    /**
     * Path to the WMI namespace CIMV2
     */
    public static final String WMI_NAMESPACE_CIMV2 = "root\\CIMV2";

    /**
     * Shared test environment
     */
    private TestEnvironment testEnvironment;

    /**
     * Test and profile registry
     */
    private TestRegistry testRegistry;

    /**
     * CLI constructor, should only be called from main()
     */
    private CLIMain() {

    }

    /**
     * Sets up the program to begin testing
     */
    private void setup(TestRegistry testRegistry) throws UnsupportedPlatformException, NativeException {
        if (!"Windows 10".equals(System.getProperty("os.name"))) {
            throw new UnsupportedPlatformException("OS is not windows 10");
        }

        WbemLocator locator = JWMI.getInstance().createWbemLocator();
        this.testEnvironment = new TestEnvironment(locator);

        this.testRegistry = testRegistry;
    }

    /**
     * Runs the security check and prints results
     */
    private void run() {
        System.out.println("Running tests...");

        float scoreTotal = 0.0f;
        float scoreCount = 0f;

        Map<Profile, List<TestResult>> testSuiteResults = new HashMap<>();

        // Run each suite, and each test in each suite
        System.out.printf("Running %d test suites.\n", testRegistry.getProfileCount());
        for (Profile tests : testRegistry.getProfiles()) {
            System.out.printf("Running %d tests in '%s'.\n", tests.getNumTests(), tests.getName());

            List<TestResult> testResults = new ArrayList<>();
            for (Test test : tests.getTestsFrom(testRegistry)) {
                testEnvironment.setCurrentTest(test);
                TestResult result = test.getRootStep().run(testEnvironment);
                testResults.add(result);

                scoreTotal += result.getScore();
                scoreCount++;
            }
            testSuiteResults.put(tests, testResults);
        }

        float overallScore = scoreCount == 0 ? 0.0f : (scoreTotal / scoreCount);

        System.out.println();
        System.out.println("Test complete!");
        System.out.printf("Overall system score: %2.0f%%\n", overallScore * 100);
        System.out.println();

        // print each suite
        System.out.println("Individual test results:");
        for (Map.Entry<Profile, List<TestResult>> suiteResult : testSuiteResults.entrySet()) {
            Profile suite = suiteResult.getKey();
            List<TestResult> suiteResults = suiteResult.getValue();

            System.out.printf("Results for suite '%s':\n", suite.getId());
            System.out.println("----------------------");
            System.out.printf("|%s:\n", suite.getName());
            System.out.printf("|%s\n", suite.getDescription());
            System.out.println("----------------------");

            // print results
            for (TestResult result : suiteResults) {
                String resultMessage = result.getInfo(TestResult.KEY_MESSAGE);
                String resultEx = result.getInfo(TestResult.KEY_EXCEPTION);

                System.out.printf("  |%-7s %s\n", result.getResultString(), result.getTestInfo().getId());
                System.out.printf("  |Name:   %s\n", result.getTestInfo().getName());
                System.out.printf("  |Desc:   %s\n", result.getTestInfo().getDescription());
                System.out.printf("  |Score:  %1.0f%%\n", result.getScore() * 100);
                System.out.printf("  |Reason: %s\n", result.getResultCause().name());
                if (resultMessage != null) {
                    System.out.printf("  |Message:   %s\n", resultMessage.replaceAll("\n", "\n  |"));
                }
                if (resultEx != null) {
                    System.out.printf("  |Exception: %s\n", resultEx);
                }
                System.out.println("  ----------------------");
            }
        }
    }

    /**
     * Shuts down the program and releases any resources
     */
    public void close() {
        // clear tests
        if (testRegistry != null) {
            testRegistry.clear();
            testRegistry = null;
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

            TestRegistry testRegistry = new TestRegistry();

            // Parse args
            if (args.length >= 1) {
                if (args[0].equalsIgnoreCase("/?")) {
                    System.out.println("\nUsage: SecurityCheckup {options | bundle_path}");
                    System.out.println("Supported arguments:");
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
                    // no args, load bundle and run
                    try {
                        String bundlePath = args[0];
                        try (Reader reader = new BufferedReader(new FileReader(bundlePath))) {
                            Bundle bundle = JsonUtils.readBundle(reader);
                            bundle.addToRegistry(testRegistry);
                        }
                    } catch (Exception e) {
                        System.err.println("Exception while loading");
                        e.printStackTrace();
                    }
                }
            } else {
                System.out.println("Missing bundle or profile.  Use /? for help.");
                exitBadArg();
            }

            try (CLIMain main = new CLIMain()) {

                main.setup(testRegistry); // Open registry, connect to WMI, etc.

                main.run(); // Run the test

                exitOK();
            } catch (UnsupportedPlatformException e) {
                System.err.println("This system or software environment is not supported: " + e.getMessage() + ".");
                System.err.println("Security Checkup cannot run.");
                exitIncompatible();
            } catch (WMIException e) {
                System.err.printf("WMI exception in object at %s, hresult = 0x%s\n", e.getPointer().toString(), Integer.toHexString(e.getHresult().intValue()));
                e.printStackTrace();
                exitWMIError();
            } catch (NativeHresultException e) {
                System.err.printf("Error in native function, hresult = 0x%s\n", Integer.toHexString(e.getHresult().intValue()));
                e.printStackTrace();
                exitNativeError();
            } catch (NativeException e) {
                System.err.println("Unknown error in native code.");
                e.printStackTrace();
                exitNativeError();
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
        WinReg.HKEY hkey = RegUtil.getHiveByName(hive);

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

    /**
     * Exits with an exit code indicating that an error occurred in native code
     */
    private static void exitNativeError() {
        System.exit(-5);
    }
}
