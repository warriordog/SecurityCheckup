package net.acomputerdog.securitycheckup.main.gui.fxml.controller;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.acomputerdog.securitycheckup.main.gui.SecurityCheckupApplication;
import net.acomputerdog.securitycheckup.main.gui.fxml.panel.TestListPanel;
import net.acomputerdog.securitycheckup.main.gui.fxml.window.TestManagerWindow;
import net.acomputerdog.securitycheckup.main.gui.util.AlertUtils;
import net.acomputerdog.securitycheckup.test.Test;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class TestManagerController implements TestManagerWindow {
    @FXML
    private Stage stage;
    @FXML
    private TestListPanel testListController;

    private Set<TestRemoveListener> testRemoveListeners;

    private SecurityCheckupApplication securityCheckupApp;

    @FXML
    private void initialize() {
        testRemoveListeners = new HashSet<>();
    }

    public void initData(SecurityCheckupApplication securityCheckupApplication) {
        this.securityCheckupApp = securityCheckupApplication;

        refreshTestList();
    }

    @FXML
    private void onRemoveTest(ActionEvent actionEvent) {
        Test test = testListController.getSelectedTest();

        // null if nothing selected
        if (test != null) {
            securityCheckupApp.getTestRegistry().removeTest(test);

            // Trigger remove listeners
            testRemoveListeners.forEach(l -> l.onRemoveTest(test));

            // redraw profile
            refreshTestList();
            testListController.clearTestInfo();
        }
    }

    @FXML
    private void onImportTest(ActionEvent actionEvent) {
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
                            return securityCheckupApp.getGson().fromJson(reader, Test.class);
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

                    this.refreshTestList();

                    testListController.showTestInfo(task.getValue());
                    AlertUtils.showInformation("Security Checkup", "Success", "Test loaded successfully.");
                });

                new Thread(task).start();
            } else {
                AlertUtils.showWarning("Security Checkup", "Error importing test", "The selected file does not exist or is a directory.");
            }
        }
    }

    @FXML
    private void onExportTest(ActionEvent actionEvent) {
        Test test = testListController.getSelectedTest();

        // null if nothing selected
        if (test != null) {

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
                            securityCheckupApp.getGson().toJson(test, writer);
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

    @Override
    public void refreshTestList() {
        testListController.clear();
        testListController.showTests(securityCheckupApp.getTestRegistry().getTests());
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
    public Stage getStage() {
        return stage;
    }
}
