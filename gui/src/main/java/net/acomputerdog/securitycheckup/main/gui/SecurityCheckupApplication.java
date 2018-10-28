package net.acomputerdog.securitycheckup.main.gui;

import javafx.application.Application;
import javafx.concurrent.Worker;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
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

import static net.acomputerdog.securitycheckup.main.gui.GUIMain.displayException;

public class SecurityCheckupApplication extends Application {
    // TODO settings
    public static final float PERFECT_SCORE = 1.0f;
    public static final float PASSING_SCORE = 0.75f;

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
        }catch (Throwable t) {
            System.err.println("Unhandled exception starting: " + t.toString());
            t.printStackTrace();

            displayException("Unhandled exception while starting.  The program must now close.", t);

            throw t;
        }
    }

    @Override
    public void stop() {
        try {
            //TODO cleanup resources
        } catch (Throwable t) {
            t.printStackTrace();

            displayException("Exception occurred during shutdown.", t);

            throw t;
        }
    }

    private void runProfile(ProfileInfoPanel info, RunInfoPanel runInfo) {
        info.setRunButtonEnabled(true);
        runInfo.setRunStatus("Running", Color.DIMGREY);

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

    public static void launch(String[] args) {
        Application.launch(args);
    }
}