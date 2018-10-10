package net.acomputerdog.securitycheckup.test.suite;

import net.acomputerdog.securitycheckup.test.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A collection of tests to run together
 */
public class Profile {
    /**
     * String ID of this suite, does not have to be human readable
     */
    private final String id;

    /**
     * Human readable name of this suite
     */
    private final String name;

    /**
     * Description of this suite
     */
    private final String description;

    /**
     * Mapping of test IDs to test instances
     */
    private final Map<String, Test> tests;

    public Profile(String id, String name, String description, Map<String, Test> tests) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.tests = tests;
    }

    public Profile(String id, String name, String description) {
        this(id, name, description, new HashMap<>());
    }

    public Profile(String id) {
        this(id, id, "");
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

        if (tests.containsKey(test.getID())) {
            throw new IllegalStateException("Duplicate test ID");
        }

        tests.put(test.getID(), test);
    }

    /**
     * Removes a test by ID, if it is present
     * @param id ID of test to remove
     */
    public void removeTest(String id) {
        tests.remove(id);
    }

    public void removeTest(Test test) {
        removeTest(test.getID());
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

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String toString() {
        return this.name;
    }
}
