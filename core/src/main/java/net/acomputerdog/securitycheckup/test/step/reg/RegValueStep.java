package net.acomputerdog.securitycheckup.test.step.reg;

import com.sun.jna.platform.win32.WinReg;
import net.acomputerdog.securitycheckup.test.TestEnvironment;
import net.acomputerdog.securitycheckup.util.RegUtil;

public class RegValueStep<T> extends RegEntryStep<T> {
    public RegValueStep(WinReg.HKEY hive, String key, String value) {
        super(hive, key, value);
    }

    @Override
    public T run(TestEnvironment environment) {
        return (T) RegUtil.readReg(getHive(), getKey(), getValue());
    }
}
