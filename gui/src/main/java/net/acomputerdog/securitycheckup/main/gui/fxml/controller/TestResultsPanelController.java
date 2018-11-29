package net.acomputerdog.securitycheckup.main.gui.fxml.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;
import net.acomputerdog.securitycheckup.main.gui.fxml.panel.TestResultsPanel;
import net.acomputerdog.securitycheckup.test.TestResult;

import java.net.URL;

public class TestResultsPanelController implements TestResultsPanel {
    @FXML
    private VBox root;
    @FXML
    private Label resultText;
    @FXML
    private Label descriptionText;
    @FXML
    private ListView<String> messagesList;
    @FXML
    public WebView fixWebView;
    @FXML
    public Label noFixMessage;
    @FXML
    public TitledPane fixPane;

    @Override
    public void showResults(TestResult result) {
        // Reset results list
        messagesList.getItems().clear();

        if (result != null) {
            descriptionText.setText(result.getTestInfo().getDescription());

            // set result value
            resultText.setText(result.getResultLine());

            // set result color
            if (result.isPassed()) {
                resultText.setTextFill(Color.GREEN);

                // don't show fix if passed
                fixPane.setManaged(false);
                fixPane.setVisible(false);
            } else {
                resultText.setTextFill(Color.RED);

                // always show pane if failed, even if no fix
                fixPane.setVisible(true);
                fixPane.setManaged(true);

                // Display fix instructions (if present)
                URL fixUrl = result.getTestInfo().getFixURL();
                if (fixUrl != null) {
                    fixWebView.getEngine().load(fixUrl.toString());

                    noFixMessage.setVisible(false);
                    fixWebView.setVisible(true);
                } else {
                    // If failed but no fix, then show message
                    noFixMessage.setVisible(true);
                    fixWebView.setVisible(false);
                }
            }

            messagesList.getItems().addAll(result.getTestMessages());

            root.setVisible(true);
        } else {
            descriptionText.setText("");
            resultText.setText("");

            root.setVisible(false);
        }
    }

    @Override
    public Node getRoot() {
        return root;
    }
}
