package net.acomputerdog.securitycheckup.main.gui.test;

import net.acomputerdog.securitycheckup.test.Test;

import java.util.Objects;

public class ProfileTest {
    private final Profile profile;
    private final Test test;

    public ProfileTest(Profile profile, Test test) {
        this.profile = profile;
        this.test = test;
    }

    public Profile getProfile() {
        return profile;
    }

    public Test getTest() {
        return test;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProfileTest)) return false;
        ProfileTest that = (ProfileTest) o;
        return Objects.equals(profile, that.profile) &&
                Objects.equals(test, that.test);
    }

    @Override
    public int hashCode() {
        return Objects.hash(profile, test);
    }

    @Override
    public String toString() {
        return this.test.getName();
    }
}
