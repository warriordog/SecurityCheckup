package net.acomputerdog.securitycheckup.test;

import net.acomputerdog.securitycheckup.test.step.Step;

import java.util.Objects;

public class Test {
    private final TestInfo info;
    private final Step<TestResult> rootStep;

    // constructor for deserializing
    private Test() {
        this (null, null);
    }

    public Test(TestInfo info, Step<TestResult> rootStep) {
        this.info = info;
        this.rootStep = rootStep;
    }

    public TestInfo getInfo() {
        return info;
    }

    public Step<TestResult> getRootStep() {
        return rootStep;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Test)) return false;
        Test test = (Test) o;
        return Objects.equals(info.getID(), test.info.getID());
    }

    @Override
    public int hashCode() {
        return info.getID().hashCode();
    }

    @Override
    public String toString() {
        return info.toString();
    }
}
