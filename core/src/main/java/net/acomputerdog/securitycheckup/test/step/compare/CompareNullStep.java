package net.acomputerdog.securitycheckup.test.step.compare;

import net.acomputerdog.securitycheckup.test.TestEnvironment;
import net.acomputerdog.securitycheckup.test.step.Step;

public class CompareNullStep implements Step<Boolean> {
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

}
