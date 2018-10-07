package net.acomputerdog.securitycheckup.main.gui.test;

import net.acomputerdog.securitycheckup.test.suite.TestSuite;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Profile {
    private final TestSuite testSuite;
    private final List<ProfileTest> tests;

    public Profile(TestSuite testSuite) {
        this.testSuite = testSuite;
        this.tests = new ArrayList<>();
        testSuite.getTests().forEach(test -> tests.add(new ProfileTest(this, test)));
    }

    public TestSuite getTestSuite() {
        return testSuite;
    }

    public List<ProfileTest> getTests() {
        return tests;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Profile)) return false;
        Profile that = (Profile) o;
        return Objects.equals(getTestSuite().getId(), that.getTestSuite().getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTestSuite().getId());
    }

    @Override
    public String toString() {
        return testSuite.getName();
    }
}
