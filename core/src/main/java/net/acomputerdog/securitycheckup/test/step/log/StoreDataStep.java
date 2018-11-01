package net.acomputerdog.securitycheckup.test.step.log;

import net.acomputerdog.securitycheckup.test.TestEnvironment;
import net.acomputerdog.securitycheckup.test.step.PassthroughStep;
import net.acomputerdog.securitycheckup.test.step.Step;
import net.acomputerdog.securitycheckup.util.gson.GenericWrapped;

public class StoreDataStep<T> extends PassthroughStep<T> {
    private final GenericWrapped<Object> dataKey;

    public StoreDataStep(Step<T> passthrough, GenericWrapped<Object> dataKey) {
        super(passthrough);
        this.dataKey = dataKey;
    }

    public GenericWrapped<Object> getDataKey() {
        return dataKey;
    }

    @Override
    public T run(TestEnvironment environment) {
        T val = super.run(environment);
        environment.setSharedResource(dataKey.getValue(), val);
        return val;
    }
}
