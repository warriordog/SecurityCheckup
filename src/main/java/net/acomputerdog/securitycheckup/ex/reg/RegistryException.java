package net.acomputerdog.securitycheckup.ex.reg;

/**
 * Indicates a general problem with the windows registry
 */
public class RegistryException extends RuntimeException {
    public RegistryException() {
        super();
    }

    public RegistryException(String message) {
        super(message);
    }

    public RegistryException(String message, Throwable cause) {
        super(message, cause);
    }

    public RegistryException(Throwable cause) {
        super(cause);
    }
}
