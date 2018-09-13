package net.acomputerdog.securitycheckup.util;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;
import net.acomputerdog.securitycheckup.ex.reg.RegistryKeyMissingException;
import net.acomputerdog.securitycheckup.ex.reg.RegistryUnknownHiveException;
import net.acomputerdog.securitycheckup.ex.reg.RegistryValueMissingException;

/**
 * Registry-related utilities
 */
public class RegUtil {
    /**
     * Name of the "default" key value
     */
    public static final String DEFAULT_VALUE = null;

    /**
     * Finds an HKEY by name.
     * The name is case-insensitive, and the common "shorthand" key names are supported:
     * * HKCR = HKEY_CLASSES_ROOT
     * * HKCU = HKEY_CURRENT_USER
     * * HKLM = HKEY_LOCAL_MACHINE
     * * HKCC = HKEY_CURRENT_CONFIG
     *
     * @param hive the name of the HKEY
     * @return return the HKEY that matches the provided name
     * @throws RegistryUnknownHiveException if the name could not be resolved
     */
    public static WinReg.HKEY getHiveByName(String hive) {
        switch (hive.toUpperCase()) {
            case "HKEY_CLASSES_ROOT":
            case "HKCR":
                return WinReg.HKEY_CLASSES_ROOT;
            case "HKEY_CURRENT_USER":
            case "HKCU":
                return WinReg.HKEY_CURRENT_USER;
            case "HKEY_LOCAL_MACHINE":
            case "HKLM":
                return WinReg.HKEY_LOCAL_MACHINE;
            case "HKEY_USERS":
                return WinReg.HKEY_USERS;
            case "HKEY_PERFORMANCE_DATA":
                return WinReg.HKEY_PERFORMANCE_DATA;
            case "HKEY_PERFORMANCE_TEXT":
                return WinReg.HKEY_PERFORMANCE_TEXT;
            case "HKEY_PERFORMANCE_NLSTEXT":
                return WinReg.HKEY_PERFORMANCE_NLSTEXT;
            case "HKEY_CURRENT_CONFIG":
            case "HKCC":
                return WinReg.HKEY_CURRENT_CONFIG;
            case "HKEY_DYN_DATA":
                return WinReg.HKEY_DYN_DATA;
            default:
                throw new RegistryUnknownHiveException("Unknown registry hive: " + hive);
        }
    }

    /**
     * Reads a value from the registry
     *
     * @param hive      hive to access
     * @param key       key to access
     * @param value     value to access
     *
     * @return  return the contents of the registry key
     *
     * @throws RegistryKeyMissingException      if the requested key is missing
     * @throws RegistryValueMissingException    if the requested value was not found in the specified key
     */
    public static Object readReg(WinReg.HKEY hive, String key, String value) {
        if (Advapi32Util.registryKeyExists(hive, key)) {
            if (Advapi32Util.registryValueExists(hive, key, value)) {
                return Advapi32Util.registryGetValue(hive, key, value);
            } else {
                throw new RegistryValueMissingException("Required registry value is missing", hive, key, value);
            }
        } else {
            throw new RegistryKeyMissingException("Required registry key is missing.", hive, key);
        }
    }
}
