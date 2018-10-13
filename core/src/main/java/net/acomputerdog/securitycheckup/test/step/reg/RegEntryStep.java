package net.acomputerdog.securitycheckup.test.step.reg;

import com.sun.jna.platform.win32.WinReg;

public abstract class RegEntryStep<T> extends RegKeyStep<T> {
    /**
     * Registry key value to access
     */
    private final String value;

    public RegEntryStep(WinReg.HKEY hive, String key, String value) {
        super(hive, key);
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
