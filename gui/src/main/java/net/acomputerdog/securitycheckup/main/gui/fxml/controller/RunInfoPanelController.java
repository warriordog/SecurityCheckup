package net.acomputerdog.securitycheckup.main.gui.fxml.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import net.acomputerdog.securitycheckup.main.gui.fxml.panel.RunInfoPanel;
import net.acomputerdog.securitycheckup.main.gui.fxml.panel.TestResultsPanel;
import net.acomputerdog.securitycheckup.main.gui.runner.TestRunner;

public class RunInfoPanelController implements RunInfoPanel {
    @FXML
    public BorderPane root;
    @FXML
    public Label statusText;
    @FXML
    public VBox testInfo;
    @FXML
    public TestResultsPanel testInfoController;
    @FXML
    public TableView<TestRunner.RunTest> testTable;
    @FXML
    public SplitPane testSplit;

    @FXML
    public void initialize() {
        testTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> this.selectTest(newValue));
        //testSplit.setDividerPositions(0.3);
    }

    @Override
    public void clear() {
        testTable.getItems().clear();
        testInfoController.showResults(null);
        root.setVisible(false);
    }

    @Override
    public void bind(TestRunner runner) {
        clear();

        testTable.getItems().addAll(runner.getTests());

        root.setVisible(true);
    }

    @Override
    public void setRunStatus(String message, Paint color) {
        setRunStatus(message);
        statusText.setTextFill(color);
    }

    @Override
    public void setRunStatus(String message) {
        statusText.setText(message);
    }

    private void selectTest(TestRunner.RunTest test) {
        // is null if a test is selected when "Run" is clicked
        if (test != null) {
            //testInfoPanel.showResults(test.getTest(), test.getResults());
            testInfoController.showResults(test.getResults());
        } else {
            testInfoController.showResults(null);
        }
    }


    @Override
    public Node getRoot() {
        return root;
    }
}
