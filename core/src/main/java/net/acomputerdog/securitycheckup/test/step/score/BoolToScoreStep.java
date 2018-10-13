package net.acomputerdog.securitycheckup.test.step.score;

import net.acomputerdog.securitycheckup.test.TestEnvironment;
import net.acomputerdog.securitycheckup.test.step.Step;

import java.util.Arrays;
import java.util.List;

import static net.acomputerdog.securitycheckup.test.TestResult.SCORE_FAIL;
import static net.acomputerdog.securitycheckup.test.TestResult.SCORE_PASS;

public class BoolToScoreStep extends Step<Float> {
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

    @Override
    public List<Step> getSubsteps() {
        return Arrays.asList(new Step[]{child});
    }
}
