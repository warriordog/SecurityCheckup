package net.acomputerdog.securitycheckup.test.registry;

import net.acomputerdog.securitycheckup.test.Profile;
import net.acomputerdog.securitycheckup.test.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TestRegistry {
    private final Map<String, Test> testMap = new HashMap<>();
    private final Map<String, Profile> profileMap = new HashMap<>();

    public Test getTest(String id) {
        return testMap.get(id);
    }

    public void addTest(Test test) {
        testMap.put(test.getInfo().getID(), test);
    }

    public Profile getProfile(String id) {
        return profileMap.get(id);
    }

    public void addProfile(Profile profile) {
        profileMap.put(profile.getId(), profile);
    }

    public Map<String, Test> getTestMap() {
        return Collections.unmodifiableMap(testMap);
    }

    public Map<String, Profile> getProfileMap() {
        return Collections.unmodifiableMap(profileMap);
    }

    public Collection<Test> getTests() {
        return Collections.unmodifiableCollection(testMap.values());
    }

    public Collection<Profile> getProfiles() {
        return Collections.unmodifiableCollection(profileMap.values());
    }

    public int getProfileCount() {
        return profileMap.size();
    }

    public int getTestCount() {
        return testMap.size();
    }

    public void clear() {
        testMap.clear();
        profileMap.clear();
    }
}
