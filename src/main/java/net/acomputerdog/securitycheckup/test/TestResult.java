package net.acomputerdog.securitycheckup.test;

public class TestResult {
    public static final float SCORE_PASS = 1.0f;
    public static final float SCORE_FAIL = 0.0f;

    /*
    These fields are mandatory
     */
    private final Test test;
    private final Test.State state;
    private final float score;

    /*
    These fields are optional
     */
    private Throwable exception;
    private String message;

    public TestResult(Test test, Test.State state, float score) {
        this.test = test;
        this.state = state;
        this.score = score;
    }

    public TestResult(Test test, float score) {
        this(test, test.getCurrentState(), score);
    }

    public Test getTest() {
        return test;
    }

    public Test.State getState() {
        return state;
    }

    public float getScore() {
        return score;
    }

    public Throwable getException() {
        return exception;
    }

    public String getMessage() {
        return message;
    }

    public String getResultString() {
        if (state == Test.State.FINISHED) {
            if (score == SCORE_PASS) {
                return "PASS";
            } else if (score == SCORE_FAIL) {
                return "FAIL";
            } else {
                return "PARTIAL";
            }
        } else if (state == Test.State.ERROR) {
            return "ERROR";
        } else if (state == Test.State.RUNNING) {
            return "TIMEOUT";
        } else {
            return "UNKNOWN";
        }
    }

    public TestResult setException(Throwable exception) {
        this.exception = exception;

        return this;
    }

    public TestResult setMessage(String message) {
        this.message = message;

        return this;
    }
}
