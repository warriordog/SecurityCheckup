package net.acomputerdog.securitycheckup.test.step.wmi;

import net.acomputerdog.jwmi.wbem.EnumWbemClassObject;
import net.acomputerdog.jwmi.wbem.WbemClassObject;
import net.acomputerdog.securitycheckup.test.TestEnvironment;
import net.acomputerdog.securitycheckup.test.step.Step;

import java.util.Collections;
import java.util.List;

public class GetFirstClsObjStep extends Step<WbemClassObject> {
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

    @Override
    public List<Step> getSubsteps() {
        return Collections.singletonList(source);
    }
}
