package net.acomputerdog.securitycheckup.test.step.data;

import net.acomputerdog.securitycheckup.test.TestEnvironment;
import net.acomputerdog.securitycheckup.test.step.Step;

public class InvertStep implements Step<Boolean> {
    private final Step<Boolean> source;

    public InvertStep(Step<Boolean> source) {
        this.source = source;
    }

    public Step<Boolean> getSource() {
        return source;
    }

    @Override
    public Boolean run(TestEnvironment environment) {
        return !source.run(environment);
    }

}
