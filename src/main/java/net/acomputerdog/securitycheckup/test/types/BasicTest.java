package net.acomputerdog.securitycheckup.test.types;

import net.acomputerdog.securitycheckup.test.Test;
import net.acomputerdog.securitycheckup.test.TestEnvironment;
import net.acomputerdog.securitycheckup.test.TestResult;

import java.util.Objects;

/**
 * Parent class for normal tests
 */
public abstract class BasicTest implements Test {
    public static final String MESSAGE_EXCEPTION = "An unhandled exception occurred.";

    /**
     * String identifier for this test.  Does not have to be human readable.
     */
    private final String id;

    /**
     * String name of this test.  Should be human readable.
     */
    private final String name;

    /**
     * String description of this test.  Should be human readable.
     */
    private final String description;

    /**
     * Current state of this test
     */
    private Test.State state = State.NOT_RUN;

    public BasicTest(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public BasicTest(String id, String name) {
        this(id, name, "");
    }

    public BasicTest(String id) {
        this(id, id);
    }

    @Override
    public TestResult runTest(TestEnvironment environment) {
        try {
            state = State.RUNNING;

            return runTestSafe(environment);
        } catch (Throwable t) {
            state = State.ERROR;
            return new TestResult(this, TestResult.SCORE_FAIL).setException(t).setMessage(MESSAGE_EXCEPTION);
        }
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public State getCurrentState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BasicTest)) return false;
        BasicTest basicTest = (BasicTest) o;
        return Objects.equals(getID(), basicTest.getID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getID());
    }

    /**
     * Implemented by subclasses to run the actual test.
     *
     * Any exceptions thrown by this method will be caught and stored in the test results,
     * along with an ERROR result.
     *
     * @param environment The test environment
     * @return return test results
     */
    protected abstract TestResult runTestSafe(TestEnvironment environment);
}
