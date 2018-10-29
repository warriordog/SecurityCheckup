package net.acomputerdog.securitycheckup.main.gui.panels;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.util.Callback;
import net.acomputerdog.securitycheckup.main.gui.runner.TestRunner;
import net.acomputerdog.securitycheckup.main.gui.runner.TestRunner.RunTest;
import net.acomputerdog.securitycheckup.test.Test;
import net.acomputerdog.securitycheckup.test.TestResult;

import java.util.HashMap;
import java.util.Map;

public class RunInfoPanel implements Panel {
    private final BorderPane root;

    private final Label statusText;

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

        this.statusText = new Label();
        statusText.setFont(Font.font(16));
        root.setTop(statusText);
        BorderPane.setMargin(statusText, new Insets(5, 0, 5, 0));

        this.testTable = new TableView<>();
        testTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        this.tests = FXCollections.observableArrayList();
        testTable.setItems(tests);
        testTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> this.selectTest(newValue));

        this.testNameColumn = new TableColumn<>("Test");
        testNameColumn.setCellValueFactory(new PropertyValueFactory<>("test"));
        this.testStatusColumn = new TableColumn<>("Result");
        testStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        testStatusColumn.setCellFactory(new ColoredCellFactory<>());
        this.testScoreColumn = new TableColumn<>("Score");
        testScoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        testScoreColumn.setCellFactory(new ColoredCellFactory<>());

        testTable.getColumns().add(testNameColumn);
        testTable.getColumns().add(testStatusColumn);
        testTable.getColumns().add(testScoreColumn);

        this.testInfo = new TestInfoPanel();

        SplitPane testPane = new SplitPane();
        testPane.getItems().addAll(testTable, testInfo.getRoot());
        SplitPane.setResizableWithParent(testTable, false);
        testPane.setDividerPosition(0, 0.4);

        root.setCenter(testPane);
    }

    public void clear() {
        runTestMap.clear();
        tests.clear();
        root.setVisible(false);
    }

    public void bind(TestRunner runner) {
        clear();

        tests.addAll(runner.getTests());

        root.setVisible(true);
    }

    public void setRunStatus(String message, Paint color) {
        setRunStatus(message);
        statusText.setTextFill(color);
    }

    public void setRunStatus(String message) {
        statusText.setText(message);
    }

    private void selectTest(RunTest test) {
        // is null if a test is selected when "Run" is clicked
        if (test != null) {
            testInfo.showTest(test.getTest(), test.getResults());
        } else {
            testInfo.showTest(null);
        }
    }

    @Override
    public Node getRoot() {
        return root;
    }

    private static class ColoredCellFactory<T> implements Callback<TableColumn<RunTest,T>, TableCell<RunTest,T>> {
        @Override
        public TableCell<RunTest,T> call(TableColumn<RunTest,T> param) {
            return new ColoredTableCell<>();
        }
    }

    private static class ColoredTableCell<T> extends TableCell<RunTest,T> {
        @Override
        protected void updateItem(T item, boolean empty) {
            super.updateItem(item, empty);

            setText(empty ? "" : String.valueOf(item));

            if (!isEmpty()) {
                this.setTextFill(Color.RED);

                TableRow<RunTest> tableRow = this.getTableRow();
                if (tableRow != null) {
                    if (tableRow.getItem() != null) {
                        TestResult result = tableRow.getItem().getResults();
                        if (result != null) {
                            if (result.getResultCause() == TestResult.ResultCause.FINISHED) {
                                if (result.isPassed()) {
                                    this.setTextFill(Color.GREEN);
                                }
                            }
                        } else {
                            // Test is running
                            this.setTextFill(Color.DIMGREY);
                        }
                    }
                }
            }
        }
    }
}
