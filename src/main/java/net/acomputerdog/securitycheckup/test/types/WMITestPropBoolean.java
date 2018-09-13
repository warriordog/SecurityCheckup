package net.acomputerdog.securitycheckup.test.types;

import com.sun.jna.platform.win32.Variant;

public class WMITestPropBoolean extends WMITestProp {
    private final boolean expected;

    public WMITestPropBoolean(String id, String name, String description, String namespace, String query, String property, boolean expected) {
        super(id, name, description, namespace, query, property);
        this.expected = expected;
    }

    @Override
    public boolean checkProperty(Variant.VARIANT property) {
        return expected == property.booleanValue();
    }
}
