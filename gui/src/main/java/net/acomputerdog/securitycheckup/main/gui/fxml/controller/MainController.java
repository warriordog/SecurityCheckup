package net.acomputerdog.securitycheckup.main.gui.fxml.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import net.acomputerdog.securitycheckup.main.gui.SecurityCheckupApplication;
import net.acomputerdog.securitycheckup.main.gui.fxml.panel.ProfileInfoPanel;
import net.acomputerdog.securitycheckup.main.gui.fxml.window.MainWindow;
import net.acomputerdog.securitycheckup.test.Profile;

public class MainController implements MainWindow {
    @FXML
    private Stage stage;
    @FXML
    private ListView<Profile> profilesList;
    @FXML
    private ProfileInfoPanel profileInfoController;

    private SecurityCheckupApplication securityCheckupApp;

    @FXML
    private void initialize() {
        profilesList.getSelectionModel().selectedItemProperty().addListener(e ->
                profileInfoController.setProfile(
                        securityCheckupApp.getTestRegistry(),
                        profilesList.getSelectionModel().getSelectedItem()));
        stage.setOnCloseRequest(e -> Platform.exit());
    }

    public void initData(SecurityCheckupApplication securityCheckupApplication) {
        this.securityCheckupApp = securityCheckupApplication;
    }

    @FXML
    private void onExit(ActionEvent actionEvent) {
        Platform.exit();
    }

    @FXML
    private void onManageProfiles(ActionEvent actionEvent) {
        securityCheckupApp.getProfileManagerWindow().getStage().show();
    }

    @FXML
    private void onAbout(ActionEvent actionEvent) {
        securityCheckupApp.getAboutWindow().getStage().show();
    }

    @Override
    public void refreshProfiles() {
        profileInfoController.clear();
        profilesList.getItems().clear();
        profilesList.getItems().addAll(securityCheckupApp.getTestRegistry().getProfiles());
    }

    @Override
    public void addRunButtonListener(ProfileInfoPanel.RunListener listener) {
        this.profileInfoController.addRunButtonListener(listener);
    }

    @Override
    public Stage getStage() {
        return stage;
    }
}
