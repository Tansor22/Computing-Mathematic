package utils;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class Helper {
   public static boolean isInteger(String str) {
        if (str.equals("") || str == null) return false;
        for (int i = 0; i < str.length(); i++)
            if (!Character.isDigit(str.charAt(i))) return false;
        return true;
    }
    public static boolean isDouble(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            ++i;
        }
        int integerPartSize = 0;
        int exponentPartSize = -1;
        while (i < length) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                if (c == '.' && integerPartSize > 0 && exponentPartSize == -1) {
                    exponentPartSize = 0;
                } else {
                    return false;
                }
            } else if (exponentPartSize > -1) {
                ++exponentPartSize;
            } else {
                ++integerPartSize;
            }
            ++i;
        }
        if ((str.charAt(0) == '0' && i > 1 && exponentPartSize < 1)
                || exponentPartSize == 0 || (str.charAt(length - 1) == '.')) {
            return false;
        }
        return true;
    }
    public static void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
		((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("/resources/img/icon.png"));
        alert.setTitle("Предупреждение");
        alert.setHeaderText("Что-то пошло не так");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
