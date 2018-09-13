package net.acomputerdog.securitycheckup.test.types.wmi;

import com.sun.jna.platform.win32.Variant;

public class WMITestPropInt extends WMITestProp {
    private final int expected;

    public WMITestPropInt(String id, String name, String description, String namespace, String query, String property, int expected) {
        super(id, name, description, namespace, query, property);
        this.expected = expected;
    }

    @Override
    public boolean checkProperty(Variant.VARIANT property) {
        return expected == property.intValue();
    }
}
