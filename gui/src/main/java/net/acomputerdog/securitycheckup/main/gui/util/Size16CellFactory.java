package net.acomputerdog.securitycheckup.main.gui.util;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.text.Font;
import javafx.util.Callback;

public class Size16CellFactory<T> implements Callback<ListView<T>, ListCell<T>> {
    @Override
    public ListCell<T> call(ListView<T> cell) {
        return new ListCell<T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null && !empty) {
                    setText(String.valueOf(item));
                    setFont(Font.font(16));
                } else {
                    setText("");
                }
            }
        };
    }
}
