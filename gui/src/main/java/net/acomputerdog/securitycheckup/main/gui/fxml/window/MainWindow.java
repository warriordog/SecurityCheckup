package net.acomputerdog.securitycheckup.main.gui.fxml.window;

import net.acomputerdog.securitycheckup.main.gui.SecurityCheckupApplication;
import net.acomputerdog.securitycheckup.main.gui.fxml.panel.ProfileInfoPanel;

public interface MainWindow extends Window {
    void refreshProfiles();

    SecurityCheckupApplication getSecurityCheckupApp();

    void addRunButtonListener(ProfileInfoPanel.RunListener listener);

}
