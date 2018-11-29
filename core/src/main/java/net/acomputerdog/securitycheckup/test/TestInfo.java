package net.acomputerdog.securitycheckup.test;

import net.acomputerdog.securitycheckup.util.Informable;

import java.net.URL;
import java.util.*;

public class TestInfo implements Informable {
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_FAILURE_ADVICE = "failure_advice";

    private final String id;
    private final String name;
    private final String description;
    private final String failureAdvice;

    private final Map<String, String> extraInfo = new HashMap<>();

    // constructor for deserializing
    private TestInfo() {
        this (null, null, null, null);
    }

    public TestInfo(String id, String name, String description, String failureAdvice) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.failureAdvice = failureAdvice;

        extraInfo.put(KEY_ID, id);
        extraInfo.put(KEY_NAME, name);
        extraInfo.put(KEY_DESCRIPTION, description);
        extraInfo.put(KEY_FAILURE_ADVICE, failureAdvice);
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getFailureAdvice() {
        return failureAdvice;
    }

    public void addInfo(String key, String value) {
        extraInfo.put(key, value);
    }

    public URL getFixURL() {
        // TODO embed in bundle
        return getClass().getResource("/doc/fix/id/" + id + "/index.html");
    }

    @Override
    public String getInfo(String key) {
        return extraInfo.get(key);
    }

    @Override
    public Map<String, String> getInfoMap() {
        return Collections.unmodifiableMap(extraInfo);
    }

    @Override
    public List<String> getLabeledInfo() {
        List<String> info = new ArrayList<>(extraInfo.size());
        extraInfo.forEach((key, value) -> info.add(key + ": " + value));
        return info;
    }

    @Override
    public String toString() {
        return getName();
    }
}
