package net.acomputerdog.securitycheckup.main.gui.panels;

import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import net.acomputerdog.securitycheckup.main.gui.test.Profile;
import net.acomputerdog.securitycheckup.main.gui.test.ProfileTest;

public class ProfileInfo implements Panel {
    private final BorderPane root;

    private final TabPane tabs;
    private final Tab tabOverview;
    private final Tab tabTests;
    private final SplitPane testSplit;

    // change with each profile
    private final Text title;
    private final StackPane infoPane;
    private final ListView<ProfileTest> testList;
    private final StackPane testInfoPane;

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
        this.tabTests = new Tab();
        tabTests.setText("Tests");
        this.tabs.getTabs().addAll(tabOverview, tabTests);
        root.setCenter(tabs);

        // profile info
        this.infoPane = new StackPane();
        tabOverview.setContent(infoPane);

        // test list and info
        this.testList = new ListView<>();
        testList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> ProfileInfo.this.onSelectTest(newValue));
        this.testInfoPane = new StackPane();
        this.testSplit = new SplitPane();
        testSplit.getItems().addAll(testList, testInfoPane);
        tabTests.setContent(testSplit);

        // default to not visible
        root.setVisible(false);
    }

    private void onSelectTest(ProfileTest test) {

    }


    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;

        this.infoPane.getChildren().clear();
        this.testList.getItems().clear();
        this.testInfoPane.getChildren().clear();

        if (profile != null) {
            this.title.setText(profile.getTestSuite().getName());

            BorderPane idPane = new BorderPane();
            idPane.setLeft(new Text("ID: "));
            idPane.setCenter(new Text(profile.getTestSuite().getId()));
            this.infoPane.getChildren().add(idPane);
            BorderPane descPane = new BorderPane();
            descPane.setLeft(new Text("Description: "));
            descPane.setCenter(new Text(profile.getTestSuite().getDescription()));
            this.infoPane.getChildren().add(descPane);

            this.testList.getItems().addAll(profile.getTests());

            root.setVisible(true);
        } else {
            this.title.setText("");
            root.setVisible(false);
        }
    }

    @Override
    public Node getRoot() {
        return root;
    }
}
