package net.acomputerdog.securitycheckup.main.gui.scene;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import net.acomputerdog.securitycheckup.main.gui.GUIMain;

public class SceneMain extends Scene {
    private final GUIMain guiMain;

    public SceneMain(GUIMain guiMain) {
        super(createRoot());
        this.guiMain = guiMain;
    }

    public GUIMain getGuiMain() {
        return guiMain;
    }

    private static Parent createRoot() {
        Text text = new Text();
        text.setFont(new Font(18));
        text.setText("Hello, world!");

        BorderPane pane = new BorderPane();
        pane.setCenter(text);

        return pane;
    }
}
