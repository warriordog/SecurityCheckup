package net.acomputerdog.securitycheckup.main.gui.fxml.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import net.acomputerdog.securitycheckup.main.gui.SecurityCheckupApplication;
import net.acomputerdog.securitycheckup.main.gui.fxml.window.ProfileManagerWindow;
import net.acomputerdog.securitycheckup.test.Profile;
import net.acomputerdog.securitycheckup.test.Test;

import java.util.HashSet;
import java.util.Set;

public class ProfileManagerController implements ProfileManagerWindow {
    @FXML
    private ListView<Profile> profilesList;
    @FXML
    private ListView<Test> testsList;
    @FXML
    private Stage stage;

    private SecurityCheckupApplication securityCheckupApp;

    private Set<ProfileRemoveListener> profileRemoveListeners;
    private Set<TestRemoveListener> testRemoveListeners;

    @FXML
    private void initialize() {
        profileRemoveListeners = new HashSet<>();
        testRemoveListeners = new HashSet<>();

        profilesList.getSelectionModel().selectedItemProperty().addListener(e -> showProfile(getSelectedProfile()));
    }

    public void initData(SecurityCheckupApplication securityCheckupApp) {
        this.securityCheckupApp = securityCheckupApp;

        refreshProfilesList();
    }

    @FXML
    private void onAddProfile(ActionEvent actionEvent) {
        securityCheckupApp.getNewProfileWindow().getStage().show();
    }

    @FXML
    private void onRemoveProfile(ActionEvent actionEvent) {
        Profile profile = getSelectedProfile();

        // null if nothing selected
        if (profile != null) {
            securityCheckupApp.getTestRegistry().removeProfile(profile);

            // Trigger remove listeners
            profileRemoveListeners.forEach(l -> l.onRemoveProfile(profile));

            // redraw profile
            refreshProfilesList();
            showProfile(null);
        }
    }

    @FXML
    private void onImportProfile(ActionEvent actionEvent) {

    }

    @FXML
    private void onExportProfile(ActionEvent actionEvent) {

    }

    @FXML
    private void onAddTest(ActionEvent actionEvent) {
        Profile profile = getSelectedProfile();

        if (profile != null) {
            securityCheckupApp.getAddTestWindow().show(profile);
        }
    }

    @FXML
    private void onRemoveTest(ActionEvent actionEvent) {
        Profile profile = getSelectedProfile();
        Test test = testsList.getSelectionModel().getSelectedItem();

        // null if nothing selected
        if (profile != null && test != null) {
            profile.removeTest(test);

            // Trigger remove listeners
            testRemoveListeners.forEach(l -> l.onRemoveTest(profile, test));

            // redraw profile
            showProfile(profile);
        }
    }

    private Profile getSelectedProfile() {
        return profilesList.getSelectionModel().getSelectedItem();
    }

    @Override
    public void addTestRemoveListener(TestRemoveListener listener) {
        testRemoveListeners.add(listener);
    }

    @Override
    public void removeTestRemoveListener(TestRemoveListener listener) {
        testRemoveListeners.remove(listener);
    }

    @Override
    public void addProfileRemoveListener(ProfileRemoveListener listener) {
        profileRemoveListeners.add(listener);
    }

    @Override
    public void removeProfileRemoveListener(ProfileRemoveListener listener) {
        profileRemoveListeners.remove(listener);
    }

    @Override
    public void refreshProfilesList() {
        profilesList.getItems().clear();
        profilesList.getItems().addAll(securityCheckupApp.getTestRegistry().getProfiles());
    }

    @Override
    public void showProfile(Profile profile) {
        testsList.getItems().clear();

        if (profile != null) {
            testsList.getItems().addAll(profile.getTests());
        }
    }

    @Override
    public Stage getStage() {
        return stage;
    }
}
