package net.acomputerdog.securitycheckup.main.gui.test;

import net.acomputerdog.securitycheckup.test.Test;
import net.acomputerdog.securitycheckup.test.suite.TestSuite;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Profile {
    private final TestSuite testSuite;
    private final List<Test> tests;

    public Profile(TestSuite testSuite) {
        this.testSuite = testSuite;
        this.tests = new ArrayList<>();
        tests.addAll(testSuite.getTests());
    }

    public TestSuite getTestSuite() {
        return testSuite;
    }

    public List<Test> getTests() {
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
