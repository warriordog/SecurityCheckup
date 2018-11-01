package net.acomputerdog.securitycheckup.test.step.reg;

import net.acomputerdog.securitycheckup.test.TestEnvironment;
import net.acomputerdog.securitycheckup.util.RegUtil;

public class RegValueStep<T> extends RegEntryStep<T> {
    public RegValueStep(String hive, String key, String value) {
        super(hive, key, value);
    }

    @Override
    public T run(TestEnvironment environment) {
        return (T) RegUtil.readReg(getHive(), getKey(), getValue());
    }
}
