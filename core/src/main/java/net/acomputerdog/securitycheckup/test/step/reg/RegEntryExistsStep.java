package net.acomputerdog.securitycheckup.test.step.reg;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;
import net.acomputerdog.securitycheckup.test.TestEnvironment;

public class RegEntryExistsStep extends RegEntryStep<Boolean> {
    public RegEntryExistsStep(WinReg.HKEY hive, String key, String value) {
        super(hive, key, value);
    }

    @Override
    public Boolean run(TestEnvironment environment) {
        return Advapi32Util.registryValueExists(getHive(), getKey(), getValue());
    }
}
