package net.acomputerdog.securitycheckup.main.gui.fxml.window;

import net.acomputerdog.securitycheckup.test.Profile;
import net.acomputerdog.securitycheckup.test.Test;

public interface ProfileManagerWindow extends Window {
    void addTestRemoveListener(TestRemoveListener listener);
    void removeTestRemoveListener(TestRemoveListener listener);

    void addProfileRemoveListener(ProfileRemoveListener listener);
    void removeProfileRemoveListener(ProfileRemoveListener listener);

    void refreshProfilesList();

    void showProfile(Profile profile);

    interface TestRemoveListener {
        void onRemoveTest(Profile profile, Test test);
    }

    interface TestAddListener {
        void onAddTest(Profile profile, Test test);
    }

    interface ProfileRemoveListener {
        void onRemoveProfile(Profile profile);
    }

    interface ProfileAddListener {
        void onAddProfile(Profile profile);
    }
}
