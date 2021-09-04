package geardesigner.controls;

import javafx.scene.control.Alert;
import javafx.stage.Window;
import org.jetbrains.annotations.NotNull;

/**
 * @author SUPERTOP
 * 交互弹框
 */
public class Alerts {
    public static void warning(Window owner, @NotNull String content) {
        final Alert warning = new Alert(Alert.AlertType.WARNING);
        warning.setHeaderText(null);
        warning.setContentText(content);
        warning.initOwner(owner);
        warning.show();
    }
}
