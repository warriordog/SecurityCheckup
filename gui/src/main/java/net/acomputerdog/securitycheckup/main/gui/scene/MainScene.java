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
import net.acomputerdog.securitycheckup.main.gui.SecurityCheckupApplication;
import net.acomputerdog.securitycheckup.main.gui.panels.ProfileInfoPanel;
import net.acomputerdog.securitycheckup.test.Profile;

public class MainScene {

    private final SecurityCheckupApplication securityCheckupApp;
    private final Scene scene;

    // components
    private final BorderPane mainPane;
    private final ListView<Profile> profilesList;

    // data
    private final ObservableList<Profile> profiles;
    private final ProfileInfoPanel selectedProfile;

    public MainScene(SecurityCheckupApplication securityCheckupApp) {
        this.securityCheckupApp = securityCheckupApp;

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
        menuProfilesManage.setDisable(true);
        MenuItem menuTestsManage = new MenuItem("Manage tests");
        menuTestsManage.setDisable(true);
        menuTest.getItems().addAll(menuProfilesManage, menuTestsManage);
        // Help menu
        Menu menuHelp = new Menu("Help");
        MenuItem menuHelpAbout = new MenuItem("About");
        menuHelpAbout.setOnAction(v -> securityCheckupApp.showAbout());
        menuHelp.getItems().add(menuHelpAbout);

        // Menubar
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(menuFile, menuTest, menuHelp);
        mainPane.setTop(menuBar);

        // Profile list, profile info on other
        SplitPane profilesSplit = new SplitPane();
        profilesSplit.setDividerPosition(0, 0.25f);
        mainPane.setCenter(profilesSplit);

        // Profiles list
        BorderPane profilesListPane = new BorderPane();
        Text profilesListLabel = new Text("Test profiles:");
        profilesListLabel.setFont(new Font(18));
        profilesListPane.setTop(profilesListLabel);
        BorderPane.setMargin(profilesListLabel, new Insets(5, 5, 5,5));
        this.selectedProfile = new ProfileInfoPanel();
        this.profiles = FXCollections.observableArrayList();
        refreshProfiles(); // load profiles
        this.profilesList = new ListView<>(this.profiles);

        // Create cell factory that will set font size
        profilesList.setCellFactory(cell -> new ListCell<Profile>() {
            @Override
            protected void updateItem(Profile item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item.toString());
                    setFont(Font.font(16));
                }
            }
        });

        // event handler for select profile
        profilesList.getSelectionModel().selectedItemProperty().addListener(e -> selectedProfile.setProfile(profilesList.getSelectionModel().getSelectedItem()));
        profilesListPane.setCenter(profilesList);

        profilesSplit.getItems().add(profilesListPane);
        profilesSplit.getItems().add(selectedProfile.getRoot());
        SplitPane.setResizableWithParent(profilesListPane, false);

        scene = new Scene(mainPane);
    }

    public void refreshProfiles() {
        // TODO find some way to combine ObservableList and TestRegistry
        profiles.clear();
        profiles.addAll(securityCheckupApp.getTestRegistry().getProfiles());
    }

    public SecurityCheckupApplication getSecurityCheckupApp() {
        return securityCheckupApp;
    }

    public Scene getScene() {
        return scene;
    }

    public void addRunButtonListener(ProfileInfoPanel.RunListener listener) {
        this.selectedProfile.addRunButtonListener(listener);
    }
}
