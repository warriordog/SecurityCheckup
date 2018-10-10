package net.acomputerdog.securitycheckup.main.gui.runner;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import net.acomputerdog.jwmi.JWMI;
import net.acomputerdog.jwmi.wbem.WbemLocator;
import net.acomputerdog.securitycheckup.main.gui.test.Profile;
import net.acomputerdog.securitycheckup.test.Test;
import net.acomputerdog.securitycheckup.test.TestEnvironment;
import net.acomputerdog.securitycheckup.test.TestResult;

import java.util.ArrayList;
import java.util.List;

public class TestRunner extends Task<Float> {
    private final Profile profile;
    private final List<RunTest> tests;

    public TestRunner(Profile profile) {
        this.profile = profile;
        this.tests = new ArrayList<>();

        profile.getTestSuite().getTests().forEach(test -> {
            RunTest runTest = new RunTest(test);
            tests.add(runTest);
        });
    }

    @Override
    protected Float call() throws Exception {
        try (WbemLocator locator = JWMI.getInstance().createWbemLocator()) {

            // TODO close all shared instances
            TestEnvironment environment = new TestEnvironment(locator);
            float scoreTotal = 0.0f;
            float scoreCount = 0f;

            // run each test
            for (RunTest runTest : tests) {
                Test test = runTest.getTest();
                TestResult result = test.runTest(environment);

                runTest.setResults(result);
                runTest.setStatus(result.getResultString());
                runTest.setScore(result.getScoreString());

                scoreCount++;
                scoreTotal += result.getScore();
            }

            return scoreCount == 0 ? 0.0f : (scoreTotal / scoreCount);
        }
    }

    public Profile getProfile() {
        return profile;
    }

    public List<RunTest> getTests() {
        return tests;
    }

    public static class RunTest {
        private ObjectProperty<Test> test = new SimpleObjectProperty<>(null, "test");
        private StringProperty status = new SimpleStringProperty(null, "status");
        private StringProperty score = new SimpleStringProperty(null, "score");
        private ObjectProperty<TestResult> results = new SimpleObjectProperty<>(null, "results");

        public RunTest(Test test) {
            this.test.set(test);
            this.status.set(test.getCurrentState().name());
            this.results.set(null);
        }

        public Test getTest() {
            return test.get();
        }

        public TestResult getResults() {
            return results.get();
        }

        public void setResults(TestResult results) {
            this.results.set(results);
        }

        public String getStatus() {
            return status.get();
        }

        public void setStatus(String status) {
            this.status.set(status);
        }

        public String getScore() {
            return score.get();
        }

        public void setScore(String score) {
            this.score.set(score);
        }

        public ObjectProperty<Test> testProperty() {
            return test;
        }

        public StringProperty statusProperty() {
            return status;
        }

        public ObjectProperty<TestResult> resultsProperty() {
            return results;
        }

        public StringProperty scoreProperty() {
            return score;
        }
    }
}
