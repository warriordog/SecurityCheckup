package net.acomputerdog.securitycheckup.test;

import net.acomputerdog.jwmi.wbem.WbemLocator;

import java.util.HashMap;
import java.util.Map;

/**
 * Environment for the current run of tests
 */
public class TestEnvironment {
    /**
     * Collection of identified objects to be shared between tests
     */
    private final Map<Object, Object> sharedResources = new HashMap<>();

    /**
     * Shared WbemLocator for connecting to WMI
     */
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
