package net.acomputerdog.securitycheckup.test.step.flow;

import net.acomputerdog.securitycheckup.test.TestEnvironment;
import net.acomputerdog.securitycheckup.test.step.PassthroughStep;
import net.acomputerdog.securitycheckup.test.step.Step;

public class DoIfStep extends PassthroughStep<Boolean> {
    private final Step doIf;

    public DoIfStep(Step<Boolean> passthrough, Step doIf) {
        super(passthrough);
        this.doIf = doIf;
    }

    public Step getDoIf() {
        return doIf;
    }

    @Override
    public Boolean run(TestEnvironment environment) {
        boolean result = super.run(environment);
        if (result) {
            doIf.run(environment);
        }
        return result;
    }
}
