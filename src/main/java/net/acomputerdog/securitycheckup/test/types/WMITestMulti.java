package net.acomputerdog.securitycheckup.test.types;

import net.acomputerdog.jwmi.wbem.EnumWbemClassObject;
import net.acomputerdog.jwmi.wbem.WbemClassObject;
import net.acomputerdog.securitycheckup.test.TestResult;

public abstract class WMITestMulti extends WMITest {

    public static final int FAIL = -1;
    public static final int CONTINUE = 0;
    public static final int PASS = 1;

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
                        return new TestResult(this, TestResult.SCORE_FAIL);
                    case PASS:
                        setState(State.FINISHED);
                        return new TestResult(this, TestResult.SCORE_PASS);
                    case CONTINUE:
                        break;
                    default:
                        throw new RuntimeException("Subtest returned invalid condition: " + status);
                }
            }
        }

        setState(State.FINISHED);
        if (defaultPass) {
            return new TestResult(this, TestResult.SCORE_PASS).setMessage("No results returned from query; default pass.");
        } else {
            return new TestResult(this, TestResult.SCORE_FAIL).setMessage("No results returned from query; default fail.");
        }
    }

    public boolean isDefaultPass() {
        return defaultPass;
    }

    public abstract int testObj(WbemClassObject obj);
}
