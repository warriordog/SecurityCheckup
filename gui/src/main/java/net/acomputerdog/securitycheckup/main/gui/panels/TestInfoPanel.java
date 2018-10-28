package net.acomputerdog.securitycheckup.main.gui.panels;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import net.acomputerdog.securitycheckup.test.Test;
import net.acomputerdog.securitycheckup.test.TestResult;

public class TestInfoPanel implements Panel {

    private final VBox root;
    private final TableView<ExtraInfo> detailsPane;

    private final TitledPane descriptionPane;
    private final TitledPane messagesPane;
    private final TitledPane advancedPane;

    private final Label descText;
    private final Label resultText;

    private final ListView<String> testMessages;

    private final ObservableList<ExtraInfo> testInfo;

    public TestInfoPanel() {
        this.root = new VBox();
        root.setVisible(false); // default hidden
        root.setSpacing(5);
        root.setPadding(new Insets(5, 5, 5, 5));

        this.resultText = new Label();
        resultText.setFont(Font.font(16));
        resultText.setWrapText(true);
        resultText.managedProperty().bind(resultText.visibleProperty()); // don't take space when invisible
        VBox.setVgrow(resultText, Priority.ALWAYS);
        root.getChildren().add(resultText);

        this.descText = new Label();
        descText.setFont(Font.font(16));
        descText.setWrapText(true);
        descText.setTextAlignment(TextAlignment.LEFT);

        VBox descBox = new VBox();
        descBox.getChildren().add(descText);
        descBox.setSpacing(0);
        descBox.setPadding(new Insets(5, 5, 5,5));
        this.descriptionPane = new TitledPane("Description", descBox);
        root.getChildren().add(descriptionPane);

        this.testMessages = new ListView<>();
        this.messagesPane = new TitledPane("Messages", testMessages);
        messagesPane.managedProperty().bind(messagesPane.visibleProperty()); // don't take space when invisible
        root.getChildren().add(messagesPane);

        this.testInfo = FXCollections.observableArrayList();
        this.detailsPane = new TableView<>();
        detailsPane.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        detailsPane.setItems(testInfo);
        TableColumn<ExtraInfo, String> keys = new TableColumn<>("Property");
        keys.setCellValueFactory(new PropertyValueFactory<>("key"));
        TableColumn<ExtraInfo, String> vals = new TableColumn<>("Value");
        vals.setCellValueFactory(new PropertyValueFactory<>("value"));
        detailsPane.getColumns().addAll(keys, vals);

        this.advancedPane = new TitledPane("Advanced info", detailsPane);
        advancedPane.managedProperty().bind(advancedPane.visibleProperty()); // don't take space when invisible
        root.getChildren().add(advancedPane);
        VBox.setVgrow(advancedPane, Priority.ALWAYS);
    }

    public void showTest(Test test) {
        this.showTest(test, null);
    }

    public void showTest(Test test, TestResult result) {
        // Reset results pane
        testInfo.clear();
        resultText.setVisible(false);
        testMessages.getItems().clear();

        if (test != null) {
            descText.setText(test.getInfo().getDescription());

            test.getInfo().getInfoMap().forEach((k, v) -> testInfo.add(new ExtraInfo(k, v)));

            // If there is a result to display
            if (result != null) {
                messagesPane.setVisible(true);

                // create results line
                StringBuilder resultString = new StringBuilder();
                resultString.append(result.getResultString());

                // only add instructions if it failed but the test ran correctly
                if (!result.isPassed() && result.getResultCause() == TestResult.ResultCause.FINISHED) {
                    resultString.append(" - ");
                    resultString.append(test.getInfo().getFailureAdvice());
                }

                // set result value
                resultText.setText(resultString.toString());
                resultText.setVisible(true);

                // set result color
                if (result.isPassed()) {
                    resultText.setTextFill(Color.GREEN);
                } else {
                    resultText.setTextFill(Color.RED);
                }

                // write
                testMessages.getItems().addAll(result.getTestMessages());
                testMessages.setVisible(true);

                // write extra info
                result.getInfoMap().forEach((k, v) -> testInfo.add(new ExtraInfo(k, v)));
            } else {
                messagesPane.setVisible(false);
            }

            root.setVisible(true);
        } else {
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
