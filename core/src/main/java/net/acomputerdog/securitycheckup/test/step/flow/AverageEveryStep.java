package net.acomputerdog.securitycheckup.test.step.flow;

import net.acomputerdog.securitycheckup.test.TestEnvironment;
import net.acomputerdog.securitycheckup.test.step.Step;

import java.util.Arrays;
import java.util.List;

public class AverageEveryStep extends Step<Float> {
    private final Step<Float>[] subSteps;

    public AverageEveryStep(Step<Float> ... subSteps) {
        this.subSteps = subSteps;
    }

    @Override
    public Float run(TestEnvironment environment) {
        float count = 0;
        float total = 0;

        for (Step<Float> step : subSteps) {
            count++;
            total += step.run(environment);
        }

        return count == 0 ? 0f : (total / count);
    }

    @Override
    public List<Step> getSubsteps() {
        return Arrays.asList(subSteps);
    }
}
