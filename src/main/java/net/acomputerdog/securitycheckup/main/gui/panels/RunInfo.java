package net.acomputerdog.securitycheckup.main.gui.panels;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
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

public class RunInfo implements Panel {
    private final BorderPane root;

    private final Text statusText;

    private final TableView<RunTest> testTable;
    private final TableColumn<RunTest, String> testNameColumn;
    private final TableColumn<RunTest, String> testStatusColumn;

    private final Map<Test, RunTest> runTestMap = new HashMap<>();
    private final ObservableList<RunTest> tests;

    public RunInfo() {
        this.root = new BorderPane();
        root.setVisible(false); //default hidden

        this.statusText = new Text();
        root.setTop(statusText);

        this.testTable = new TableView<>();
        this.tests = FXCollections.observableArrayList();
        testTable.setItems(tests);

        this.testNameColumn = new TableColumn<>("Test");
        testNameColumn.setCellValueFactory(new PropertyValueFactory<>("test"));
        this.testStatusColumn = new TableColumn<>("Result");
        testStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        testTable.getColumns().addAll(testNameColumn, testStatusColumn);

        root.setCenter(testTable);
    }

    public void bind(TestRunner runner) {
        statusText.setText("Status: running");
        runTestMap.clear();
        tests.clear();

        tests.addAll(runner.getTests());

        root.setVisible(true);
        // add each test
        /*
        runner.getProfileInfo().getProfile().getTestSuite().getTests().forEach(test -> {
            RunTest runTest = new RunTest(test);
            tests.add(runTest);
            runTestMap.put(test, runTest);
        });
        */

        //TODO bind

    }

    @Override
    public Node getRoot() {
        return root;
    }
}
