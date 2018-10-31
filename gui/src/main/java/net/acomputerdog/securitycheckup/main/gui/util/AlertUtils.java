package net.acomputerdog.securitycheckup.main.gui.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

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

    public static boolean askWarning(String title, String header, String body) {
        Alert alert = new Alert(Alert.AlertType.WARNING, body, ButtonType.YES, ButtonType.NO);
        return askAlert(alert, title, header);
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

    private static boolean askAlert(Alert alert, String title, String header) {
        if (title != null) {
            alert.setTitle(title);
        }
        if (header != null) {
            alert.setHeaderText(header);
        }
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent()) {
            return result.get() == ButtonType.YES;
        } else {
            return false;
        }
    }
}
