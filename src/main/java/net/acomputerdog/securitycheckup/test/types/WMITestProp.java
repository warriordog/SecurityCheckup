package net.acomputerdog.securitycheckup.test.types;

import com.sun.jna.platform.win32.Variant;
import net.acomputerdog.jwmi.nat.ReleasableVariant;
import net.acomputerdog.jwmi.wbem.WbemClassObject;
import net.acomputerdog.securitycheckup.test.TestResult;

public abstract class WMITestProp extends WMITestSingle {
    private final String property;

    protected WMITestProp(String id, String name, String description, String namespace, String query, String property) {
        super(id, name, description, namespace, query);
        this.property = property;
    }

    @Override
    public TestResult testClass(WbemClassObject classObject) {
        try (ReleasableVariant variant = classObject.get(property)) {
            setState(State.FINISHED);
            if (checkProperty(variant)) {
                return new TestResult(this, TestResult.SCORE_PASS).setMessage("WMI property matched.");
            } else {
                return new TestResult(this, TestResult.SCORE_FAIL).setMessage("WMI property did not match.");
            }
        }
    }

    public abstract boolean checkProperty(Variant.VARIANT property);

}
