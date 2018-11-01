package net.acomputerdog.securitycheckup.test.step.flow;

import net.acomputerdog.securitycheckup.test.TestEnvironment;
import net.acomputerdog.securitycheckup.test.step.PassthroughStep;
import net.acomputerdog.securitycheckup.test.step.Step;

public class RequireThenStep extends PassthroughStep<Float> {
    private final Step<Boolean> requiredStep;

    public RequireThenStep(Step<Boolean> requiredStep, Step<Float> nextStep) {
        super(nextStep);
        this.requiredStep = requiredStep;
    }

    public Step<Boolean> getRequiredStep() {
        return requiredStep;
    }


    @Override
    public Float run(TestEnvironment environment) {
        if (requiredStep.run(environment)) {
            return getPassthrough().run(environment);
        } else {
            return 0.0f;
        }
    }

}
