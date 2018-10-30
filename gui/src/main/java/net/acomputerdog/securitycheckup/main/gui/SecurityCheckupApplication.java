package net.acomputerdog.securitycheckup.main.gui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.application.Application;
import javafx.concurrent.Worker;
import javafx.fxml.FXMLLoader;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import net.acomputerdog.securitycheckup.main.gui.fxml.controller.*;
import net.acomputerdog.securitycheckup.main.gui.fxml.panel.ProfileInfoPanel;
import net.acomputerdog.securitycheckup.main.gui.fxml.panel.RunInfoPanel;
import net.acomputerdog.securitycheckup.main.gui.fxml.window.*;
import net.acomputerdog.securitycheckup.main.gui.runner.TestRunner;
import net.acomputerdog.securitycheckup.profiles.BasicTests;
import net.acomputerdog.securitycheckup.test.Profile;
import net.acomputerdog.securitycheckup.test.TestResult;
import net.acomputerdog.securitycheckup.test.registry.TestRegistry;

import java.io.*;

import static net.acomputerdog.securitycheckup.main.gui.GUIMain.displayException;

public class SecurityCheckupApplication extends Application {
    // TODO settings
    public static final float PERFECT_SCORE = 1.0f;
    public static final float PASSING_SCORE = 0.75f;

    private TestRegistry testRegistry;
    private Gson gson;

    private MainController mainController;
    private AboutController aboutController;
    private ProfileManagerController profileManagerController;
    private NewProfileController newProfileController;
    private AddTestController addTestController;

    @Override
    public void init() {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        this.gson = builder.create();

        this.testRegistry = new TestRegistry();
        BasicTests.lookupOrRegister(testRegistry);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            // load windows
            this.mainController = loadFXMLController("/ui/window/main.fxml");
            mainController.initData(this);
            this.aboutController = loadFXMLController("/ui/window/about.fxml");
            this.profileManagerController = loadFXMLController("/ui/window/profile_manager.fxml");
            profileManagerController.initData(this);
            this.newProfileController = loadFXMLController("/ui/window/new_profile.fxml");
            newProfileController.initData(this);
            this.addTestController = loadFXMLController("/ui/window/add_test.fxml");
            addTestController.initData(this);

            // register events
            mainController.addRunButtonListener(this::runProfile);

            profileManagerController.addTestRemoveListener((profile, test) -> {
                // Triggers when test removed from registry
                // TODO selective refresh
                mainController.refreshProfiles();
            });
            profileManagerController.addProfileRemoveListener(profile -> {
                // Triggers profile removed from registry
                // TODO selective refresh
                mainController.refreshProfiles();
            });

            newProfileController.addCreateProfileListener(profile -> {
                // Triggers when profile created
                profileManagerController.refreshProfilesList();

                // TODO selective refresh
                mainController.refreshProfiles();
            });

            addTestController.addAddTestListener((profile, test) -> {
                // triggers when test added to profile
                profileManagerController.showProfile(profile);

                // TODO selective refresh
                mainController.refreshProfiles();
            });

            // show main window
            mainController.refreshProfiles();
            mainController.getStage().show();
        } catch (Throwable t) {
            System.err.println("Unhandled exception starting: " + t.toString());
            t.printStackTrace();

            displayException("Unhandled exception while starting.  The program must now close.", t);

            throw t;
        }
    }

    private void runProfile(ProfileInfoPanel info, RunInfoPanel runInfo) {
        info.setRunButtonEnabled(true);
        runInfo.setRunStatus("Running", Color.DIMGREY);

        TestRunner runner = new TestRunner(testRegistry, info.getProfile());
        runInfo.bind(runner);

        // enable button when run finishes or fails
        runner.runningProperty().addListener(l -> {
            if (!runner.isRunning()) {
                info.setRunButtonEnabled(false);
            }
        });

        // handle normal exit
        runner.stateProperty().addListener(l -> {
            if (runner.getState() == Worker.State.SUCCEEDED) {
                float result = runner.getValue();
                Paint color = result >= PERFECT_SCORE ? Color.GREEN : result >= PASSING_SCORE ? Color.ORANGE : Color.RED;

                runInfo.setRunStatus("Success: " + TestResult.formatScore(runner.getValue()), color);
            }
        });

        // handle exceptions
        runner.exceptionProperty().addListener(l -> {
            System.err.println("Uncaught exception in test run thread");
            runner.getException().printStackTrace();

            runInfo.setRunStatus("Exception: " + runner.getException().toString(), Color.RED);
        });

        new Thread(runner).start();
    }

    // TODO in utility class
    public void saveProfile(Profile profile, File saveFile) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile))) {
            gson.toJson(profile, writer);
        }
    }

    // TODO in utility class
    public Profile loadProfile(File profileFile) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(profileFile))) {
            return gson.fromJson(reader, Profile.class);
        }
    }

    public AboutWindow getAboutWindow() {
        return aboutController;
    }

    public ProfileManagerWindow getProfileManagerWindow() {
        return profileManagerController;
    }

    public NewProfileWindow getNewProfileWindow() {
        return newProfileController;
    }

    public MainWindow getMainWindow() {
        return mainController;
    }

    public AddTestWindow getAddTestWindow() {
        return addTestController;
    }

    public TestRegistry getTestRegistry() {
        return testRegistry;
    }

    private <T> T loadFXMLController(String path) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(path));
        fxmlLoader.load();
        return fxmlLoader.getController();
    }

    public static void launch(String[] args) {
        Application.launch(args);
    }
}
