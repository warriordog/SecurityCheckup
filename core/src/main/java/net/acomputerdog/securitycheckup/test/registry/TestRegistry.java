package net.acomputerdog.securitycheckup.test.registry;

import net.acomputerdog.securitycheckup.test.Profile;
import net.acomputerdog.securitycheckup.test.Test;

import java.util.HashMap;
import java.util.Map;

public class TestRegistry {
    private final Map<String, Test> testMap = new HashMap<>();
    private final Map<String, Profile> profileMap = new HashMap<>();

    public Test getTest(String id) {
        return testMap.get(id);
    }

    public Profile getProfile(String id) {
        return profileMap.get(id);
    }

    public void addTest(Test test) {
        testMap.put(test.getInfo().getID(), test);
    }

    public void addProfile(Profile profile) {
        profileMap.put(profile.getId(), profile);
    }
}
