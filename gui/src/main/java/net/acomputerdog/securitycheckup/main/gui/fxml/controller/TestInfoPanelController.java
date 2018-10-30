package net.acomputerdog.securitycheckup.main.gui.fxml.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import net.acomputerdog.securitycheckup.main.gui.fxml.panel.TestInfoPanel;
import net.acomputerdog.securitycheckup.test.Test;

public class TestInfoPanelController implements TestInfoPanel {
    @FXML
    private Label descriptionText;
    @FXML
    private TableView<ExtraInfo> testInfoView;
    @FXML
    private VBox root;

    @Override
    public void showTest(Test test) {
        // Reset results list
        testInfoView.getItems().clear();

        if (test != null) {
            descriptionText.setText(test.getInfo().getDescription());

            test.getInfo().getInfoMap().forEach((k, v) -> testInfoView.getItems().add(new ExtraInfo(k, v)));

            root.setVisible(true);
        } else {
            descriptionText.setText("");

            root.setVisible(false);
        }
    }

    @Override
    public Node getRoot() {
        return root;
    }

    public static class ExtraInfo {
        private StringProperty key = new SimpleStringProperty(null, "key");
        private StringProperty value = new SimpleStringProperty(null, "value");

        public ExtraInfo(String key, String value) {
            this.key.setValue(key);
            this.value.setValue(value);
        }

        public String getKey() {
            return key.get();
        }

        public StringProperty keyProperty() {
            return key;
        }

        public String getValue() {
            return value.get();
        }

        public StringProperty valueProperty() {
            return value;
        }
    }
}
