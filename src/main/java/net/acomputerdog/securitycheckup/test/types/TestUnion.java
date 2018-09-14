package net.acomputerdog.securitycheckup.test.types;

import net.acomputerdog.securitycheckup.test.Test;
import net.acomputerdog.securitycheckup.test.TestEnvironment;
import net.acomputerdog.securitycheckup.test.TestResult;

import java.util.ArrayList;
import java.util.List;

import static net.acomputerdog.securitycheckup.test.TestResult.SCORE_FAIL;

/**
 * Test that combines multiple other tests into a single unit
 */
public class TestUnion extends BasicTest {
    /**
     * List of tests to run
     */
    private final List<Subtest> subtests = new ArrayList<>();

    public TestUnion(String id, String name, String description) {
        super(id, name, description);
    }

    public TestUnion(String id, String name) {
        super(id, name);
    }

    public TestUnion(String id) {
        super(id);
    }

    public TestUnion addTest(Subtest subtest) {
        subtests.add(subtest);
        return this;
    }

    @Override
    protected TestResult runTestSafe(TestEnvironment environment) {
        if (subtests.isEmpty()) {
            this.setState(State.NOT_APPLICABLE);
            return new TestResult(this, TestResult.SCORE_PASS).setMessage("No subtests were run.");
        }

        StringBuilder message = new StringBuilder();
        float totalScore = 0.0f;
        float count = 0f;
        for (Subtest subtest : subtests) {
            try {
                Test test = subtest.test;

                TestResult result = test.runTest(environment);

                // If state is not NA or [it is NA and] we include NA, then include
                if (result.getState() != State.NOT_APPLICABLE || !subtest.skipNotApplicable) {
                    totalScore += result.getScore();
                    count++;
                }

                message.append("\n");
                message.append(test.getID());
                message.append(": ");
                message.append(result.getResultString());
                if (result.getMessage() != null) {
                    message.append(" - ");
                    message.append(result.getMessage());
                }
                if (result.getException() != null) {
                    message.append("(");
                    message.append(result.getException().toString());
                    message.append(")");
                }
            } catch (Throwable t) {
                totalScore += SCORE_FAIL;
            }
        }
        float finalScore = count == 0f ? 0f : totalScore / count;

        this.setState(State.FINISHED);
        return new TestResult(this, finalScore).setMessage(message.toString());
    }

    /**
     * A test to run within a TestUnion, along with parameters to control its execution
     */
    public static class Subtest {
        /**
         * Test to run
         */
        private final Test test;

        /**
         * If true, and this test returns NOT_APPLICABLE, then its score will not be included in the final
         * score calculation.
         */
        private boolean skipNotApplicable = false;

        public Subtest(Test test) {
            this.test = test;
        }

        public boolean isSkipNotApplicable() {
            return skipNotApplicable;
        }

        public Subtest setSkipNotApplicable(boolean skipNotApplicable) {
            this.skipNotApplicable = skipNotApplicable;

            return this;
        }

        public Test getTest() {
            return test;
        }
    }
}
