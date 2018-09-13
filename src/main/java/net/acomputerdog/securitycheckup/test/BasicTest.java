package net.acomputerdog.securitycheckup.test;

public abstract class BasicTest implements Test {
    public static final String MESSAGE_EXCEPTION = "An unhandled exception occurred.";

    private final String id;
    private final String name;
    private final String description;

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

    protected abstract TestResult runTestSafe(TestEnvironment environment);
}
