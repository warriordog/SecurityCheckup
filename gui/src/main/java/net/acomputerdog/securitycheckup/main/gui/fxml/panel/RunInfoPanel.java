package net.acomputerdog.securitycheckup.main.gui.fxml.panel;

import javafx.scene.paint.Paint;
import net.acomputerdog.securitycheckup.main.gui.runner.TestRunner;

public interface RunInfoPanel extends Panel {

    void clear();

    void bind(TestRunner runner);

    void setRunStatus(String message, Paint color) ;

    void setRunStatus(String message);

}
