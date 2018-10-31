package net.acomputerdog.securitycheckup.test.registry;

import net.acomputerdog.securitycheckup.test.Profile;
import net.acomputerdog.securitycheckup.test.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TestRegistry {
    private final Map<String, Test> testMap;
    private final Map<String, Profile> profileMap;

    public TestRegistry() {
        this(new HashMap<>(), new HashMap<>());
    }

    public TestRegistry(Map<String, Profile> profileMap, Map<String, Test> testMap) {
        this.testMap = testMap;
        this.profileMap = profileMap;
    }

    public Test getTest(String id) {
        return testMap.get(id);
    }

    public void addTest(Test test) {
        testMap.put(test.getInfo().getId(), test);
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

    public void removeProfile(String id) {
        profileMap.remove(id);
    }

    public void removeProfile(Profile profile) {
        this.removeProfile(profile.getId());
    }

    public void removeTest(String id) {
        testMap.remove(id);

        for (Profile profile : profileMap.values()) {
            profile.removeTest(id);
        }
    }

    public void removeTest(Test test) {
        this.removeTest(test.getInfo().getId());
    }

    public void clear() {
        testMap.clear();
        profileMap.clear();
    }

    public boolean hasProfile(String id) {
        return profileMap.containsKey(id);
    }

    public boolean hasTest(String id) {
        return testMap.containsKey(id);
    }
}
