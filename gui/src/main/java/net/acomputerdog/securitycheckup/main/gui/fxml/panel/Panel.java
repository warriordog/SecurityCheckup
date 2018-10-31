package net.acomputerdog.securitycheckup.main.gui.fxml.panel;

import javafx.scene.Node;

/**
 * A Panel is a JFX node that contains a collection of nodes that together form a single piece of a UI.  This interface
 * should be extended and implemented by Controllers for FXML files.
 */
public interface Panel {

    Node getRoot();
}
