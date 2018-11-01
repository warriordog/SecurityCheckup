package net.acomputerdog.securitycheckup.test.step.flow;

import net.acomputerdog.securitycheckup.test.TestEnvironment;
import net.acomputerdog.securitycheckup.test.step.PassthroughStep;
import net.acomputerdog.securitycheckup.test.step.Step;

import java.util.ArrayList;
import java.util.List;

public class DoAndDoIgnoredStep<T> extends PassthroughStep<T> {
    private final Step optionalStep;

    public DoAndDoIgnoredStep(Step<T> passthrough, Step optionalStep) {
        super(passthrough);
        this.optionalStep = optionalStep;
    }

    public Step getOptionalStep() {
        return optionalStep;
    }

    @Override
    public T run(TestEnvironment environment) {
        T result = super.run(environment);

        optionalStep.run(environment);

        return result;
    }

    @Override
    public List<Step> getSubsteps() {
        List<Step> substeps = new ArrayList<>();
        substeps.add(getPassthrough());
        substeps.add(optionalStep);
        return substeps;
    }
}
