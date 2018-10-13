package net.acomputerdog.securitycheckup.test.step.reg;


import com.sun.jna.platform.win32.WinReg;
import net.acomputerdog.securitycheckup.test.step.Step;

public abstract class RegStep<T> extends Step<T> {
    /**
     * Registry hive to access
     */
    private final WinReg.HKEY hive;

    public RegStep(WinReg.HKEY hive) {
        this.hive = hive;
    }

    public WinReg.HKEY getHive() {
        return hive;
    }
}
