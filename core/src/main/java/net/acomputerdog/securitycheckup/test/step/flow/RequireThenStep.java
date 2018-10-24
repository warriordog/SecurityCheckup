package net.acomputerdog.securitycheckup.test.step.flow;

import net.acomputerdog.securitycheckup.test.TestEnvironment;
import net.acomputerdog.securitycheckup.test.step.Step;

import java.util.Arrays;
import java.util.List;

public class RequireThenStep extends Step<Float> {
    private final Step<Boolean> requiredStep;
    private final Step<Float> nextStep;

    public RequireThenStep(Step<Boolean> requiredStep, Step<Float> nextStep) {
        this.requiredStep = requiredStep;
        this.nextStep = nextStep;
    }

    public Step<Boolean> getRequiredStep() {
        return requiredStep;
    }

    public Step<Float> getNextStep() {
        return nextStep;
    }


    @Override
    public Float run(TestEnvironment environment) {
        if (requiredStep.run(environment)) {
            return nextStep.run(environment);
        } else {
            return 0.0f;
        }
    }

    @Override
    public List<Step> getSubsteps() {
        return Arrays.asList(new Step[]{requiredStep, nextStep});
    }
}
