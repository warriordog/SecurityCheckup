package net.acomputerdog.securitycheckup.test.step.flow;

import net.acomputerdog.securitycheckup.test.TestEnvironment;
import net.acomputerdog.securitycheckup.test.step.Step;
import net.acomputerdog.securitycheckup.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

public class PassEveryStep extends Step<Boolean> {
    private final List<Step<Boolean>> subSteps;

    public PassEveryStep(Step<Boolean> ... substeps) {
        this(CollectionUtils.createList(substeps));
    }

    public PassEveryStep(List<Step<Boolean>> subSteps) {
        this.subSteps = Collections.unmodifiableList(subSteps);
    }

    @Override
    public Boolean run(TestEnvironment environment) {
        for (Step<Boolean> step : subSteps) {
            if (!step.run(environment)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<Step<Boolean>> getSubsteps() {
        return subSteps;
    }
}
