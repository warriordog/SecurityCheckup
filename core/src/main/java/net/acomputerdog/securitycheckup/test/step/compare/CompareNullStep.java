package net.acomputerdog.securitycheckup.test.step.compare;

import net.acomputerdog.securitycheckup.test.TestEnvironment;
import net.acomputerdog.securitycheckup.test.step.Step;

import java.util.Collections;
import java.util.List;

public class CompareNullStep extends Step<Boolean> {
    private final Step child;

    public CompareNullStep(Step child) {
        this.child = child;
    }

    public Step getChild() {
        return child;
    }

    @Override
    public Boolean run(TestEnvironment environment) {
        return child.run(environment) == null;
    }

    @Override
    public List<Step> getSubsteps() {
        return Collections.singletonList(child);
    }
}
