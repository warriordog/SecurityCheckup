package net.acomputerdog.securitycheckup.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.acomputerdog.securitycheckup.test.registry.TestRegistry;

import java.io.Reader;
import java.io.Writer;
import java.util.*;

/**
 * A collection of tests to run together
 */
public class Profile {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private final String id;
    private final String name;
    private final String description;

    /**
     * List of IDs of tests that are included in this profile
     */
    private final Set<String> testIDs;

    public Profile(String id, String name, String description, Set<String> testIDs) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.testIDs = testIDs;
    }

    public Profile(String id, String name, String description) {
        this(id, name, description, new HashSet<>());
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

        if (testIDs.contains(test.getInfo().getId())) {
            throw new IllegalStateException("Duplicate test ID");
        }

        testIDs.add(test.getInfo().getId());
    }

    /**
     * Removes a test by ID, if it is present
     * @param id ID of test to remove
     */
    public void removeTest(String id) {
        testIDs.remove(id);
    }

    public void removeTest(Test test) {
        removeTest(test.getInfo().getId());
    }

    public int getNumTests() {
        return testIDs.size();
    }

    /**
     * Gets a list of all tests to run
     *
     * @return return list containing all tests
     */
    public Set<String> getTests() {
        return Collections.unmodifiableSet(testIDs);
    }

    public List<Test> getTestsFrom(TestRegistry registry) {
        List<Test> tests = new ArrayList<>(testIDs.size());
        for (String testId : testIDs) {
            Test test = registry.getTest(testId);
            if (test == null) {
                throw new RuntimeException("No test found for id: " + testId);
            }
            tests.add(test);
        }

        return tests;
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

    @Override
    public String toString() {
        return name;
    }

    public void toJson(Writer writer) {
        gson.toJson(this, writer);
    }

    public static Profile fromJson(Reader reader) {
        return gson.fromJson(reader, Profile.class);
    }
}
