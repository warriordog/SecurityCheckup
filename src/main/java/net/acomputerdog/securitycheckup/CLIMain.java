package net.acomputerdog.securitycheckup;

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
                    System.out.println("/?          Show this help");
                    System.out.println("/version    Show version string and exit");

                    exitOK();
                } else if (args[0].equalsIgnoreCase("/version")) {
                    // version was already printed
                    exitOK();
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
