package net.acomputerdog.securitycheckup.test.step.wmi;

import net.acomputerdog.jwmi.wbem.EnumWbemClassObject;
import net.acomputerdog.jwmi.wbem.WbemClassObject;
import net.acomputerdog.securitycheckup.test.TestEnvironment;
import net.acomputerdog.securitycheckup.test.step.Step;

public class GetFirstClsObjStep implements Step<WbemClassObject> {
    private final WMIStep source;

    public GetFirstClsObjStep(WMIStep source) {
        this.source = source;
    }

    public WMIStep getSource() {
        return source;
    }

    @Override
    public WbemClassObject run(TestEnvironment environment) {
        try (EnumWbemClassObject enumWbemClassObject = source.run(environment)) {
            return enumWbemClassObject.hasNext() ? enumWbemClassObject.next() : null;
        }
    }

}
