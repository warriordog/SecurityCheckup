package net.acomputerdog.securitycheckup.test;

/**
 * The results of a test
 */
public class TestResult {
    public static final float SCORE_PASS = 1.0f;
    public static final float SCORE_FAIL = 0.0f;

    private final Test test;

    private Test.State state;
    private float score = SCORE_FAIL;
    private Throwable exception;
    private String message;

    /**
     * If true, score and results will be inverted.
     *
     * score = 1 - score;
     * passed() = !passed();
     */
    private boolean inverted = false;

    public TestResult(Test test) {
        this.test = test;
        this.state = test.getCurrentState();
    }

    public TestResult setState(Test.State state) {
        this.state = state;

        return this;
    }

    public TestResult setScore(float score) {
        this.score = score;

        return this;
    }

    public TestResult setInverted(boolean inverted) {
        this.inverted = inverted;

        return this;
    }

    public TestResult setException(Throwable exception) {
        this.exception = exception;

        return this;
    }

    public TestResult setMessage(String message) {
        this.message = message;

        return this;
    }

    public Test getTest() {
        return test;
    }

    public Test.State getState() {
        return state;
    }

    public float getScore() {
        if (inverted) {
            return 1 - score;
        } else {
            return score;
        }
    }

    public Throwable getException() {
        return exception;
    }

    public String getMessage() {
        return message;
    }

    public boolean isInverted() {
        return inverted;
    }

    /**
     * Gets a human-readable string representing the overall result of this test
     * @return Return a string representing final results
     */
    public String getResultString() {
        switch (state) {
            case FINISHED:
                if (score == SCORE_PASS) {
                    return "PASS";
                } else if (score == SCORE_FAIL) {
                    return "FAIL";
                } else {
                    return "PARTIAL";
                }
            case ERROR:
                return "ERROR";
            case RUNNING:
                return "TIMEOUT";
            case NOT_APPLICABLE:
            case INCOMPATIBLE:
                return "SKIPPED";
            default:
                return "UNKNOWN";
        }
    }

    /**
     * Checks if this test passed, given a provided minimum "passing" score
     *
     * @param passingScore Minimum score needed to pass
     * @return Return true if score >= passing score
     */
    public boolean passed(float passingScore) {
        boolean passed = this.getScore() >= passingScore;
        if (inverted) {
            return !passed;
        } else {
            return passed;
        }
    }

    /**
     * Checks if this test passed with a perfect score (score == SCORE_PASS == 1.0f)
     * @return Return true if this test passed with a perfect score
     */
    public boolean passed() {
        return passed(SCORE_PASS);
    }
}
