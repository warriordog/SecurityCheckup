package net.acomputerdog.securitycheckup.test.types.wmi;

import net.acomputerdog.jwmi.wbem.EnumWbemClassObject;
import net.acomputerdog.jwmi.wbem.WbemClassObject;
import net.acomputerdog.securitycheckup.test.TestResult;

/**
 * Parent class for a test type that involves checking a WMI query with multiple results
 */
public abstract class WMITestMulti extends WMITest {

    /**
     * Return value from testObj() that indicates that the test should stop with a FAIL result
     */
    public static final int FAIL = -1;
    /**
     * Return value from testObj() that indicates that the test should continue.
     */
    public static final int CONTINUE = 0;
    /**
     * Return value from testObj() that indicates that the test should stop with a PASS result;
     */
    public static final int PASS = 1;

    /**
     * If true, then a query with no matches is interpreted as a PASS.
     * If false, then no matches is interpreted as a FAIL.
     */
    private final boolean defaultPass;

    protected WMITestMulti(String id, String name, String description, String namespace, String query, boolean defaultPass) {
        super(id, name, description, namespace, query);
        this.defaultPass = defaultPass;
    }

    @Override
    public TestResult finishTest(EnumWbemClassObject results) {
        while (results.hasNext()) {
            try (WbemClassObject classObject = results.next()) {
                int status = testObj(classObject);
                switch (status) {
                    case FAIL:
                        setState(State.FINISHED);
                        return new TestResult(this).setScore(TestResult.SCORE_FAIL);
                    case PASS:
                        setState(State.FINISHED);
                        return new TestResult(this).setScore(TestResult.SCORE_PASS);
                    case CONTINUE:
                        break;
                    default:
                        throw new RuntimeException("Subtest returned invalid condition: " + status);
                }
            }
        }

        setState(State.FINISHED);
        if (defaultPass) {
            return new TestResult(this).setScore(TestResult.SCORE_PASS).setMessage("No results returned from query; default pass.");
        } else {
            return new TestResult(this).setScore(TestResult.SCORE_FAIL).setMessage("No results returned from query; default fail.");
        }
    }

    public boolean isDefaultPass() {
        return defaultPass;
    }

    /**
     * Implemented by subclasses to test each WbemClassObject returned by the query.
     * This method will be called once for each returned method, and subclasses should
     * return one of FAIL, CONTINUE, or PASS to control the test progress.
     *
     * @param obj The current object to test
     * @return return FAIL, CONTINUE, or PASS based on the object provided
     */
    public abstract int testObj(WbemClassObject obj);
}
