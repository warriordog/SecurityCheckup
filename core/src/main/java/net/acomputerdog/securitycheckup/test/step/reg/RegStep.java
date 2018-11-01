package net.acomputerdog.securitycheckup.test.step.reg;


import com.sun.jna.platform.win32.WinReg;
import net.acomputerdog.securitycheckup.test.step.Step;
import net.acomputerdog.securitycheckup.util.RegUtil;

public abstract class RegStep<T> implements Step<T> {
    /**
     * Registry hive to access
     */
    private final String hive;

    public RegStep(String hive) {
        this.hive = hive;
    }

    public String getHiveString() {
        return hive;
    }

    public WinReg.HKEY getHive() {
        return RegUtil.getHiveByName(hive);
    }
}
