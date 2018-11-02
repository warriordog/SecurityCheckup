package net.acomputerdog.securitycheckup.main.gui.fxml.window;

import net.acomputerdog.securitycheckup.test.Profile;
import net.acomputerdog.securitycheckup.test.Test;

public interface AddTestWindow extends Window {
    void show(Profile profile);
    void setProfile(Profile profile);

    void addAddTestListener(ProfileAddTestListener listener);
    void removeAddTestListener(ProfileAddTestListener listener);

    interface ProfileAddTestListener {
        void onAddTest(Profile profile, Test test);
    }
}
