package net.acomputerdog.securitycheckup.main.gui;

import javafx.application.Application;
import javafx.concurrent.Worker;
import javafx.stage.Stage;
import net.acomputerdog.securitycheckup.main.gui.panels.ProfileInfoPanel;
import net.acomputerdog.securitycheckup.main.gui.panels.RunInfoPanel;
import net.acomputerdog.securitycheckup.main.gui.runner.TestRunner;
import net.acomputerdog.securitycheckup.main.gui.scene.MainScene;
import net.acomputerdog.securitycheckup.profiles.BasicTests;
import net.acomputerdog.securitycheckup.test.Profile;
import net.acomputerdog.securitycheckup.test.TestResult;

import java.util.ArrayList;
import java.util.List;

public class GUIMain extends Application {

    private List<Profile> defaultProfiles;

    private Stage primaryStage;
    private MainScene mainWin;

    @Override
    public void init() {
        defaultProfiles = new ArrayList<>();
        defaultProfiles.add(new BasicTests());
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            this.mainWin = new MainScene(this);

            // Add all default profiles
            defaultProfiles.forEach(mainWin::addProfile);
            mainWin.addRunButtonListener(this::runProfile);

            this.primaryStage = primaryStage;
            this.primaryStage.setScene(mainWin.getScene());
            this.primaryStage.setTitle("Security Checkup");
            this.primaryStage.setWidth(1200);
            this.primaryStage.setHeight(800);
            this.primaryStage.show();
        } catch (Throwable t) {
            System.err.println("Unhandled exception starting: " + t.toString());
            t.printStackTrace();
            throw t;
        }
    }

    @Override
    public void stop() {
        try {
            //TODO cleanup resources
        } catch (Throwable t) {
            System.err.println("Unhandled exception while stopping: " + t.toString());
            t.printStackTrace();
            throw t;
        }
    }

    private void runProfile(ProfileInfoPanel info, RunInfoPanel runInfo) {
        info.setRunButtonEnabled(true);
        runInfo.setResultString("Running");

        TestRunner runner = new TestRunner(info.getProfile());
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
                runInfo.setResultString("Success: " + TestResult.formatScore(runner.getValue()));
            }
        });

        // handle exceptions
        runner.exceptionProperty().addListener(l -> {
            System.err.println("Uncaught exception in test run thread");
            runner.getException().printStackTrace();

            runInfo.setResultString("Exception: " + runner.getException().toString());
        });

        new Thread(runner).start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}