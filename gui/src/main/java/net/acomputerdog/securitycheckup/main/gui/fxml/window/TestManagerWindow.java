package net.acomputerdog.securitycheckup.main.gui.fxml.window;

import net.acomputerdog.securitycheckup.test.Test;

public interface TestManagerWindow extends Window {

    void refreshTestList();

    void addTestRemoveListener(TestRemoveListener listener);
    void removeTestRemoveListener(TestRemoveListener listener);

    interface TestRemoveListener {
        void onRemoveTest(Test test);
    }
}
