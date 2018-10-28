package net.acomputerdog.securitycheckup.test.step.log;

import net.acomputerdog.securitycheckup.test.TestEnvironment;
import net.acomputerdog.securitycheckup.test.step.PassthroughStep;
import net.acomputerdog.securitycheckup.test.step.Step;

public class StoreDataStep<T> extends PassthroughStep<T> {
    private final Object dataKey;

    public StoreDataStep(Step<T> passthrough, Object dataKey) {
        super(passthrough);
        this.dataKey = dataKey;
    }

    public Object getDataKey() {
        return dataKey;
    }

    @Override
    public T run(TestEnvironment environment) {
        T val = super.run(environment);
        environment.setSharedResource(dataKey, val);
        return val;
    }
}
