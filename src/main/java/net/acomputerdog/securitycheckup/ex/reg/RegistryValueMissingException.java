package net.acomputerdog.securitycheckup.ex.reg;

import com.sun.jna.platform.win32.WinReg;

/**
 * Indicates that a registry key value could not be found
 */
public class RegistryValueMissingException extends RegistryValueException {
    public RegistryValueMissingException(WinReg.HKEY hive, String key, String value) {
        super(hive, key, value);
    }

    public RegistryValueMissingException(String message, WinReg.HKEY hive, String key, String value) {
        super(message, hive, key, value);
    }
}
