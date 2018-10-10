package net.acomputerdog.securitycheckup.test.types.reg;

import com.sun.jna.platform.win32.WinReg;

/**
 * Superclass for tests involving a registry key
 */
public abstract class RegKeyTest extends RegTest {
    /**
     * Registry key to read
     */
    private final String key;

    public RegKeyTest(String id, String name, String description, WinReg.HKEY hive, String key) {
        super(id, name, description, hive);
        this.key = key;
    }

    public RegKeyTest(String id, String name, String description, String hive, String key) {
        super(id, name, description, hive);
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
