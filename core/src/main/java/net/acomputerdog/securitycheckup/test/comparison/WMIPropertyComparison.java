package net.acomputerdog.securitycheckup.test.comparison;

import net.acomputerdog.jwmi.nat.ReleasableVariant;
import net.acomputerdog.jwmi.wbem.WbemClassObject;

public class WMIPropertyComparison<T> implements Comparison<WbemClassObject, T> {
    private final Comparison<T, T> passthrough;
    private final String property;

    public WMIPropertyComparison(Comparison<T, T> passthrough, String property) {
        this.passthrough = passthrough;
        this.property = property;
    }

    public Comparison<T, T> getPassthrough() {
        return passthrough;
    }

    public String getProperty() {
        return property;
    }

    @Override
    public boolean compare(WbemClassObject a, T b) {
        try (ReleasableVariant var = a.get(property)) {
            return passthrough.compare((T)var.getJavaType(), b);
        }
    }
}
