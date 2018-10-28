package net.acomputerdog.securitycheckup.main.gui;

import net.acomputerdog.securitycheckup.ex.UnsupportedPlatformException;

import javax.swing.*;

public class GUIMain {
    public static void main(String[] args) {
        try {
            // check environment
            checkEnvironment();

            SecurityCheckupApplication.launch(args);
        } catch (UnsupportedPlatformException e) {
            System.out.println("Unsupported platform!");
            e.printStackTrace();

            displayWarning("You are using an unsupported platform.\r\n\r\n" + e.getMessage(), "Unsupported platform");
        } catch (Throwable t) {
            System.err.println("Exception starting application");
            t.printStackTrace();

            displayException("Exception occurred while starting SecurityCheckup.  The program must now close.", t);
        }
    }

    public static void displayException(String message, Throwable throwable) {
        String fullMessage = message + "\r\n\r\nException: " + String.valueOf(throwable);
        JOptionPane.showMessageDialog(null, fullMessage, "Unhandled exception", JOptionPane.ERROR_MESSAGE);
    }

    public static void displayWarning(String message, String title) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.WARNING_MESSAGE);
    }

    private static void checkEnvironment() throws UnsupportedPlatformException {
        // check for windows 10
        if (!"Windows 10".equals(System.getProperty("os.name"))) {
            throw new UnsupportedPlatformException("Only Windows 10 is supported.  Other versions of windows and " +
                    "non-windows operating systems are unsupported.");
        }

        // check for java 8
        String javaVersion = System.getProperty("java.specification.version");
        if (!javaVersion.startsWith("1.8") && !javaVersion.startsWith("9") && !javaVersion.startsWith("10")) {
            //displayWarning("Allowing untested java version: " + javaVersion, "Warning");
            throw new UnsupportedPlatformException("Unsupported java version: " + javaVersion + ".  Java 8, 9, or 10 is required.  " +
                    "Older and newer versions are unsupported.");
        }

        // check for JavaFX
        try {
            Class.forName("javafx.application.Application", false, ClassLoader.getSystemClassLoader());
        } catch (ClassNotFoundException e) {
            throw new UnsupportedPlatformException("JavaFX is required.  Please install the full, non-headless " +
                    "version of Java 8 JRE or JDK.");
        }
    }
}
