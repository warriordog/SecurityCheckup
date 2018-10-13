package net.acomputerdog.securitycheckup.main.gui.panels;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import net.acomputerdog.securitycheckup.main.gui.runner.TestRunner;
import net.acomputerdog.securitycheckup.main.gui.runner.TestRunner.RunTest;
import net.acomputerdog.securitycheckup.test.Test;

import java.util.HashMap;
import java.util.Map;

public class RunInfoPanel implements Panel {
    private final BorderPane root;

    private final Text statusText;

    private final TableView<RunTest> testTable;
    private final TableColumn<RunTest, String> testNameColumn;
    private final TableColumn<RunTest, String> testStatusColumn;
    private final TableColumn<RunTest, String> testScoreColumn;

    private final Map<Test, RunTest> runTestMap = new HashMap<>();
    private final ObservableList<RunTest> tests;

    private final TestInfoPanel testInfo;

    public RunInfoPanel() {
        this.root = new BorderPane();
        root.setVisible(false); //default hidden

        this.statusText = new Text();
        root.setTop(statusText);
        BorderPane.setMargin(statusText, new Insets(5, 0, 5, 0));

        this.testTable = new TableView<>();
        this.tests = FXCollections.observableArrayList();
        testTable.setItems(tests);
        testTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> this.selectTest(newValue));

        this.testNameColumn = new TableColumn<>("Test");
        testNameColumn.setCellValueFactory(new PropertyValueFactory<>("test"));
        this.testStatusColumn = new TableColumn<>("Result");
        testStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        this.testScoreColumn = new TableColumn<>("Score");
        testScoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));

        testTable.getColumns().addAll(testNameColumn, testStatusColumn, testScoreColumn);

        this.testInfo = new TestInfoPanel();

        SplitPane testPane = new SplitPane();
        testPane.getItems().addAll(testTable, testInfo.getRoot());
        testPane.setDividerPosition(0, 0.5);

        root.setCenter(testPane);
    }

    public void bind(TestRunner runner) {
        runTestMap.clear();
        tests.clear();

        tests.addAll(runner.getTests());

        root.setVisible(true);
    }

    public void setResultString(String message) {
        statusText.setText(message);
    }

    private void selectTest(RunTest test) {
        // is null if a test is selected when "Run" is clicked
        if (test != null) {
            testInfo.showTest(test.getTest());
            testInfo.showResult(test.getResults());
        } else {
            testInfo.showTest(null);
        }
    }

    @Override
    public Node getRoot() {
        return root;
    }
}
