package net.acomputerdog.securitycheckup.test.types.wmi;

import com.sun.jna.platform.win32.Variant;
import net.acomputerdog.jwmi.nat.ReleasableVariant;
import net.acomputerdog.jwmi.wbem.WbemClassObject;
import net.acomputerdog.securitycheckup.test.TestResult;

/**
 * Parent class for any test type that involves testing a single WMI property.
 *
 * If a query run by this class returns multiple results, then only the first is used.
 */
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
                return new TestResult(this).setScore(TestResult.SCORE_PASS).setMessage("WMI property matched.");
            } else {
                return new TestResult(this).setScore(TestResult.SCORE_FAIL).setMessage("WMI property did not match.");
            }
        }
    }

    /**
     * Implemented by subclasses to check if the requested property passes the test
     * @param property The property to test
     * @return return true if the test passes, false otherwise
     */
    public abstract boolean checkProperty(Variant.VARIANT property);

}
