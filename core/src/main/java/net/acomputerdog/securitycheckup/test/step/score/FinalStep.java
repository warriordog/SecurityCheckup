package net.acomputerdog.securitycheckup.test.step.score;

import net.acomputerdog.securitycheckup.test.TestEnvironment;
import net.acomputerdog.securitycheckup.test.TestResult;
import net.acomputerdog.securitycheckup.test.step.Step;

public class FinalStep implements Step<TestResult> {
    private final Step<Float> child;

    public FinalStep(Step<Float> child) {
        this.child = child;
    }

    public Step<Float> getChild() {
        return child;
    }

    @Override
    public TestResult run(TestEnvironment environment) {
        TestResult result;

        try {
            Float score = child.run(environment);
            if (score != null) {
                result = TestResult.createNormalScore(environment.getCurrentTest().getInfo(), environment.getTestMessages(), score);
            } else {
                result = new TestResult(environment.getCurrentTest().getInfo(), false, TestResult.SCORE_FAIL, TestResult.ResultCause.FAILED, environment.getTestMessages());
                result.setExtraInfo(TestResult.KEY_MESSAGE, "Child test returned null");
            }
        } catch (Exception e) {
            System.err.println("Exception during test");
            e.printStackTrace();

            result = new TestResult(environment.getCurrentTest().getInfo(), false, TestResult.SCORE_FAIL, TestResult.ResultCause.EXCEPTION, environment.getTestMessages());
            result.setExtraInfo(TestResult.KEY_EXCEPTION, e.getMessage());
        }

        return result;
    }

}
