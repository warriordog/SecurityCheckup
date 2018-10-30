package net.acomputerdog.securitycheckup.main.gui.fxml.window;

import net.acomputerdog.securitycheckup.test.Profile;
import net.acomputerdog.securitycheckup.test.Test;

public interface AddTestWindow extends Window {
    void show(Profile profile);
    void setProfile(Profile profile);

    void addAddTestListener(AddTestListener listener);
    void removeAddTestListener(AddTestListener listener);

    interface AddTestListener {
        void onAddTest(Profile profile, Test test);
    }
}
