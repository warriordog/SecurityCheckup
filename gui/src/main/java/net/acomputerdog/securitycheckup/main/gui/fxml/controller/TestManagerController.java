package net.acomputerdog.securitycheckup.main.gui.fxml.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import net.acomputerdog.securitycheckup.main.gui.SecurityCheckupApplication;
import net.acomputerdog.securitycheckup.main.gui.fxml.panel.TestListPanel;
import net.acomputerdog.securitycheckup.main.gui.fxml.window.TestManagerWindow;
import net.acomputerdog.securitycheckup.main.gui.json.JsonUI;
import net.acomputerdog.securitycheckup.test.Test;

public class TestManagerController implements TestManagerWindow {
    @FXML
    private Stage stage;
    @FXML
    private TestListPanel testListController;

    private SecurityCheckupApplication securityCheckupApp;

    @FXML
    private void initialize() {
    }

    public void initData(SecurityCheckupApplication securityCheckupApplication) {
        this.securityCheckupApp = securityCheckupApplication;

        refreshTestsList();
    }

    @FXML
    private void onRemoveTest(ActionEvent actionEvent) {
        Test test = testListController.getSelectedTest();

        // null if nothing selected
        if (test != null) {
            securityCheckupApp.getTestRegistry().removeTest(test);

            // redraw profile
            refreshTestsList();
            testListController.clearTestInfo();
        }
    }

    @FXML
    private void onImportTest(ActionEvent actionEvent) {
        JsonUI.showImportTest(securityCheckupApp, getStage());
    }

    @FXML
    private void onExportTest(ActionEvent actionEvent) {
        Test test = testListController.getSelectedTest();

        // null if nothing selected
        if (test != null) {
            JsonUI.showExportTest(securityCheckupApp, getStage(), test);
        }
    }

    @Override
    public void refreshTestsList() {
        testListController.clear();
        testListController.showTests(securityCheckupApp.getTestRegistry().getTests());
    }

    @Override
    public Stage getStage() {
        return stage;
    }
}
