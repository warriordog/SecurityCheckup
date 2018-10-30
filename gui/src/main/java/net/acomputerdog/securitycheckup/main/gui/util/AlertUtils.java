package net.acomputerdog.securitycheckup.main.gui.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class AlertUtils {
    public static void showInformation(String title, String header, String body) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, body, ButtonType.OK);
        displayAlert(alert, title, header);
    }

    public static void showError(String title, String header, String body) {
        Alert alert = new Alert(Alert.AlertType.ERROR, body, ButtonType.OK);
        displayAlert(alert, title, header);
    }

    public static void showWarning(String title, String header, String body) {
        Alert alert = new Alert(Alert.AlertType.WARNING, body, ButtonType.OK);
        displayAlert(alert, title, header);
    }

    private static void displayAlert(Alert alert, String title, String header) {
        if (title != null) {
            alert.setTitle(title);
        }
        if (header != null) {
            alert.setHeaderText(header);
        }
        alert.show();
    }

}
