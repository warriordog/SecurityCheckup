package net.acomputerdog.securitycheckup.main.gui.fxml.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import net.acomputerdog.securitycheckup.main.gui.fxml.panel.ProfileInfoPanel;
import net.acomputerdog.securitycheckup.main.gui.fxml.panel.RunInfoPanel;
import net.acomputerdog.securitycheckup.main.gui.fxml.panel.TestListPanel;
import net.acomputerdog.securitycheckup.test.Profile;
import net.acomputerdog.securitycheckup.test.registry.TestRegistry;

public class ProfileInfoPanelController implements ProfileInfoPanel {
    @FXML
    public TabPane tabs;
    @FXML
    private StackPane root;
    @FXML
    private Text selectProfileMessage;
    @FXML
    private Label descriptionText;
    @FXML
    private RunInfoPanel runInfoController;
    @FXML
    private TestListPanel testListController;
    @FXML
    private Label profileIdText;
    @FXML
    private Label profileDescText;
    @FXML
    private Button runButton;

    private Profile profile;

    @Override
    public Profile getProfile() {
        return profile;
    }

    @Override
    public void setProfile(TestRegistry testRegistry, Profile profile) {
        clear();

        if (profile != null) {
            this.profile = profile;

            this.descriptionText.setText(profile.getDescription());

            // Set test list tab
            testListController.showTests(profile.getTestsFrom(testRegistry));

            // set details tab
            profileIdText.setText(profile.getId());
            profileDescText.setText(profile.getDescription());

            tabs.setVisible(true);
            selectProfileMessage.setVisible(false);
        } else {
            tabs.setVisible(false);
            selectProfileMessage.setVisible(true);
        }
    }

    @Override
    public void addRunButtonListener(RunListener listener) {
        this.runButton.addEventHandler(ActionEvent.ACTION, e -> listener.onRunClicked(this, this.runInfoController));
    }

    @Override
    public void clear() {
        this.profile = null;
        testListController.clear();
        runInfoController.clear();
    }

    @Override
    public void setRunButtonEnabled(boolean enabled) {
        this.runButton.setDisable(enabled);
    }

    @Override
    public Node getRoot() {
        return root;
    }
}
