package net.acomputerdog.securitycheckup.main.gui.fxml.panel;

import net.acomputerdog.securitycheckup.test.TestResult;

public interface TestResultsPanel extends Panel {
    void showResults(TestResult result);
}
