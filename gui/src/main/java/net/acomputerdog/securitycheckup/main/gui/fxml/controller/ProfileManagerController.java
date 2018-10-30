package net.acomputerdog.securitycheckup.main.gui.fxml.controller;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.acomputerdog.securitycheckup.main.gui.SecurityCheckupApplication;
import net.acomputerdog.securitycheckup.main.gui.fxml.window.ProfileManagerWindow;
import net.acomputerdog.securitycheckup.main.gui.util.AlertUtils;
import net.acomputerdog.securitycheckup.test.Profile;
import net.acomputerdog.securitycheckup.test.Test;

import java.io.File;
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
            FileChooser openChooser = new FileChooser();
            openChooser.setTitle("Import profile");
            openChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("JSON files", "*.json"),
                    new FileChooser.ExtensionFilter("All files", "*.*")
            );

            File profileFile = openChooser.showOpenDialog(stage);

            if (profileFile != null) {
                if (profileFile.isFile()) {
                    Task<Profile> task = new Task<Profile>() {
                        @Override
                        protected Profile call() throws Exception {
                            return securityCheckupApp.loadProfile(profileFile);
                        }
                    };
                    task.setOnCancelled(e -> AlertUtils.showWarning("Security Checkup", "Error importing profile", task.getMessage()));
                    task.setOnFailed(e -> AlertUtils.showError("Security Checkup", "Unhandled error importing profile", String.valueOf(task.getException())));
                    task.setOnSucceeded(e -> {
                        Profile profile = task.getValue();

                        securityCheckupApp.getTestRegistry().addProfile(profile);

                        // TODO selective refresh
                        this.refreshProfilesList();
                        securityCheckupApp.getMainWindow().refreshProfiles();

                        showProfile(task.getValue());
                        AlertUtils.showInformation("Security Checkup", "Success", "Profile loaded successfully.");
                    });

                    new Thread(task).start();
                } else {
                    AlertUtils.showWarning("Security Checkup", "Error importing profile", "The selected file does not exist or is a directory.");
                }
            }
    }

    @FXML
    private void onExportProfile(ActionEvent actionEvent) {
        Profile profile = getSelectedProfile();

        // null if nothing selected
        if (profile != null) {

            FileChooser saveChooser = new FileChooser();
            saveChooser.setTitle("Export profile");
            saveChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("JSON files", "*.json"),
                    new FileChooser.ExtensionFilter("All files", "*.*")
            );

            File saveFile = saveChooser.showSaveDialog(stage);

            if (saveFile != null) {
                Task task = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        securityCheckupApp.saveProfile(profile,saveFile);
                        return null;
                    }
                };
                task.setOnFailed(e -> AlertUtils.showError("Security Checkup","Unhandled error exporting profile",String.valueOf(task.getException())));
                task.setOnSucceeded(e -> AlertUtils.showInformation("Security Checkup","Success", "Profile saved successfully."));

                new Thread(task).start();
            }
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
            testsList.getItems().addAll(profile.getTestsFrom(securityCheckupApp.getTestRegistry()));
        }
    }

    @Override
    public Stage getStage() {
        return stage;
    }
}
