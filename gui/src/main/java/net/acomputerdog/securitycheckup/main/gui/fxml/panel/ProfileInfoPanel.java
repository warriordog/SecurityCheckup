package net.acomputerdog.securitycheckup.main.gui.fxml.panel;

import net.acomputerdog.securitycheckup.test.Profile;
import net.acomputerdog.securitycheckup.test.registry.TestRegistry;

public interface ProfileInfoPanel extends Panel {

    Profile getProfile();
    void setProfile(TestRegistry testRegistry, Profile profile);

    void setRunButtonEnabled(boolean enabled);

    void addRunButtonListener(RunListener listener);

    void clear();

    interface RunListener {
        void onRunClicked(ProfileInfoPanel profileInfo, RunInfoPanel runInfo);
    }
}
