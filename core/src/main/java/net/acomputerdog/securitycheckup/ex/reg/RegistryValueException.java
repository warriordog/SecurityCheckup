package net.acomputerdog.securitycheckup.ex.reg;

import com.sun.jna.platform.win32.WinReg;

/**
 * Indicates a problem with a specific registry key value
 */
public class RegistryValueException extends RegistryKeyException {
    private final String value;

    public RegistryValueException(WinReg.HKEY hive, String key, String value) {
        super(hive, key);
        this.value = value;
    }

    public RegistryValueException(String message, WinReg.HKEY hive, String key, String value) {
        super(message, hive, key);
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
