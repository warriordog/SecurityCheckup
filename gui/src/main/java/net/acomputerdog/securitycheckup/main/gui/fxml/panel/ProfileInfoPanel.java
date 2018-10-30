package net.acomputerdog.securitycheckup.main.gui.fxml.panel;

import net.acomputerdog.securitycheckup.test.Profile;

public interface ProfileInfoPanel extends Panel {

    Profile getProfile();

    void setProfile(Profile profile);

    void addRunButtonListener(RunListener listener);

    void setRunButtonEnabled(boolean enabled);

    interface RunListener {
        void onRunClicked(ProfileInfoPanel profileInfo, RunInfoPanel runInfo);
    }
}
