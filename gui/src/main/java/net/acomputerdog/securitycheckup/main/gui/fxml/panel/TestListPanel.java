package net.acomputerdog.securitycheckup.main.gui.fxml.panel;

import net.acomputerdog.securitycheckup.test.Test;

import java.util.Collection;

public interface TestListPanel extends Panel {
    void showTests(Collection<Test> tests);
    void clear();

    Test getSelectedTest();

    void showTestInfo(Test value);
    void clearTestInfo();
}
