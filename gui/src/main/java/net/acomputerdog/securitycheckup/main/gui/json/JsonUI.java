package net.acomputerdog.securitycheckup.main.gui.json;

import javafx.concurrent.Task;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.acomputerdog.securitycheckup.main.gui.SecurityCheckupApplication;
import net.acomputerdog.securitycheckup.main.gui.util.AlertUtils;
import net.acomputerdog.securitycheckup.test.Profile;
import net.acomputerdog.securitycheckup.test.Test;
import net.acomputerdog.securitycheckup.test.registry.Bundle;
import net.acomputerdog.securitycheckup.util.gson.JsonUtils;

import java.io.*;

public class JsonUI {
    public static void showImportBundle(SecurityCheckupApplication securityCheckupApp, Stage stage) {
        FileChooser openChooser = new FileChooser();
        openChooser.setTitle("Import bundle");
        openChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JSON files", "*.json"),
                new FileChooser.ExtensionFilter("All files", "*.*")
        );

        File selectedFile = openChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            if (selectedFile.isFile()) {
                Task<Bundle> task = new Task<Bundle>() {
                    @Override
                    protected Bundle call() throws Exception {
                        try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
                            return JsonUtils.readBundle(reader);
                        }
                    }
                };
                task.setOnCancelled(e -> AlertUtils.showWarning("Security Checkup", "Error importing bundle", task.getMessage()));
                task.setOnFailed(e -> {
                    Throwable ex = task.getException();
                    if (ex != null) {
                        System.err.println("Exception importing bundle");
                        ex.printStackTrace();
                        AlertUtils.showError("Security Checkup","Unhandled error importing bundle",String.valueOf(ex));
                    } else {
                        System.err.println("Unknown error importing bundle (no exception)");
                        AlertUtils.showError("Security Checkup","Unhandled error importing bundle", "Import failed for unknown reason (no exception)");
                    }
                });
                task.setOnSucceeded(e -> {
                    Bundle bundle = task.getValue();

                    bundle.addToRegistry(securityCheckupApp.getTestRegistry());

                    AlertUtils.showInformation("Security Checkup", "Success", "Bundle loaded successfully.");
                });

                new Thread(task).start();
            } else {
                AlertUtils.showWarning("Security Checkup", "Error importing bundle", "The selected file does not exist or is a directory.");
            }
        }
    }

    public static void showExportBundle(SecurityCheckupApplication securityCheckupApp, Stage stage, Bundle bundle) {
        FileChooser saveChooser = new FileChooser();
        saveChooser.setTitle("Export bundle");
        saveChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JSON files", "*.json"),
                new FileChooser.ExtensionFilter("All files", "*.*")
        );
        saveChooser.setInitialFileName("bundle.json");

        File saveFile = saveChooser.showSaveDialog(stage);

        if (saveFile != null) {
            Task task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {

                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile))) {
                        JsonUtils.writeBundle(bundle, writer);
                    }
                    return null;
                }
            };
            task.setOnFailed(e -> {
                Throwable ex = task.getException();
                if (ex != null) {
                    System.err.println("Exception exporting bundle");
                    ex.printStackTrace();
                    AlertUtils.showError("Security Checkup","Unhandled error exporting bundle",String.valueOf(ex));
                } else {
                    System.err.println("Unknown error exporting bundle (no exception)");
                    AlertUtils.showError("Security Checkup","Unhandled error exporting bundle", "Export failed for unknown reason (no exception)");
                }
            });
            task.setOnSucceeded(e -> AlertUtils.showInformation("Security Checkup","Success", "Bundle saved successfully."));

            new Thread(task).start();
        }
    }

    public static void showImportProfile(SecurityCheckupApplication securityCheckupApp, Stage stage) {
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
                        try (BufferedReader reader = new BufferedReader(new FileReader(profileFile))) {
                            return JsonUtils.readProfile(reader);
                        }
                    }
                };
                task.setOnCancelled(e -> AlertUtils.showWarning("Security Checkup", "Error importing profile", task.getMessage()));
                task.setOnFailed(e -> {
                    Throwable ex = task.getException();
                    if (ex != null) {
                        System.err.println("Exception importing profile");
                        ex.printStackTrace();
                        AlertUtils.showError("Security Checkup","Unhandled error importing profile",String.valueOf(ex));
                    } else {
                        System.err.println("Unknown error importing profile (no exception)");
                        AlertUtils.showError("Security Checkup","Unhandled error importing profile", "Import failed for unknown reason (no exception)");
                    }
                });
                task.setOnSucceeded(e -> {
                    Profile profile = task.getValue();

                    // check for duplicate profile
                    if (securityCheckupApp.getTestRegistry().hasProfile(profile.getId())) {
                        // Ask to overwrite
                        if (!AlertUtils.askWarning("Security Checkup", "Profile already exists", "A profile with the same ID already exists.  Do you want to overwrite it?")) {
                            return;
                        }
                    }

                    securityCheckupApp.getTestRegistry().addProfile(profile);

                    AlertUtils.showInformation("Security Checkup", "Success", "Profile loaded successfully.");
                });

                new Thread(task).start();
            } else {
                AlertUtils.showWarning("Security Checkup", "Error importing profile", "The selected file does not exist or is a directory.");
            }
        }
    }

    public static void showExportProfile(SecurityCheckupApplication securityCheckupApp, Stage stage, Profile profile) {
        FileChooser saveChooser = new FileChooser();
        saveChooser.setTitle("Export profile");
        saveChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JSON files", "*.json"),
                new FileChooser.ExtensionFilter("All files", "*.*")
        );
        saveChooser.setInitialFileName(profile.getId() + ".json");

        File saveFile = saveChooser.showSaveDialog(stage);

        if (saveFile != null) {
            Task task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile))) {
                        JsonUtils.writeProfile(profile, writer);
                    }
                    return null;
                }
            };
            task.setOnFailed(e -> {
                Throwable ex = task.getException();
                if (ex != null) {
                    System.err.println("Exception exporting profile");
                    ex.printStackTrace();
                    AlertUtils.showError("Security Checkup","Unhandled error exporting profile",String.valueOf(ex));
                } else {
                    System.err.println("Unknown error exporting profile (no exception)");
                    AlertUtils.showError("Security Checkup","Unhandled error exporting profile", "Export failed for unknown reason (no exception)");
                }
            });
            task.setOnSucceeded(e -> AlertUtils.showInformation("Security Checkup","Success", "Profile saved successfully."));

            new Thread(task).start();
        }
    }

    public static void showImportTest(SecurityCheckupApplication securityCheckupApp, Stage stage) {
        FileChooser openChooser = new FileChooser();
        openChooser.setTitle("Import test");
        openChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JSON files", "*.json"),
                new FileChooser.ExtensionFilter("All files", "*.*")
        );

        File testFile = openChooser.showOpenDialog(stage);

        if (testFile != null) {
            if (testFile.isFile()) {
                Task<Test> task = new Task<Test>() {
                    @Override
                    protected Test call() throws Exception {
                        try (BufferedReader reader = new BufferedReader(new FileReader(testFile))) {
                            return JsonUtils.readTest(reader);
                        }
                    }
                };
                task.setOnCancelled(e -> AlertUtils.showWarning("Security Checkup", "Error importing test", task.getMessage()));
                task.setOnFailed(e -> {
                    Throwable ex = task.getException();
                    if (ex != null) {
                        System.err.println("Exception importing test");
                        ex.printStackTrace();
                        AlertUtils.showError("Security Checkup","Unhandled error importing test",String.valueOf(ex));
                    } else {
                        System.err.println("Unknown error importing test (no exception)");
                        AlertUtils.showError("Security Checkup","Unhandled error importing test", "Import failed for unknown reason (no exception)");
                    }
                });
                task.setOnSucceeded(e -> {
                    Test test = task.getValue();

                    // check for duplicate test
                    if (securityCheckupApp.getTestRegistry().hasTest(test.getInfo().getId())) {
                        // Ask to overwrite
                        if (!AlertUtils.askWarning("Security Checkup", "Test already exists", "A test with the same ID already exists.  Do you want to overwrite it?")) {
                            return;
                        }
                    }

                    securityCheckupApp.getTestRegistry().addTest(test);

                    AlertUtils.showInformation("Security Checkup", "Success", "Test loaded successfully.");
                });

                new Thread(task).start();
            } else {
                AlertUtils.showWarning("Security Checkup", "Error importing test", "The selected file does not exist or is a directory.");
            }
        }
    }

    public static void showExportTest(SecurityCheckupApplication securityCheckupApp, Stage stage, Test test) {
        FileChooser saveChooser = new FileChooser();
        saveChooser.setTitle("Export test");
        saveChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JSON files", "*.json"),
                new FileChooser.ExtensionFilter("All files", "*.*")
        );
        saveChooser.setInitialFileName(test.getInfo().getId() + ".json");

        File saveFile = saveChooser.showSaveDialog(stage);

        if (saveFile != null) {
            Task task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile))) {
                        JsonUtils.writeTest(test, writer);
                    }
                    return null;
                }
            };
            task.setOnFailed(e -> {
                Throwable ex = task.getException();
                if (ex != null) {
                    System.err.println("Exception exporting test");
                    ex.printStackTrace();
                    AlertUtils.showError("Security Checkup","Unhandled error exporting test",String.valueOf(ex));
                } else {
                    System.err.println("Unknown error exporting test (no exception)");
                    AlertUtils.showError("Security Checkup","Unhandled error exporting test", "Export failed for unknown reason (no exception)");
                }
            });
            task.setOnSucceeded(e -> AlertUtils.showInformation("Security Checkup","Success", "Test saved successfully."));

            new Thread(task).start();
        }
    }

}
