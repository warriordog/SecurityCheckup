package net.acomputerdog.securitycheckup.test;

import net.acomputerdog.securitycheckup.util.Informable;

import java.util.*;

public class TestResult implements Informable {
    public static String KEY_PASSED = "Passed";
    public static String KEY_SCORE = "Score";
    public static String KEY_CAUSE = "Cause";
    public static String KEY_EXCEPTION = "Exception";
    public static String KEY_MESSAGE = "Message";
    public static String KEY_STRING = "String";

    public static final float SCORE_PASS = 1.0f;
    public static final float SCORE_FAIL = 0.0f;

    private final boolean passed;
    private final float score;
    private final ResultCause resultCause;

    private final TestInfo testInfo;

    private final Map<String, String> extraInfo = new HashMap<>();

    public TestResult(TestInfo testInfo, boolean passed, float score, ResultCause resultCause) {
        this.testInfo = testInfo;
        this.passed = passed;
        this.score = score;
        this.resultCause = resultCause;

        extraInfo.put(KEY_PASSED, String.valueOf(passed));
        extraInfo.put(KEY_SCORE, getScoreString());
        extraInfo.put(KEY_CAUSE, getResultCause().prettyName);
        extraInfo.put(KEY_STRING, getResultString());
    }

    public boolean isPassed() {
        return passed;
    }

    public float getScore() {
        return score;
    }

    public ResultCause getResultCause() {
        return resultCause;
    }

    public void setExtraInfo(String key, String value) {
        extraInfo.put(key, value);
    }

    public TestInfo getTestInfo() {
        return testInfo;
    }

    public String getResultString() {
        switch (resultCause) {
            case FINISHED:
                if (score == SCORE_PASS) {
                    return "PASS";
                } else if (score == SCORE_FAIL) {
                    return "FAIL";
                } else {
                    return "PARTIAL";
                }
            case FAILED:
            case EXCEPTION:
                return "ERROR";
            case SKIPPED:
            case INCOMPATIBLE:
            case NOT_RUN:
                return "SKIPPED";
            default:
                return "UNKNOWN";
        }
    }

    public String getScoreString() {
        return formatScore(this.getScore());
    }

    @Override
    public String getInfo(String key) {
        return extraInfo.get(key);
    }

    @Override
    public Map<String, String> getInfoMap() {
        return Collections.unmodifiableMap(extraInfo);
    }

    @Override
    public List<String> getLabeledInfo() {
        List<String> info = new ArrayList<>(extraInfo.size());
        extraInfo.forEach((key, value) -> info.add(key + ": " + value));
        return info;
    }

    public static TestResult createNormalScore(TestInfo testInfo, float score) {
        return new TestResult(testInfo, score == SCORE_PASS, score, ResultCause.FINISHED);
    }

    public static TestResult createNormalPass(TestInfo testInfo) {
        return new TestResult(testInfo, true, SCORE_PASS, ResultCause.FINISHED);
    }

    public static TestResult createNormalFail(TestInfo testInfo) {
        return new TestResult(testInfo, false, SCORE_FAIL, ResultCause.FINISHED);
    }

    public enum ResultCause {
        FINISHED("Finished"),
        SKIPPED("Skipped"),
        NOT_RUN("Not run"),
        INCOMPATIBLE("Incompatible"),
        FAILED("Failed"),
        EXCEPTION("Exception");

        public final String prettyName;

        ResultCause(String prettyName) {
            this.prettyName = prettyName;
        }
    }

    public static String formatScore(float score) {
        return String.format("%1.0f%%", score * 100f);
    }
}
