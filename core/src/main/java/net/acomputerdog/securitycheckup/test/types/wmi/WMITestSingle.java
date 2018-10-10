package net.acomputerdog.securitycheckup.test.types.wmi;

import net.acomputerdog.jwmi.wbem.EnumWbemClassObject;
import net.acomputerdog.jwmi.wbem.WbemClassObject;
import net.acomputerdog.securitycheckup.test.TestResult;

/**
 * Parent class for tests that involve only a single query result.
 *
 * If a query returns multiple results, then only the first is used.
 */
public abstract class WMITestSingle extends WMITest {
    protected WMITestSingle(String id, String name, String description, String namespace, String query) {
        super(id, name, description, namespace, query);
    }

    @Override
    public TestResult finishTest(EnumWbemClassObject results) {
        if (results.hasNext()) {
            try (WbemClassObject classObject = results.next()) {
                return testClass(classObject);
            }
        } else {
            setState(State.FINISHED);
            return new TestResult(this).setScore(TestResult.SCORE_FAIL).setMessage("No results returned from query.");
        }
    }

    /**
     * Implemented by subclasses to test if the provided object passes the test.
     * @param classObject The object to test
     * @return return true if the test passes, false otherwise
     */
    public abstract TestResult testClass(WbemClassObject classObject);
}
