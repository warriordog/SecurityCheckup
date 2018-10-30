package net.acomputerdog.securitycheckup.main.gui.util;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import net.acomputerdog.securitycheckup.main.gui.runner.TestRunner;

public class ColoredCellFactory<T> implements Callback<TableColumn<TestRunner.RunTest,T>, TableCell<TestRunner.RunTest,T>> {
    @Override
    public TableCell<TestRunner.RunTest,T> call(TableColumn<TestRunner.RunTest,T> param) {
        return new ColoredTableCell<>();
    }
}
