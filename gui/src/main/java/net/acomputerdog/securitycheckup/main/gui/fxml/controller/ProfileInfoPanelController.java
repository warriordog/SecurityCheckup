package net.acomputerdog.securitycheckup.main.gui.fxml.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import net.acomputerdog.securitycheckup.main.gui.fxml.panel.ProfileInfoPanel;
import net.acomputerdog.securitycheckup.main.gui.fxml.panel.RunInfoPanel;
import net.acomputerdog.securitycheckup.main.gui.fxml.panel.TestInfoPanel;
import net.acomputerdog.securitycheckup.test.Profile;
import net.acomputerdog.securitycheckup.test.Test;

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
    private BorderPane runInfo;
    @FXML
    private RunInfoPanel runInfoController;
    @FXML
    private ListView<Test> testList;
    @FXML
    private VBox testInfo;
    @FXML
    private TestInfoPanel testInfoController;
    @FXML
    private Label profileIdText;
    @FXML
    private Label profileDescText;
    @FXML
    private Button runButton;

    private Profile profile;

    @FXML
    private void initialize() {
        testList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> this.onSelectTest(newValue));
    }

    private void onSelectTest(Test test) {
        testInfoController.showTest(test);
    }

    @Override
    public Profile getProfile() {
        return profile;
    }

    @Override
    public void setProfile(Profile profile) {
        this.profile = profile;

        this.testList.getItems().clear();
        testInfoController.showTest(null);
        runInfoController.clear();

        if (profile != null) {
            this.descriptionText.setText(profile.getDescription());

            // Set test list tab
            this.testList.getItems().addAll(profile.getTests());

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
    public void setRunButtonEnabled(boolean enabled) {
        this.runButton.setDisable(enabled);
    }

    @Override
    public Node getRoot() {
        return root;
    }
}
