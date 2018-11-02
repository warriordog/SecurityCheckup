package net.acomputerdog.securitycheckup.test.registry;

import net.acomputerdog.securitycheckup.test.Profile;
import net.acomputerdog.securitycheckup.test.Test;

import java.util.*;

public class TestRegistry {
    private final Map<String, Test> testMap;
    private final Map<String, Profile> profileMap;

    private final Set<AddTestListener> addTestListeners = new HashSet<>();
    private final Set<RemoveTestListener> removeTestListeners = new HashSet<>();
    private final Set<AddProfileListener> addProfileListeners = new HashSet<>();
    private final Set<RemoveProfileListener> removeProfileListeners = new HashSet<>();

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

    public Profile getProfile(String id) {
        return profileMap.get(id);
    }

    public void addTest(Test test) {
        testMap.put(test.getInfo().getId(), test);
        addTestListeners.forEach(l -> l.onAddTest(test));
    }

    public void addProfile(Profile profile) {
        profileMap.put(profile.getId(), profile);
        addProfileListeners.forEach(l -> l.onAddProfile(profile));
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

    public boolean hasProfile(String id) {
        return profileMap.containsKey(id);
    }

    public boolean hasProfile(Profile profile) {
        return this.hasProfile(profile.getId());
    }

    public boolean hasTest(String id) {
        return testMap.containsKey(id);
    }

    public boolean hasTest(Test test) {
        return this.hasTest(test.getInfo().getId());
    }

    public void removeProfile(String id) {
        Profile profile = profileMap.remove(id);

        if (profile != null) {
            removeProfileListeners.forEach(l -> l.onRemoveProfile(profile));
        }
    }

    public void removeProfile(Profile profile) {
        this.removeProfile(profile.getId());
    }

    public void removeTest(String id) {
        Test test = testMap.remove(id);

        if (test != null) {
            for (Profile profile : profileMap.values()) {
                profile.removeTest(id);
            }

            removeTestListeners.forEach(l -> l.onRemoveTest(test));
        }
    }

    public void removeTest(Test test) {
        this.removeTest(test.getInfo().getId());
    }

    public void clear() {
        testMap.values().forEach(test -> removeTestListeners.forEach(l -> l.onRemoveTest(test)));
        profileMap.values().forEach(profile -> removeProfileListeners.forEach(l -> l.onRemoveProfile(profile)));

        testMap.clear();
        profileMap.clear();
    }

    /*
    Add test listeners
     */
    public void registerAddTestListener(AddTestListener listener) {
        addTestListeners.add(listener);
    }
    public void deregisterAddTestListener(AddTestListener listener) {
        addTestListeners.remove(listener);
    }

    /*
    Remove test listeners
     */
    public void registerRemoveTestListener(RemoveTestListener listener) {
        removeTestListeners.add(listener);
    }
    public void deregisterRemoveTestListener(RemoveTestListener listener) {
        removeTestListeners.remove(listener);
    }

    /*
    Add profile listeners
     */
    public void registerAddProfileListener(AddProfileListener listener) {
        addProfileListeners.add(listener);
    }
    public void deregisterAddProfileListener(AddProfileListener listener) {
        addProfileListeners.remove(listener);
    }

    /*
    Remove profile listeners
     */
    public void registerRemoveProfileListener(RemoveProfileListener listener) {
        removeProfileListeners.add(listener);
    }
    public void deregisterRemoveProfileListener(RemoveProfileListener listener) {
        removeProfileListeners.remove(listener);
    }

    public interface AddTestListener {
        void onAddTest(Test test);
    }

    public interface RemoveTestListener {
        void onRemoveTest(Test test);
    }

    public interface TestListener extends AddTestListener, RemoveTestListener {
        @Override
        default void onAddTest(Test test) {
            onTestEvent(test, Event.ADD);
        }

        @Override
        default void onRemoveTest(Test test) {
            onTestEvent(test, Event.REMOVE);
        }

        void onTestEvent(Test test, Event event);
    }

    public interface AddProfileListener {
        void onAddProfile(Profile profile);
    }

    public interface RemoveProfileListener {
        void onRemoveProfile(Profile profile);
    }

    public interface ProfileListener extends AddProfileListener, RemoveProfileListener {
        @Override
        default void onAddProfile(Profile profile) {
            onProfileEvent(profile, Event.ADD);
        }

        @Override
        default void onRemoveProfile(Profile profile) {
            onProfileEvent(profile, Event.REMOVE);
        }

        void onProfileEvent(Profile profile, Event event);
    }

    public interface RegistryListener extends TestListener, ProfileListener {

        @Override
        default void onTestEvent(Test test, Event event) {
            onEvent(test, event, Type.TEST);
        }


        @Override
        default void onProfileEvent(Profile profile, Event event) {
            onEvent(profile, event, Type.PROFILE);
        }

        void onEvent(Object object, Event event, Type type);
    }

    public enum Event {
        ADD,
        REMOVE
    }

    public enum Type {
        TEST,
        PROFILE
    }
}
