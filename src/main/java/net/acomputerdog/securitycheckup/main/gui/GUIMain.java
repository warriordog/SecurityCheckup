package net.acomputerdog.securitycheckup.main.gui;

import javafx.application.Application;
import javafx.stage.Stage;
import net.acomputerdog.securitycheckup.main.gui.scene.SceneMain;

public class GUIMain extends Application {

    private Stage primaryStage;
    private SceneMain mainWin;

    @Override
    public void init() {
        //TODO test/etc setup
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            this.mainWin = new SceneMain(this);

            this.primaryStage = primaryStage;
            this.primaryStage.setScene(mainWin);
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
