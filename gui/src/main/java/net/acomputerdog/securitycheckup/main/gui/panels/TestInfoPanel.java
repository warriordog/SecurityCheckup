package net.acomputerdog.securitycheckup.main.gui.panels;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import net.acomputerdog.securitycheckup.test.Test;
import net.acomputerdog.securitycheckup.test.TestResult;

public class TestInfoPanel implements Panel {

    private final VBox root;
    private final TableView<ExtraInfo> detailsPane;

    private final Label descText;

    private final ObservableList<ExtraInfo> testInfo;

    /*
    private final Text resultScoreLabel;
    private final Text resultStateLabel;
    private final Text resultStringLabel;
    private final Text resultMessageLabel;
    private final Text resultExceptionLabel;
    private final Label resultScoreText;
    private final Label resultStateText;
    private final Label resultStringText;
    private final Label resultMessageText;
    private final Label resultExceptionText;
    */


    public TestInfoPanel() {
        this.root = new VBox();
        root.setVisible(false); // default hidden
        root.setSpacing(5);
        root.setPadding(new Insets(5, 5, 5, 5));

        this.descText = new Label();
        descText.setFont(Font.font(14));
        descText.setWrapText(true);
        root.getChildren().add(descText);

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



        /*
        this.resultStringLabel = new Text("Result: ");
        this.resultStringText = new Label();
        resultStringText.setWrapText(true);

        this.resultScoreLabel = new Text("Score: ");
        this.resultScoreText = new Label();
        resultScoreText.setWrapText(true);

        this.resultStateLabel = new Text("State: ");
        this.resultStateText = new Label();
        resultStateText.setWrapText(true);

        this.resultMessageLabel = new Text("Message: ");
        this.resultMessageText = new Label();
        resultMessageText.setWrapText(true);

        this.resultExceptionLabel = new Text("Exception: ");
        this.resultExceptionText = new Label();
        resultExceptionText.setWrapText(true);


        detailsPane.add(new Text("ID: "), 0, 0);
        detailsPane.add(idText, 1, 0);
        detailsPane.add(resultStringLabel, 0, 1);
        detailsPane.add(resultStringText, 1, 1);
        detailsPane.add(resultScoreLabel, 0, 2);
        detailsPane.add(resultScoreText, 1, 2);
        detailsPane.add(resultStateLabel, 0, 3);
        detailsPane.add(resultStateText, 1, 3);
        detailsPane.add(resultMessageLabel, 0, 4);
        detailsPane.add(resultMessageText, 1, 4);
        detailsPane.add(resultExceptionLabel, 0, 5);
        detailsPane.add(resultExceptionText, 1, 5);
        */
    }

    public void showTest(Test test) {
        this.showTest(test, null);
    }

    public void showTest(Test test, TestResult result) {
        // Reset results pane
        //showResult(null);
        testInfo.clear();

        if (test != null) {
            descText.setText(test.getInfo().getDescription());
            //idText.setText(test.getInfo().getID());

            test.getInfo().getInfoMap().forEach((k, v) -> testInfo.add(new ExtraInfo(k, v)));

            if (result != null) {
                result.getInfoMap().forEach((k, v) -> testInfo.add(new ExtraInfo(k, v)));
            }

            root.setVisible(true);
        } else {
            root.setVisible(false);
        }
    }

    /*

    private void setResultsVisible(boolean visible) {
        resultExceptionLabel.setVisible(visible);
        resultMessageLabel.setVisible(visible);
        resultStringLabel.setVisible(visible);
        resultStateLabel.setVisible(visible);
        resultScoreLabel.setVisible(visible);
        resultExceptionText.setVisible(visible);
        resultMessageText.setVisible(visible);
        resultStringText.setVisible(visible);
        resultStateText.setVisible(visible);
        resultScoreText.setVisible(visible);
    }
*/

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
