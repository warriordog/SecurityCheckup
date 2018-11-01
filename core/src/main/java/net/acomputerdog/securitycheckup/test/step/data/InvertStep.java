package net.acomputerdog.securitycheckup.test.step.data;

import net.acomputerdog.securitycheckup.test.TestEnvironment;
import net.acomputerdog.securitycheckup.test.step.Step;

import java.util.Collections;
import java.util.List;

public class InvertStep extends Step<Boolean> {
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

    @Override
    public List<Step<Boolean>> getSubsteps() {
        return Collections.singletonList(source);
    }
}
