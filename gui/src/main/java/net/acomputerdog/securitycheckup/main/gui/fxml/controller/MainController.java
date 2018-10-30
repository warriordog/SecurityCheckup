package net.acomputerdog.securitycheckup.main.gui.fxml.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
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
    private StackPane profileInfo;
    @FXML
    private ProfileInfoPanel profileInfoController;

    private SecurityCheckupApplication securityCheckupApp;

    @FXML
    public void initialize() {
        profilesList.getSelectionModel().selectedItemProperty().addListener(e -> profileInfoController.setProfile(profilesList.getSelectionModel().getSelectedItem()));
        stage.setOnCloseRequest(e -> Platform.exit());
    }

    public void initData(SecurityCheckupApplication securityCheckupApplication) {
        this.securityCheckupApp = securityCheckupApplication;
    }

    @FXML
    public void onExit(ActionEvent actionEvent) {
        Platform.exit();
    }

    @FXML
    public void onManageProfiles(ActionEvent actionEvent) {
        securityCheckupApp.getProfileManagerWindow().getStage().show();
    }

    @FXML
    public void onAbout(ActionEvent actionEvent) {
        securityCheckupApp.getAboutWindow().getStage().show();
    }

    @Override
    public void refreshProfiles() {
        profileInfoController.setProfile(null);
        profilesList.getItems().clear();
        profilesList.getItems().addAll(securityCheckupApp.getTestRegistry().getProfiles());
    }

    @Override
    public SecurityCheckupApplication getSecurityCheckupApp() {
        return securityCheckupApp;
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
