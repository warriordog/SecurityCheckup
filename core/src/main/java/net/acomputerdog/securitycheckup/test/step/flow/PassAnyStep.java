package net.acomputerdog.securitycheckup.test.step.flow;

import net.acomputerdog.securitycheckup.test.TestEnvironment;
import net.acomputerdog.securitycheckup.test.step.Step;
import net.acomputerdog.securitycheckup.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

public class PassAnyStep implements Step<Boolean> {
    private final List<Step<Boolean>> subSteps;

    @SafeVarargs
    public PassAnyStep(Step<Boolean> ... substeps) {
        this(CollectionUtils.createList(substeps));
    }

    public PassAnyStep(List<Step<Boolean>> subSteps) {
        this.subSteps = Collections.unmodifiableList(subSteps);
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

}
