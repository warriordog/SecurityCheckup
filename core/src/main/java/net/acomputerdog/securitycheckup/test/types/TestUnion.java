package net.acomputerdog.securitycheckup.test.types;

import net.acomputerdog.securitycheckup.test.Test;
import net.acomputerdog.securitycheckup.test.TestEnvironment;
import net.acomputerdog.securitycheckup.test.TestResult;

import java.util.ArrayList;
import java.util.List;

import static net.acomputerdog.securitycheckup.test.TestResult.SCORE_FAIL;
import static net.acomputerdog.securitycheckup.test.TestResult.SCORE_PASS;

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

    public TestUnion addTest(Test test) {
        return addTest(new Subtest(test));
    }

    public TestUnion addTest(Subtest subtest) {
        subtests.add(subtest);
        return this;
    }

    @Override
    protected TestResult runTestSafe(TestEnvironment environment) {
        if (subtests.isEmpty()) {
            this.setState(State.NOT_APPLICABLE);
            return new TestResult(this).setScore(SCORE_PASS).setMessage("No subtests were run.");
        }

        StringBuilder message = new StringBuilder();
        List<SubtestResults> results = new ArrayList<>();
        for (Subtest subtest : subtests) {
            Test test = subtest.getTest();
            try {

                // get results and invert if needed
                TestResult result = test.runTest(environment);
                result.setInverted(subtest.inverted);

                boolean skip = false;
                boolean forcePassed = false;
                boolean forceFailed = false;

                // Skip NOT_APPLICABLE (if set)
                if (result.getState() == State.NOT_APPLICABLE && subtest.skipNotApplicable) {
                    skip = true;
                }

                switch (subtest.getScoringMode()) {
                    case AVERAGE:
                        break;
                    case MUST_PASS:
                        if (!result.passed()) {
                            forceFailed = true;
                        }
                        break;
                    case FILTER:
                        if (result.passed()) {
                            skip = true;
                        } else {
                            forceFailed = true;
                        }
                        break;
                    case SHORT_CIRCUIT:
                        if (result.passed()) {
                            forcePassed = true;
                        } else {
                            skip = true;
                        }
                        break;
                    default:
                        throw new IllegalArgumentException("Illegal scoring mode: " + subtest.getScoringMode());
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

                results.add(new SubtestResults(subtest, result, skip, forcePassed, forceFailed));
            } catch (Throwable t) {
                results.add(new SubtestResults(subtest,
                        new TestResult(test).setScore(SCORE_FAIL).setException(t).setMessage("Subtest threw exception: " + t.toString()),
                        false, false, false));
            }
        }

        float totalScore = 0.0f;
        float count = 0f;

        for (SubtestResults subtestResults : results) {
            TestResult result = subtestResults.result;

            if (!subtestResults.skip) {
                if (subtestResults.forceFailed) {
                    totalScore = SCORE_FAIL;
                    count = 1f;
                    break;
                } else if (subtestResults.forcePassed) {
                    totalScore = SCORE_PASS;
                    count = 1f;
                    break;
                } else {
                    totalScore += result.getScore();
                    count++;
                }
            }
        }

        float finalScore = count == 0f ? 0f : totalScore / count;

        this.setState(State.FINISHED);
        return new TestResult(this).setScore(finalScore).setMessage(message.toString());
    }

    private static class SubtestResults {
        private final Subtest subtest;
        private final TestResult result;
        private final boolean skip;
        private final boolean forcePassed;
        private final boolean forceFailed;

        private SubtestResults(Subtest subtest, TestResult result, boolean skip, boolean forcePassed, boolean forceFailed) {
            this.subtest = subtest;
            this.result = result;
            this.skip = skip;
            this.forcePassed = forcePassed;
            this.forceFailed = forceFailed;
        }
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

        /**
         * Determines how this subtest will be scored
         */
        private ScoringMode scoringMode = ScoringMode.MUST_PASS;

        /**
         * If true, this subtest's score will be inverted.
         *
         * newScore = 1 - oldScore;
         * passed = !passed;
         */
        private boolean inverted = false;

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

        public ScoringMode getScoringMode() {
            return scoringMode;
        }

        public Subtest setScoringMode(ScoringMode scoringMode) {
            this.scoringMode = scoringMode;

            return this;
        }

        public boolean isInverted() {
            return inverted;
        }

        public Subtest setInverted(boolean inverted) {
            this.inverted = inverted;

            return this;
        }

        public Test getTest() {
            return test;
        }
    }

    /**
     * Scoring mode for a subtest
     */
    public enum ScoringMode {
        /**
         * Subtest score will be averaged with others
         */
        AVERAGE,

        /**
         * This subtest must pass.  If it passes, then its score is averaged with the others.
         * If it fails, then the test fails.
         */
        MUST_PASS,

        /**
         * This subtest acts as a filter.  If it passes, then its score is disregarded and the
         * test continues.  If it fails, then the test fails.
         */
        FILTER,

        /**
         * This subtest short-circuits the test.  If it passes, then the test passes.  If it fails,
         * then it is disregarded.
         */
        SHORT_CIRCUIT
    }
}
