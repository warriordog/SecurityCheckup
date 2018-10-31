package net.acomputerdog.securitycheckup.main.gui.fxml.window;

import net.acomputerdog.securitycheckup.main.gui.fxml.panel.ProfileInfoPanel;

public interface MainWindow extends Window {
    void refreshProfiles();

    void addRunButtonListener(ProfileInfoPanel.RunListener listener);

}
