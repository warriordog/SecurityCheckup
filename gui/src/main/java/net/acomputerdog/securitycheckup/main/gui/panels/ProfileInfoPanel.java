package net.acomputerdog.securitycheckup.main.gui.panels;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import net.acomputerdog.securitycheckup.test.Profile;
import net.acomputerdog.securitycheckup.test.Test;

public class ProfileInfoPanel implements Panel {
    private StackPane root;

    private final TabPane tabs;
    private final Tab tabOverview;
    private final Tab tabTests;
    private final Tab tabDetails;
    private final SplitPane testSplit;

    // change with each profile
    private final ListView<Test> testList;
    private final VBox overviewPane;
    private final TestInfoPanel testInfoPane;
    private final GridPane detailsPane;

    private final Text descriptionText;
    private final Button runButton;
    private final RunInfoPanel runInfo;
    private final Text selectProfileMessage;

    private Profile profile;

    public ProfileInfoPanel() {
        root = new StackPane();

        // tabs
        this.tabs = new TabPane();
        this.tabOverview = new Tab();
        tabOverview.setText("Overview");
        tabOverview.setClosable(false);
        this.tabTests = new Tab();
        tabTests.setText("Tests");
        tabTests.setClosable(false);
        this.tabDetails = new Tab();
        tabDetails.setText("Details");
        tabDetails.setClosable(false);
        tabs.getTabs().addAll(tabOverview, tabTests, tabDetails);

        // overview tab
        this.overviewPane = new VBox();
        overviewPane.setPadding(new Insets(5, 5, 5, 5));
        tabOverview.setContent(overviewPane);

        this.descriptionText = new Text();
        descriptionText.setFont(Font.font(16));

        this.runButton = new Button();
        runButton.setText("Run tests");
        this.runInfo = new RunInfoPanel();
        overviewPane.getChildren().addAll(descriptionText, runButton, runInfo.getRoot());
        VBox.setVgrow(runInfo.getRoot(), Priority.ALWAYS);

        // test list and info
        this.testList = new ListView<>();
        testList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> ProfileInfoPanel.this.onSelectTest(newValue));
        this.testInfoPane = new TestInfoPanel();
        this.testSplit = new SplitPane();
        testSplit.getItems().addAll(testList, testInfoPane.getRoot());
        testSplit.setDividerPosition(0, 0.3);
        SplitPane.setResizableWithParent(testList, false);
        tabTests.setContent(testSplit);

        // Detailed info
        this.detailsPane = new GridPane();
        detailsPane.setPadding(new Insets(5, 5, 5, 5));
        detailsPane.setHgap(2);
        detailsPane.setVgap(2);
        tabDetails.setContent(detailsPane);

        // default to not visible
        tabs.setVisible(false);

        // message to select a profile
        this.selectProfileMessage = new Text("Select a profile from the list on the left.");
        selectProfileMessage.setFont(Font.font(12));

        root.getChildren().add(tabs);
        root.getChildren().add(selectProfileMessage);
    }

    private void onSelectTest(Test test) {
        testInfoPane.showTest(test);
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;

        this.testList.getItems().clear();
        this.detailsPane.getChildren().clear();
        testInfoPane.showTest(null);

        if (profile != null) {
            this.descriptionText.setText(profile.getDescription());

            // Set test list tab
            this.testList.getItems().addAll(profile.getTests());

            // set details tab
            this.detailsPane.add(new Text("ID: "), 0, 0);
            this.detailsPane.add(new Text(profile.getId()), 1, 0);
            this.detailsPane.add(new Text("Description: "), 0, 1);
            this.detailsPane.add(new Text(profile.getDescription()), 1, 1);

            tabs.setVisible(true);
            selectProfileMessage.setVisible(false);
        } else {
            tabs.setVisible(false);
            selectProfileMessage.setVisible(true);
        }
    }

    public void addRunButtonListener(RunListener listener) {
        this.runButton.addEventHandler(ActionEvent.ACTION, e -> listener.onRunClicked(this, this.runInfo));
    }

    public void setRunButtonEnabled(boolean enabled) {
        this.runButton.setDisable(enabled);
    }

    @Override
    public Node getRoot() {
        return root;
    }

    public interface RunListener {
        void onRunClicked(ProfileInfoPanel profileInfo, RunInfoPanel runInfo);
    }
}
