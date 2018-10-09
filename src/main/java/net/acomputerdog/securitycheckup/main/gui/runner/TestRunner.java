package net.acomputerdog.securitycheckup.main.gui.runner;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import net.acomputerdog.securitycheckup.main.gui.test.Profile;
import net.acomputerdog.securitycheckup.test.Test;

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
        // run each test
        for (RunTest runTest : tests) {
            Test test = runTest.getTest();
            // TODO test
            runTest.setStatus("SKIPPED");
        }

        return 0.5f;
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

        public RunTest(Test test) {
            this.test.set(test);
            this.status.set("WAITING");
        }

        public Test getTest() {
            return test.get();
        }

        public String getStatus() {
            return status.get();
        }

        public void setStatus(String status) {
            this.status.set(status);
        }

        public ObjectProperty<Test> testProperty() {
            return test;
        }

        public StringProperty statusProperty() {
            return status;
        }
    }
}
