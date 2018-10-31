package net.acomputerdog.securitycheckup.main.gui.fxml.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import net.acomputerdog.securitycheckup.main.gui.fxml.panel.TestResultsPanel;
import net.acomputerdog.securitycheckup.test.TestResult;

public class TestResultsPanelController implements TestResultsPanel {
    @FXML
    private VBox root;
    @FXML
    private Label resultText;
    @FXML
    private Label descriptionText;
    @FXML
    private ListView<String> messagesList;

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
            } else {
                resultText.setTextFill(Color.RED);
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
