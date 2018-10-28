package net.acomputerdog.securitycheckup.main.gui.panels;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import net.acomputerdog.securitycheckup.test.Test;
import net.acomputerdog.securitycheckup.test.TestResult;

public class TestInfoPanel implements Panel {

    private final VBox root;
    private final TableView<ExtraInfo> detailsPane;

    private final Label descText;
    private final Label resultText;

    private final ListView<String> testMessages;

    private final ObservableList<ExtraInfo> testInfo;

    public TestInfoPanel() {
        this.root = new VBox();
        root.setVisible(false); // default hidden
        root.setSpacing(5);
        root.setPadding(new Insets(5, 5, 5, 5));

        this.descText = new Label();
        descText.setFont(Font.font(16));
        descText.setWrapText(true);
        root.getChildren().add(descText);

        this.resultText = new Label();
        resultText.setFont(Font.font(16));
        resultText.setWrapText(true);
        resultText.managedProperty().bind(resultText.visibleProperty()); // don't take space when invisible
        VBox.setVgrow(resultText, Priority.ALWAYS);
        root.getChildren().add(resultText);

        Text messagesText = new Text("Test messages:");
        messagesText.managedProperty().bind(messagesText.visibleProperty()); // don't take space when invisible
        root.getChildren().add(messagesText);

        this.testMessages = new ListView<>();
        testMessages.managedProperty().bind(testMessages.visibleProperty()); // don't take space when invisible
        messagesText.visibleProperty().bind(testMessages.visibleProperty()); // hide label with list
        root.getChildren().add(testMessages);

        this.testInfo = FXCollections.observableArrayList();
        this.detailsPane = new TableView<>();
        detailsPane.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        detailsPane.setItems(testInfo);
        TableColumn<ExtraInfo, String> keys = new TableColumn<>("Property");
        keys.setCellValueFactory(new PropertyValueFactory<>("key"));
        TableColumn<ExtraInfo, String> vals = new TableColumn<>("Value");
        vals.setCellValueFactory(new PropertyValueFactory<>("value"));
        detailsPane.getColumns().addAll(keys, vals);

        root.getChildren().add(new Text("Advanced info:"));
        root.getChildren().add(detailsPane);
        VBox.setVgrow(detailsPane, Priority.ALWAYS);
    }

    public void showTest(Test test) {
        this.showTest(test, null);
    }

    public void showTest(Test test, TestResult result) {
        // Reset results pane
        testInfo.clear();
        resultText.setVisible(false);
        testMessages.getItems().clear();
        testMessages.setVisible(false);

        if (test != null) {
            descText.setText(test.getInfo().getDescription());

            test.getInfo().getInfoMap().forEach((k, v) -> testInfo.add(new ExtraInfo(k, v)));

            // If there is a result to display
            if (result != null) {
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
