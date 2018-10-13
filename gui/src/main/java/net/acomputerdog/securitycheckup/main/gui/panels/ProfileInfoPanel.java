package net.acomputerdog.securitycheckup.main.gui.panels;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import net.acomputerdog.securitycheckup.test.Profile;
import net.acomputerdog.securitycheckup.test.Test;

public class ProfileInfoPanel implements Panel {
    private final TabPane tabs;
    private final Tab tabOverview;
    private final Tab tabTests;
    private final Tab tabDetails;
    private final Tab tabRun;
    private final SplitPane testSplit;

    // change with each profile
    private final ListView<Test> testList;
    private final BorderPane infoPane;
    private final TestInfoPanel testInfoPane;
    private final GridPane detailsPane;
    private final BorderPane runPane;

    private final Button runButton;
    private final RunInfoPanel runInfo;

    private Profile profile;

    public ProfileInfoPanel() {

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
        this.tabRun = new Tab();
        tabRun.setText("Run");
        tabRun.setClosable(false);
        tabs.getTabs().addAll(tabOverview, tabTests, tabDetails, tabRun);

        // profile info
        this.infoPane = new BorderPane();
        infoPane.setPadding(new Insets(5, 5, 5, 5));
        tabOverview.setContent(infoPane);

        // test list and info
        this.testList = new ListView<>();
        testList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> ProfileInfoPanel.this.onSelectTest(newValue));
        this.testInfoPane = new TestInfoPanel();
        this.testSplit = new SplitPane();
        testSplit.getItems().addAll(testList, testInfoPane.getRoot());
        testSplit.setDividerPosition(0, 0.3);
        tabTests.setContent(testSplit);

        // Detailed info
        this.detailsPane = new GridPane();
        detailsPane.setPadding(new Insets(5, 5, 5, 5));
        detailsPane.setHgap(2);
        detailsPane.setVgap(2);
        tabDetails.setContent(detailsPane);

        // Run pane
        this.runPane = new BorderPane();
        runPane.setPadding(new Insets(5, 5, 5, 5));
        this.runButton = new Button();
        runButton.setText("Run tests");
        runPane.setTop(runButton);
        this.runInfo = new RunInfoPanel();
        runPane.setCenter(runInfo.getRoot());

        tabRun.setContent(runPane);

        // default to not visible
        tabs.setVisible(false);
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
        this.infoPane.getChildren().clear();
        this.detailsPane.getChildren().clear();
        testInfoPane.showTest(null);

        if (profile != null) {
            // Set overview tab
            this.infoPane.setTop(new Text(profile.getInfo().getDescription()));

            // Set test list tab
            this.testList.getItems().addAll(profile.getTests());

            // set details tab
            this.detailsPane.add(new Text("ID: "), 0, 0);
            this.detailsPane.add(new Text(profile.getInfo().getID()), 1, 0);
            this.detailsPane.add(new Text("Description: "), 0, 1);
            this.detailsPane.add(new Text(profile.getInfo().getDescription()), 1, 1);

            tabs.setVisible(true);
        } else {
            tabs.setVisible(false);
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
        return tabs;
    }

    public interface RunListener {
        void onRunClicked(ProfileInfoPanel profileInfo, RunInfoPanel runInfo);
    }
}
