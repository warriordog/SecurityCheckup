package net.acomputerdog.securitycheckup.main.gui.fxml.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.acomputerdog.securitycheckup.main.gui.SecurityCheckupApplication;
import net.acomputerdog.securitycheckup.main.gui.fxml.window.NewProfileWindow;
import net.acomputerdog.securitycheckup.main.gui.util.AlertUtils;
import net.acomputerdog.securitycheckup.test.Profile;

public class NewProfileController implements NewProfileWindow {
    @FXML
    private Stage stage;
    @FXML
    private TextField idText;
    @FXML
    private TextField nameText;
    @FXML
    private TextField descriptionText;

    private SecurityCheckupApplication securityCheckupApp;

    @FXML
    private void initialize() {
    }

    public void initData(SecurityCheckupApplication securityCheckupApp) {
        this.securityCheckupApp = securityCheckupApp;
    }

    @FXML
    private void onCreateProfile(ActionEvent actionEvent) {
        String id = idText.getText().trim();
        if (!id.isEmpty()) {

            // default to id if missing
            String name = nameText.getText().trim();
            if (name.isEmpty()) {
                name = id;
            }

            // can be empty
            String desc = descriptionText.getText().trim();

            Profile profile = new Profile(id, name, desc);

            securityCheckupApp.getTestRegistry().addProfile(profile);

            stage.hide();
        } else {
            AlertUtils.showWarning("Security Checkup", "Unable to create profile", "Profile id cannot be blank");
        }
    }

    @FXML
    private void onCancel(ActionEvent actionEvent) {
        stage.hide();
    }

    @Override
    public Stage getStage() {
        return stage;
    }
}
