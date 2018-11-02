package net.acomputerdog.securitycheckup.test.registry;

import net.acomputerdog.securitycheckup.test.Profile;
import net.acomputerdog.securitycheckup.test.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Bundle {
    private final Set<Test> tests;
    private final Set<Profile> profiles;

    // default constructor for deserialization
    private Bundle() {
        this(null, null);
    }

    public Bundle(TestRegistry testRegistry) {
        this(testRegistry.getTests(), testRegistry.getProfiles());
    }

    public Bundle(Collection<Test> tests, Collection<Profile> profiles) {
        this(new HashSet<>(tests), new HashSet<>(profiles));
    }

    public Bundle(Set<Test> tests, Set<Profile> profiles) {
        this.tests = tests;
        this.profiles = profiles;
    }

    public Set<Test> getTests() {
        return tests;
    }

    public Set<Profile> getProfiles() {
        return profiles;
    }

    public void addToRegistry(TestRegistry registry, boolean overwrite, boolean requireAdd) {
        for (Test test : tests) {
            if (overwrite || !registry.hasTest(test)) {
                registry.addTest(test);
            } else {
                if (requireAdd) {
                    throw new IllegalStateException("Registry already contains test: " + test.getInfo().getId());
                }
            }
        }

        for (Profile profile : profiles) {
            if (overwrite || !registry.hasProfile(profile)) {
                registry.addProfile(profile);
            } else {
                if (requireAdd) {
                    throw new IllegalStateException("Registry already contains profile: " + profile.getId());
                }
            }
        }
    }

    public void addToRegistry(TestRegistry registry) {
        this.addToRegistry(registry, true, false);
    }
}
