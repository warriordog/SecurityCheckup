package net.acomputerdog.securitycheckup.ex.reg;

import com.sun.jna.platform.win32.WinReg;

/**
 * Indicates that a registry key could not be found
 */
public class RegistryKeyMissingException extends RegistryKeyException {
    public RegistryKeyMissingException(WinReg.HKEY hive, String key) {
        super(hive, key);
    }

    public RegistryKeyMissingException(String message, WinReg.HKEY hive, String key) {
        super(message, hive, key);
    }
}
