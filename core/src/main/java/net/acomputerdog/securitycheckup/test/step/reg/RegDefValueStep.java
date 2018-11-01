package net.acomputerdog.securitycheckup.test.step.reg;

import net.acomputerdog.securitycheckup.ex.reg.RegistryKeyMissingException;
import net.acomputerdog.securitycheckup.ex.reg.RegistryValueMissingException;
import net.acomputerdog.securitycheckup.test.TestEnvironment;
import net.acomputerdog.securitycheckup.util.gson.GenericWrapped;

public class RegDefValueStep<T> extends RegValueStep<T> {
    private final GenericWrapped<T> defaultValue;

    public RegDefValueStep(String hive, String key, String value, T defaultValue) {
        this(hive, key, value, new GenericWrapped<>(defaultValue));
    }

    public RegDefValueStep(String hive, String key, String value, GenericWrapped<T> defaultValue) {
        super(hive, key, value);
        this.defaultValue = defaultValue;
    }

    public GenericWrapped<T> getDefaultValue() {
        return defaultValue;
    }

    @Override
    public T run(TestEnvironment environment) {
        try {
            return super.run(environment);
        } catch (RegistryKeyMissingException | RegistryValueMissingException e) {
            return defaultValue.getValue();
        }
    }
}
