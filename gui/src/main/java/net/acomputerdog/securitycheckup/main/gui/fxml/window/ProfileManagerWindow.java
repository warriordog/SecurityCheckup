package net.acomputerdog.securitycheckup.main.gui.fxml.window;

import net.acomputerdog.securitycheckup.test.Profile;
import net.acomputerdog.securitycheckup.test.Test;

public interface ProfileManagerWindow extends Window {
    void addTestRemoveListener(ProfileRemoveTestListener listener);
    void removeTestRemoveListener(ProfileRemoveTestListener listener);

    void addProfileRemoveListener(ProfileRemoveListener listener);
    void removeProfileRemoveListener(ProfileRemoveListener listener);

    void refreshProfilesList();
    void refreshTestsList();

    void showProfile(Profile profile);

    interface ProfileRemoveTestListener {
        void onRemoveTest(Profile profile, Test test);
    }

    interface ProfileAddTestListener {
        void onAddTest(Profile profile, Test test);
    }

    interface ProfileRemoveListener {
        void onRemoveProfile(Profile profile);
    }

    interface ProfileAddListener {
        void onAddProfile(Profile profile);
    }
}
