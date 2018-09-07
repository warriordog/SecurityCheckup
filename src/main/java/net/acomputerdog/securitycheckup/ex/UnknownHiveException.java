package net.acomputerdog.securitycheckup.ex;

/**
 * An exception that indicates that a name could not be resolved to a valid registry hive
 */
public class UnknownHiveException extends IllegalArgumentException {
    public UnknownHiveException() {
        super();
    }

    public UnknownHiveException(String s) {
        super(s);
    }

    public UnknownHiveException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnknownHiveException(Throwable cause) {
        super(cause);
    }
}
