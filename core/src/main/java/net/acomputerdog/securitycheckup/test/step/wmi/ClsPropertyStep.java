package net.acomputerdog.securitycheckup.test.step.wmi;

import net.acomputerdog.jwmi.nat.ReleasableVariant;
import net.acomputerdog.jwmi.wbem.WbemClassObject;
import net.acomputerdog.securitycheckup.test.TestEnvironment;
import net.acomputerdog.securitycheckup.test.step.Step;

import java.util.Collections;
import java.util.List;

public class ClsPropertyStep<T> extends Step<T> {
    private final Step<WbemClassObject> source;
    private final String property;

    public ClsPropertyStep(Step<WbemClassObject> source, String property) {
        this.source = source;
        this.property = property;
    }

    public Step<WbemClassObject> getSource() {
        return source;
    }

    public String getProperty() {
        return property;
    }

    @Override
    public T run(TestEnvironment environment) {
        try (WbemClassObject cls = source.run(environment)) {
            try (ReleasableVariant var = cls.get(property)) {
                return (T)var.getJavaType();
            }
        }
    }

    @Override
    public List<Step<WbemClassObject>> getSubsteps() {
        return Collections.singletonList(source);
    }
}
