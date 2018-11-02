package net.acomputerdog.securitycheckup.main.gui.fxml.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;
import net.acomputerdog.securitycheckup.main.gui.SecurityCheckupApplication;
import net.acomputerdog.securitycheckup.main.gui.fxml.window.ProfileManagerWindow;
import net.acomputerdog.securitycheckup.main.gui.json.JsonUI;
import net.acomputerdog.securitycheckup.test.Profile;
import net.acomputerdog.securitycheckup.test.Test;
import net.acomputerdog.securitycheckup.test.registry.Bundle;

import java.util.Collection;
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

    private Set<ProfileRemoveTestListener> profileRemoveTestListeners;

    @FXML
    private void initialize() {
        profileRemoveTestListeners = new HashSet<>();

        profilesList.getSelectionModel().selectedItemProperty().addListener(e -> showProfile(getSelectedProfile()));
        profilesList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
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

            // redraw profile
            refreshProfilesList();
            showProfile(null);
        }
    }

    @FXML
    private void onImportProfile(ActionEvent actionEvent) {
        JsonUI.showImportProfile(securityCheckupApp, getStage());
    }

    @FXML
    private void onExportProfile(ActionEvent actionEvent) {
        Profile profile = getSelectedProfile();

        // null if nothing selected
        if (profile != null) {
            JsonUI.showExportProfile(securityCheckupApp, getStage(), profile);
        }
    }

    @FXML
    public void onExportBundle(ActionEvent actionEvent) {
        Collection<Profile> selectedProfiles = getSelectedProfiles();

        // null if nothing selected
        if (!selectedProfiles.isEmpty()) {

            Set<Profile> profiles = new HashSet<>(selectedProfiles);
            Set<Test> tests = new HashSet<>();
            profiles.forEach(profile -> tests.addAll(profile.getTestsFrom(securityCheckupApp.getTestRegistry())));

            Bundle bundle = new Bundle(tests, profiles);

            JsonUI.showExportBundle(securityCheckupApp, this.getStage(), bundle);
        }
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
            profileRemoveTestListeners.forEach(l -> l.onRemoveTest(profile, test));

            // redraw profile
            showProfile(profile);
        }
    }

    @Override
    public Profile getSelectedProfile() {
        return profilesList.getSelectionModel().getSelectedItem();
    }

    @Override
    public Collection<Profile> getSelectedProfiles() {
        return profilesList.getSelectionModel().getSelectedItems();
    }

    @Override
    public void addProfileRemoveTestListener(ProfileRemoveTestListener listener) {
        profileRemoveTestListeners.add(listener);
    }

    @Override
    public void removeProfileRemoveTestListener(ProfileRemoveTestListener listener) {
        profileRemoveTestListeners.remove(listener);
    }

    @Override
    public void refreshProfilesList() {
        profilesList.getItems().clear();
        profilesList.getItems().addAll(securityCheckupApp.getTestRegistry().getProfiles());
    }

    @Override
    public void refreshTestsList() {
        showProfile(getSelectedProfile());
    }

    @Override
    public void showProfile(Profile profile) {
        testsList.getItems().clear();

        if (profile != null) {
            testsList.getItems().addAll(profile.getTestsFrom(securityCheckupApp.getTestRegistry()));
        }
    }

    @Override
    public Stage getStage() {
        return stage;
    }

}
