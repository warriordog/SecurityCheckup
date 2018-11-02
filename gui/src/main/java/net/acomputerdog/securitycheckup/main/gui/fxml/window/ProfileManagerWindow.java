package net.acomputerdog.securitycheckup.main.gui.fxml.window;

import net.acomputerdog.securitycheckup.test.Profile;
import net.acomputerdog.securitycheckup.test.Test;

import java.util.Collection;

public interface ProfileManagerWindow extends Window {
    void addProfileRemoveTestListener(ProfileRemoveTestListener listener);
    void removeProfileRemoveTestListener(ProfileRemoveTestListener listener);

    void refreshProfilesList();
    void refreshTestsList();

    void showProfile(Profile profile);

    Profile getSelectedProfile();
    Collection<Profile> getSelectedProfiles();

    interface ProfileRemoveTestListener {
        void onRemoveTest(Profile profile, Test test);
    }
}
