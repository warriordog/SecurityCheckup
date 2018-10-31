package net.acomputerdog.securitycheckup.test.step.reg;

import com.sun.jna.platform.win32.WinReg;
import net.acomputerdog.securitycheckup.ex.reg.RegistryKeyMissingException;
import net.acomputerdog.securitycheckup.ex.reg.RegistryValueMissingException;
import net.acomputerdog.securitycheckup.test.TestEnvironment;

public class RegDefValueStep<T> extends RegValueStep<T> {
    private final T defaultValue;

    public RegDefValueStep(WinReg.HKEY hive, String key, String value, T defaultValue) {
        super(hive, key, value);
        this.defaultValue = defaultValue;
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    @Override
    public T run(TestEnvironment environment) {
        try {
            return super.run(environment);
        } catch (RegistryKeyMissingException | RegistryValueMissingException e) {
            return defaultValue;
        }
    }
}
