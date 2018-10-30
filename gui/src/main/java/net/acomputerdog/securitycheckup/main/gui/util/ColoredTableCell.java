package net.acomputerdog.securitycheckup.main.gui.util;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableRow;
import javafx.scene.paint.Color;
import net.acomputerdog.securitycheckup.main.gui.runner.TestRunner;
import net.acomputerdog.securitycheckup.test.TestResult;

public class ColoredTableCell<T> extends TableCell<TestRunner.RunTest,T> {
    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);

        setText(empty ? "" : String.valueOf(item));

        if (!isEmpty()) {
            this.setTextFill(Color.RED);

            TableRow<TestRunner.RunTest> tableRow = (TableRow<TestRunner.RunTest>)this.getTableRow();
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
