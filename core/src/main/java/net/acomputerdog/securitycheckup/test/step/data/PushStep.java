package net.acomputerdog.securitycheckup.test.step.data;

import net.acomputerdog.securitycheckup.test.TestEnvironment;
import net.acomputerdog.securitycheckup.test.step.Step;
import net.acomputerdog.securitycheckup.util.gson.GenericWrapped;

public class PushStep<T> implements Step<T> {
    private final GenericWrapped<T> value;

    public PushStep(T value) {
        this(new GenericWrapped<>(value));
    }

    public PushStep(GenericWrapped<T> value) {
        this.value = value;
    }

    public GenericWrapped<T> getValue() {
        return value;
    }

    @Override
    public T run(TestEnvironment environment) {
        return value.getValue();
    }
}
