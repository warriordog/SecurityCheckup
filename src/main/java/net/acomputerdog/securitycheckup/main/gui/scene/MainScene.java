package net.acomputerdog.securitycheckup.main.gui.scene;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import net.acomputerdog.securitycheckup.main.gui.GUIMain;
import net.acomputerdog.securitycheckup.main.gui.panels.ProfileInfo;
import net.acomputerdog.securitycheckup.test.suite.Profile;

public class MainScene {

    private final GUIMain guiMain;
    private final Scene scene;

    // components
    private final BorderPane mainPane;
    private final ListView<Profile> profilesList;

    // data
    private final ObservableList<Profile> profiles;
    private final ProfileInfo selectedProfile;

    public MainScene(GUIMain guiMain) {
        this.guiMain = guiMain;

        // Pane with Menubar and content
        this.mainPane = new BorderPane();

        // Menus
        // File menu
        Menu menuFile = new Menu("File");
        MenuItem menuFileExit = new MenuItem("Exit");
        menuFileExit.setOnAction(e -> Platform.exit()); // Exit when exit pressed
        menuFile.getItems().add(menuFileExit);
        // Tests menu
        Menu menuTest = new Menu("Tests");
        MenuItem menuProfilesManage = new MenuItem("Manage profiles");
        MenuItem menuTestsManage = new MenuItem("Manage tests");
        menuTest.getItems().addAll(menuProfilesManage, menuTestsManage);
        // Help menu
        Menu menuHelp = new Menu("Help");
        MenuItem menuHelpAbout = new MenuItem("About");
        menuHelp.getItems().add(menuHelpAbout);

        // Menubar
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(menuFile, menuTest, menuHelp);
        mainPane.setTop(menuBar);

        // Profile list, profile info on other
        SplitPane profilesSplit = new SplitPane();
        profilesSplit.setDividerPosition(0, 0.15f);
        mainPane.setCenter(profilesSplit);

        // Profiles list
        BorderPane profilesPane = new BorderPane();
        Text profilesListLabel = new Text("Test profiles:");
        profilesListLabel.setFont(new Font(14));
        profilesPane.setTop(profilesListLabel);
        BorderPane.setMargin(profilesListLabel, new Insets(5, 5, 5,5));
        this.selectedProfile = new ProfileInfo();
        this.profiles = FXCollections.observableArrayList();
        this.profilesList = new ListView<>(this.profiles);
        // event handler for select profile
        profilesList.getSelectionModel().selectedItemProperty().addListener(e -> selectedProfile.setProfile(profilesList.getSelectionModel().getSelectedItem()));
        profilesPane.setCenter(profilesList);

        profilesSplit.getItems().add(profilesPane);
        profilesSplit.getItems().add(selectedProfile.getRoot());

        scene = new Scene(mainPane);
    }

    public void addProfile(Profile profile) {
        profiles.add(profile);
    }

    public GUIMain getGuiMain() {
        return guiMain;
    }

    public Scene getScene() {
        return scene;
    }

    public void addRunButtonListener(ProfileInfo.RunListener listener) {
        this.selectedProfile.addRunButtonListener(listener);
    }
}
