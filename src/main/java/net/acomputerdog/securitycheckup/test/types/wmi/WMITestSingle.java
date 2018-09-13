package net.acomputerdog.securitycheckup.test.types.wmi;

import net.acomputerdog.jwmi.wbem.EnumWbemClassObject;
import net.acomputerdog.jwmi.wbem.WbemClassObject;
import net.acomputerdog.securitycheckup.test.TestResult;

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
            return new TestResult(this, TestResult.SCORE_FAIL).setMessage("No results returned from query.");
        }
    }

    public abstract TestResult testClass(WbemClassObject classObject);
}
