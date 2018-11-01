package net.acomputerdog.securitycheckup.test.step.score;

import net.acomputerdog.securitycheckup.test.TestEnvironment;
import net.acomputerdog.securitycheckup.test.step.Step;

import static net.acomputerdog.securitycheckup.test.TestResult.SCORE_FAIL;
import static net.acomputerdog.securitycheckup.test.TestResult.SCORE_PASS;

public class BoolToScoreStep implements Step<Float> {
    private final Step<Boolean> child;

    public BoolToScoreStep(Step<Boolean> child) {
        this.child = child;
    }

    public Step<Boolean> getChild() {
        return child;
    }

    @Override
    public Float run(TestEnvironment environment) {
        return child.run(environment) ? SCORE_PASS : SCORE_FAIL;
    }

}
