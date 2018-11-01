package net.acomputerdog.securitycheckup.test.step.data;

import net.acomputerdog.securitycheckup.test.TestEnvironment;
import net.acomputerdog.securitycheckup.test.step.Step;

public class PushStep<T> implements Step<T> {
    private final T value;

    public PushStep(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    @Override
    public T run(TestEnvironment environment) {
        return value;
    }
}
