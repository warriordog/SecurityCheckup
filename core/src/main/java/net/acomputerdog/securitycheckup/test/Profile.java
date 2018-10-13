package net.acomputerdog.securitycheckup.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A collection of tests to run together
 */
public class Profile {
    private final TestInfo info;

    /**
     * Mapping of test IDs to test instances
     */
    private final Map<String, Test> tests = new HashMap<>();

    public Profile(TestInfo info, Map<String, Test> tests) {
        this(info);
        tests.forEach(this.tests::put);
    }

    public Profile(TestInfo info) {
        this.info = info;
    }

    /**
     * Add a test to this suite.  Must not be a duplicate ID.
     *
     * @throws IllegalArgumentException if test is null or duplicate ID
     *
     * @param test test to add
     */
    public void addTest(Test test) {
        if (test == null) {
            throw new IllegalArgumentException("Test cannot be null");
        }

        if (tests.containsKey(test.getInfo().getID())) {
            throw new IllegalStateException("Duplicate test ID");
        }

        tests.put(test.getInfo().getID(), test);
    }

    /**
     * Removes a test by ID, if it is present
     * @param id ID of test to remove
     */
    public void removeTest(String id) {
        tests.remove(id);
    }

    public void removeTest(Test test) {
        removeTest(test.getInfo().getID());
    }

    public Test getTest(String id) {
        return tests.get(id);
    }

    public int getNumTests() {
        return tests.size();
    }

    /**
     * Gets a list of all tests to run
     *
     * @return return list containing all tests
     */
    public List<Test> getTests() {
        List<Test> testList = new ArrayList<>(tests.size());
        testList.addAll(tests.values());
        return testList;
    }

    public TestInfo getInfo() {
        return info;
    }

    @Override
    public String toString() {
        return info.toString();
    }
}
