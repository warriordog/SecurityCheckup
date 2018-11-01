package net.acomputerdog.securitycheckup.test.step;

import net.acomputerdog.securitycheckup.test.TestEnvironment;

public abstract class PassthroughStep<T> implements Step<T> {
    private final Step<T> passthrough;

    protected PassthroughStep(Step<T> passthrough) {
        this.passthrough = passthrough;
    }

    @Override
    public T run(TestEnvironment environment) {
        return passthrough.run(environment);
    }

    public Step<T> getPassthrough() {
        return passthrough;
    }

}
