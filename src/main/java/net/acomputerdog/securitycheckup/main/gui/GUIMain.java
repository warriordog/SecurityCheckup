package net.acomputerdog.securitycheckup.main.gui;

import javafx.application.Application;
import javafx.stage.Stage;
import net.acomputerdog.securitycheckup.main.common.BasicTests;
import net.acomputerdog.securitycheckup.main.gui.scene.MainScene;
import net.acomputerdog.securitycheckup.main.gui.test.Profile;

import java.util.ArrayList;
import java.util.List;

public class GUIMain extends Application {

    private List<Profile> defaultProfiles;

    private Stage primaryStage;
    private MainScene mainWin;

    @Override
    public void init() {
        defaultProfiles = new ArrayList<>();
        defaultProfiles.add(new Profile(new BasicTests()));
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            this.mainWin = new MainScene(this);

            // Add all default profiles
            defaultProfiles.forEach(mainWin::addProfile);

            this.primaryStage = primaryStage;
            this.primaryStage.setScene(mainWin.getScene());
            this.primaryStage.setTitle("Security Checkup");
            this.primaryStage.setWidth(800);
            this.primaryStage.setHeight(600);
            this.primaryStage.show();
        } catch (Throwable t) {
            System.err.println("Unhandled exception starting: " + t.toString());
            t.printStackTrace();
            throw t;
        } finally {
            // TODO exit
        }
    }

    @Override
    public void stop() throws Exception {
        try {
            //TODO cleanup resources
        } catch (Throwable t) {
            System.err.println("Unhandled exception while stopping: " + t.toString());
            t.printStackTrace();
            throw t;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
