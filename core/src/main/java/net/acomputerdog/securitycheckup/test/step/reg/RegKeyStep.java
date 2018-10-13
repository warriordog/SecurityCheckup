package net.acomputerdog.securitycheckup.test.step.reg;

import com.sun.jna.platform.win32.WinReg;

public abstract class RegKeyStep<T> extends RegStep<T> {
    /**
     * Registry key to read
     */
    private final String key;

    public RegKeyStep(WinReg.HKEY hive, String key) {
        super(hive);
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
