package net.acomputerdog.securitycheckup.main.gui.panels;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import net.acomputerdog.securitycheckup.main.gui.test.Profile;
import net.acomputerdog.securitycheckup.main.gui.test.ProfileTest;

public class ProfileInfo implements Panel {
    private final BorderPane root;

    private final TabPane tabs;
    private final Tab tabOverview;
    private final Tab tabTests;
    private final Tab tabDetails;
    private final Tab tabRun;
    private final SplitPane testSplit;

    // change with each profile
    private final Text title;
    private final ListView<ProfileTest> testList;
    private final BorderPane infoPane;
    private final GridPane testInfoPane;
    private final GridPane detailsPane;
    private final BorderPane runPane;

    private final Button runButton;
    private final RunInfo runInfo;

    private Profile profile;

    public ProfileInfo() {
        this.root = new BorderPane();

        // Title
        this.title = new Text();
        title.setFont(new Font(18));
        root.setTop(title);

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
        root.setCenter(tabs);

        // profile info
        this.infoPane = new BorderPane();
        infoPane.setPadding(new Insets(5, 5, 5, 5));
        tabOverview.setContent(infoPane);

        // test list and info
        this.testList = new ListView<>();
        testList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> ProfileInfo.this.onSelectTest(newValue));
        this.testInfoPane = new GridPane();
        this.testSplit = new SplitPane();
        testSplit.getItems().addAll(testList, testInfoPane);
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
        this.runButton = new Button();
        runButton.setText("Run tests");
        runPane.setTop(runButton);
        this.runInfo = new RunInfo();
        runPane.setCenter(runInfo.getRoot());

        tabRun.setContent(runPane);

        // default to not visible
        root.setVisible(false);
    }

    private void onSelectTest(ProfileTest test) {
        testInfoPane.getChildren().clear();

        testInfoPane.add(new Text("Name: "), 0, 0);
        testInfoPane.add(new Text(test.getTest().getName()), 1, 0);
        testInfoPane.add(new Text("Description: "), 0, 1);
        testInfoPane.add(new Text(test.getTest().getDescription()), 1, 1);
        testInfoPane.add(new Text("ID: "), 0, 2);
        testInfoPane.add(new Text(test.getTest().getID()), 1, 2);
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;

        this.testList.getItems().clear();
        this.infoPane.getChildren().clear();
        this.testInfoPane.getChildren().clear();
        this.detailsPane.getChildren().clear();

        if (profile != null) {
            // set title
            this.title.setText(profile.getTestSuite().getName());

            // Set overview tab
            this.infoPane.setTop(new Text(profile.getTestSuite().getDescription()));

            // Set test list tab
            this.testList.getItems().addAll(profile.getTests());

            // set details tab
            this.detailsPane.add(new Text("ID: "), 0, 0);
            this.detailsPane.add(new Text(profile.getTestSuite().getId()), 1, 0);
            this.detailsPane.add(new Text("Description: "), 0, 1);
            this.detailsPane.add(new Text(profile.getTestSuite().getDescription()), 1, 1);

            root.setVisible(true);
        } else {
            this.title.setText("");
            root.setVisible(false);
        }
    }

    public void addRunButtonListener(RunListener listener) {
        this.runButton.addEventHandler(ActionEvent.ACTION, e -> {
            listener.onRunClicked(this, this.runInfo);
        });
    }

    public void setRunButtonEnabled(boolean enabled) {
        this.runButton.setDisable(enabled);
    }

    @Override
    public Node getRoot() {
        return root;
    }

    public interface RunListener {
        void onRunClicked(ProfileInfo profileInfo, RunInfo runInfo);
    }
}
