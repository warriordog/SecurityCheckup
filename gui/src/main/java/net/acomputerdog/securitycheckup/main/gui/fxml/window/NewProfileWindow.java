package net.acomputerdog.securitycheckup.main.gui.fxml.window;

import net.acomputerdog.securitycheckup.test.Profile;

public interface NewProfileWindow extends Window {
    void addCreateProfileListener(CreateProfileListener listener);
    void removeCreateProfileListener(CreateProfileListener listener);

    interface CreateProfileListener {
        void onCreateProfile(Profile profile);
    }
}
