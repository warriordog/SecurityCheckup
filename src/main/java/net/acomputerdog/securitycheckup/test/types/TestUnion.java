package net.acomputerdog.securitycheckup.test.types;

import net.acomputerdog.securitycheckup.test.BasicTest;
import net.acomputerdog.securitycheckup.test.Test;
import net.acomputerdog.securitycheckup.test.TestEnvironment;
import net.acomputerdog.securitycheckup.test.TestResult;

import java.util.ArrayList;
import java.util.List;

import static net.acomputerdog.securitycheckup.test.TestResult.SCORE_FAIL;

public class TestUnion extends BasicTest {
    private final List<Test> subtests = new ArrayList<>();

    public TestUnion(String id, String name, String description) {
        super(id, name, description);
    }

    public TestUnion(String id, String name) {
        super(id, name);
    }

    public TestUnion(String id) {
        super(id);
    }

    public TestUnion addTest(Test test) {
        subtests.add(test);
        return this;
    }

    @Override
    protected TestResult runTestSafe(TestEnvironment environment) {
        if (subtests.isEmpty()) {
            this.setState(State.NOT_APPLICABLE);
            return new TestResult(this, TestResult.SCORE_PASS).setMessage("No subtests were run.");
        }

        StringBuilder message = new StringBuilder();
        float totalScore = 0.0f;
        for (Test test : subtests) {
            try {
                TestResult result = test.runTest(environment);

                totalScore += result.getScore();

                message.append("\n");
                message.append(test.getID());
                message.append(": ");
                message.append(result.getState().name());
                if (result.getMessage() != null) {
                    message.append(" - ");
                    message.append(result.getMessage());
                }
                if (result.getException() != null) {
                    message.append("(");
                    message.append(result.getException().toString());
                    message.append(")");
                }
            } catch (Throwable t) {
                totalScore += SCORE_FAIL;
                //results.add(new TestResult(test, State.ERROR, TestResult.SCORE_FAIL).setException(t).setMessage("Unhandled exception in subtest"));
            }
        }
        float finalScore = totalScore / (float)subtests.size();

        this.setState(State.FINISHED);
        return new TestResult(this, finalScore).setMessage(message.toString());
    }
}
