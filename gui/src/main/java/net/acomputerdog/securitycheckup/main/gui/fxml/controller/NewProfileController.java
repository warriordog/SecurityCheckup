package net.acomputerdog.securitycheckup.main.gui.fxml.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.acomputerdog.securitycheckup.main.gui.SecurityCheckupApplication;
import net.acomputerdog.securitycheckup.main.gui.fxml.window.NewProfileWindow;
import net.acomputerdog.securitycheckup.test.Profile;

import java.util.HashSet;
import java.util.Set;

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

    private Set<CreateProfileListener> createProfileListeners;

    @FXML
    private void initialize() {
        createProfileListeners = new HashSet<>();
    }

    public void initData(SecurityCheckupApplication securityCheckupApp) {
        this.securityCheckupApp = securityCheckupApp;
    }

    @FXML
    private void onCreateProfile(ActionEvent actionEvent) {
        Profile profile = new Profile(idText.getText(), nameText.getText(), descriptionText.getText());

        securityCheckupApp.getTestRegistry().addProfile(profile);

        createProfileListeners.forEach(l -> l.onCreateProfile(profile));

        stage.hide();
    }

    @FXML
    private void onCancel(ActionEvent actionEvent) {
        stage.hide();
    }

    @Override
    public Stage getStage() {
        return stage;
    }

    @Override
    public void addCreateProfileListener(CreateProfileListener listener) {
        createProfileListeners.add(listener);
    }

    @Override
    public void removeCreateProfileListener(CreateProfileListener listener) {
        createProfileListeners.remove(listener);
    }
}
