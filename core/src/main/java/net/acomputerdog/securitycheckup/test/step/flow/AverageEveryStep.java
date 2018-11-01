package net.acomputerdog.securitycheckup.test.step.flow;

import net.acomputerdog.securitycheckup.test.TestEnvironment;
import net.acomputerdog.securitycheckup.test.step.Step;
import net.acomputerdog.securitycheckup.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

public class AverageEveryStep implements Step<Float> {
    private final List<Step<Float>> subSteps;

    @SafeVarargs
    public AverageEveryStep(Step<Float> ... substeps) {
        this(CollectionUtils.createList(substeps));
    }

    public AverageEveryStep(List<Step<Float>> subSteps) {
        this.subSteps = Collections.unmodifiableList(subSteps);
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

}
