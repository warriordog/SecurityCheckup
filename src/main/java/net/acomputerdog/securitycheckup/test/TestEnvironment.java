package net.acomputerdog.securitycheckup.test;

import net.acomputerdog.jwmi.wbem.WbemLocator;

import java.util.HashMap;
import java.util.Map;

public class TestEnvironment {
    private final Map<Object, Object> sharedResources = new HashMap<>();

    private final WbemLocator wbemLocator;

    public TestEnvironment(WbemLocator wbemLocator) {
        this.wbemLocator = wbemLocator;
    }

    public boolean hasSharedResource(Object key) {
        return sharedResources.containsKey(key);
    }

    public <T> T getSharedResource(Object key) {
        return (T)sharedResources.get(key);
    }

    public void setSharedResource(Object key, Object value) {
        sharedResources.put(key, value);
    }

    public void clearSharedResources() {
        sharedResources.clear();
    }

    public WbemLocator getWbemLocator() {
        return wbemLocator;
    }
}
