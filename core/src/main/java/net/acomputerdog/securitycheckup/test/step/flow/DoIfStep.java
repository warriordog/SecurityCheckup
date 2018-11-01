package net.acomputerdog.securitycheckup.test.step.flow;

import net.acomputerdog.securitycheckup.test.TestEnvironment;
import net.acomputerdog.securitycheckup.test.step.PassthroughStep;
import net.acomputerdog.securitycheckup.test.step.Step;

import java.util.ArrayList;
import java.util.List;

public class DoIfStep extends PassthroughStep<Boolean> {
    private final Step<Boolean> doIf;

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

    @Override
    public List<Step> getSubsteps() {
        List<Step> subSteps = new ArrayList<>();
        subSteps.add(super.getPassthrough());
        subSteps.add(doIf);
        return subSteps;
    }
}
