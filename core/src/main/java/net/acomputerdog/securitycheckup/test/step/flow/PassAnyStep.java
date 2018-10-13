package net.acomputerdog.securitycheckup.test.step.flow;

import net.acomputerdog.securitycheckup.test.TestEnvironment;
import net.acomputerdog.securitycheckup.test.step.Step;

import java.util.Arrays;
import java.util.List;

public class PassAnyStep extends Step<Boolean> {
    private final Step<Boolean>[] subSteps;

    public PassAnyStep(Step<Boolean> ... subSteps) {
        this.subSteps = subSteps;
    }

    @Override
    public Boolean run(TestEnvironment environment) {
        for (Step<Boolean> step : subSteps) {
            if (step.run(environment)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Step> getSubsteps() {
        return Arrays.asList(subSteps);
    }
}
