package net.acomputerdog.securitycheckup.test.step;

import net.acomputerdog.securitycheckup.test.TestEnvironment;

import java.util.Collections;
import java.util.List;

public abstract class PassthroughStep<T> extends Step<T> {
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

    @Override
    public List<Step> getSubsteps() {
        return Collections.singletonList(passthrough);
    }
}
