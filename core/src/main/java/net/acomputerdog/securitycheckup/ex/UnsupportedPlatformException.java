package net.acomputerdog.securitycheckup.ex;

/**
 * An exception indicating that some version or feature of the system or its
 * software is incompatible with the program.
 */
public class UnsupportedPlatformException extends Exception {
    public UnsupportedPlatformException() {
        super();
    }

    public UnsupportedPlatformException(String message) {
        super(message);
    }

    public UnsupportedPlatformException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedPlatformException(Throwable cause) {
        super(cause);
    }
}
