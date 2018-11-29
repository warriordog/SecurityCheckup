package net.acomputerdog.securitycheckup.main.gui.fxml.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import net.acomputerdog.securitycheckup.main.gui.fxml.panel.TestInfoPanel;
import net.acomputerdog.securitycheckup.test.Test;

import java.net.URL;

public class TestInfoPanelController implements TestInfoPanel {
    @FXML
    private Label descriptionText;
    @FXML
    private TableView<ExtraInfo> testInfoView;
    @FXML
    private VBox root;
    @FXML
    public TitledPane fixPane;
    @FXML
    public WebView fixWebView;

    @Override
    public void showTest(Test test) {
        // Reset results list
        clear();

        if (test != null) {
            // set description
            descriptionText.setText(test.getInfo().getDescription());

            // display extra info
            test.getInfo().getInfoMap().forEach((k, v) -> testInfoView.getItems().add(new ExtraInfo(k, v)));

            // Display fix instructions (if present)
            URL fixUrl = test.getInfo().getFixURL();
            if (fixUrl != null) {
                fixWebView.getEngine().load(fixUrl.toString());

                fixPane.setManaged(true);
                fixPane.setVisible(true);
            } else {
                // If failed but no fix, then show message
                fixPane.setManaged(false);
                fixPane.setVisible(false);
            }

            root.setVisible(true);
        } else {
            root.setVisible(false);
        }
    }

    @Override
    public void clear() {
        testInfoView.getItems().clear();
        descriptionText.setText("");
    }

    @Override
    public Node getRoot() {
        return root;
    }

    public static class ExtraInfo {
        private final StringProperty key = new SimpleStringProperty(null, "key");
        private final StringProperty value = new SimpleStringProperty(null, "value");

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
