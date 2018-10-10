package net.acomputerdog.securitycheckup.test;

/**
 * Interface for classes that implement a particular type of test.
 *
 * A "test" is a check for a particular setting or condition on the running system.
 */
public interface Test {
    /**
     * Runs this test.
     *
     * @return return the results of the test
     */
    TestResult runTest(TestEnvironment environment);

    /**
     * Gets the ID of this test.  Does not have to be human readable.
     *
     * @return string ID of this test
     */
    String getID();

    /**
     * Gets the human-readable name of this test.
     *
     * @return string name of this test
     */
    String getName();

    /**
     * Gets the human-readable description of this test
     * @return string description of this test
     */
    String getDescription();

    /**
     * Gets the current state of this test
     * @return return the current state of this test
     */
    Test.State getCurrentState();

    /**
     * The current state of a test.
     */
    enum State {
        /**
         * The test was not run
         */
        NOT_RUN,

        /**
         * Test is currently running.
         * If the test should have already finished, then it froze or timed out.
         */
        RUNNING,

        /**
         * The test was called but did not apply to the current system.
         * Should be treated as a success.
         */
        NOT_APPLICABLE,

        /**
         * The test was started but was incompatible with some system aspect and could not finish.
         */
        INCOMPATIBLE,

        /**
         * Test started but did not finish due to an error.
         */
        ERROR,

        /**
         * Test ran and finished successfully.
         * The test will be in this state whether the system passed or failed.
         */
        FINISHED
    }
}
