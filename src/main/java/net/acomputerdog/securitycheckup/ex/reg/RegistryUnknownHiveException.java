package net.acomputerdog.securitycheckup.ex.reg;

/**
 * An exception that indicates that a name could not be resolved to a valid registry hive
 */
public class RegistryUnknownHiveException extends IllegalArgumentException {
    public RegistryUnknownHiveException() {
        super();
    }

    public RegistryUnknownHiveException(String s) {
        super(s);
    }
}
