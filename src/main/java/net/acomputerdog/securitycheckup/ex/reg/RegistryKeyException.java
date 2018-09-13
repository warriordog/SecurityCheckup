package net.acomputerdog.securitycheckup.ex.reg;

import com.sun.jna.platform.win32.WinReg;

/**
 * Indicates a problem related to a specific registry key
 */
public class RegistryKeyException extends RegistryException {
    private final WinReg.HKEY hive;
    private final String key;

    public RegistryKeyException(WinReg.HKEY hive, String key) {
        super();
        this.hive = hive;
        this.key = key;
    }

    public RegistryKeyException(String message, WinReg.HKEY hive, String key) {
        super(message);
        this.hive = hive;
        this.key = key;
    }

    public WinReg.HKEY getHive() {
        return hive;
    }

    public String getKey() {
        return key;
    }
}
