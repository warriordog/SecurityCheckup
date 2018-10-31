package net.acomputerdog.securitycheckup.main.gui.fxml.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.VBox;
import net.acomputerdog.securitycheckup.main.gui.fxml.panel.TestInfoPanel;
import net.acomputerdog.securitycheckup.main.gui.fxml.panel.TestListPanel;
import net.acomputerdog.securitycheckup.test.Test;

import java.util.Collection;

public class TestListPanelController implements TestListPanel {
    @FXML
    private SplitPane root;
    @FXML
    private ListView<Test> testList;
    @FXML
    private VBox testInfo;
    @FXML
    private TestInfoPanel testInfoController;

    @FXML
    private void initialize() {
        testList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showTestInfo(newValue));
    }

    @Override
    public void showTests(Collection<Test> tests) {
        clear();

        if (tests != null) {
            // Set test list tab
            this.testList.getItems().addAll(tests);

            root.setVisible(true);
        } else {
            root.setVisible(false);
        }
    }

    @Override
    public void clear() {
        this.testList.getItems().clear();
        clearTestInfo();
    }

    @Override
    public Test getSelectedTest() {
        return testList.getSelectionModel().getSelectedItem();
    }

    @Override
    public void showTestInfo(Test value) {
        testInfoController.showTest(value);
    }

    @Override
    public void clearTestInfo() {
        testInfoController.clear();
    }

    @Override
    public Node getRoot() {
        return root;
    }
}
