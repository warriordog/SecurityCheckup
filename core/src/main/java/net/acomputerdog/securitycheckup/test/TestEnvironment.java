package net.acomputerdog.securitycheckup.test;

import net.acomputerdog.jwmi.wbem.WbemLocator;

import java.util.*;

public class TestEnvironment {
    /**
     * Collection of identified objects to be shared between tests
     */
    private final Map<Object, Object> sharedResources = new HashMap<>();

    /**
     * Shared WbemLocator for connecting to WMI
     */
    private final WbemLocator wbemLocator;

    /**
     * List of messages generated during testing
     */
    private final List<String> testMessages = new ArrayList<>();

    private Test currentTest;

    public TestEnvironment(WbemLocator wbemLocator) {
        this.wbemLocator = wbemLocator;
    }

    public void setCurrentTest(Test currentTest) {
        this.currentTest = currentTest;
    }

    public Test getCurrentTest() {
        return currentTest;
    }

    public List<String> getTestMessages() {
        return testMessages;
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

    public Collection<Object> getAllResources() {
        return Collections.unmodifiableCollection(sharedResources.values());
    }

    public WbemLocator getWbemLocator() {
        return wbemLocator;
    }
}
