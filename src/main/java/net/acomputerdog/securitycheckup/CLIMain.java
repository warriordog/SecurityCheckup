package net.acomputerdog.securitycheckup;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;
import net.acomputerdog.securitycheckup.ex.UnknownHiveException;
import net.acomputerdog.securitycheckup.ex.UnsupportedPlatformException;

/**
 * Main class for command line usage
 */
public class CLIMain implements AutoCloseable {

    /**
     * CLI constructor, should only be called from main()
     */
    private CLIMain() {

    }

    /**
     * Sets up the program to begin testing
     */
    private void setup() throws UnsupportedPlatformException {

    }

    /**
     * Runs the security check and prints results
     */
    private void run() {

    }

    /**
     * Shuts down the program and releases any resources
     */
    public void close() {

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
                    System.out.println("/?                                  Show this help");
                    System.out.println("/version                            Show version string and exit");
                    System.out.println("/debug_reg  <hive>  <key>  [value]  DEBUG: Print the value of a registry key");

                    exitOK();
                } else if (args[0].equalsIgnoreCase("/version")) {
                    // version was already printed
                    exitOK();
                } else if (args[0].equalsIgnoreCase("/debug_reg")) {
                    if (args.length >= 2) {

                        // Value can be null for default value
                        String value = null;
                        if (args.length >= 3) {
                            value = args[3];
                        }

                        debugPrintReg(args[1], args[2], value);
                    } else {
                        System.out.println("You must specify the hive and path to the key to read");
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
                System.err.println("This system or software environment is not supported.  Security Checkup cannot run.");
                e.printStackTrace();
                exitIncompatible();
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
        System.out.println();
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
}
