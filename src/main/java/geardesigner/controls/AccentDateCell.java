package geardesigner.controls;

import javafx.collections.ObservableSet;
import javafx.scene.control.DateCell;

import java.time.LocalDate;

/**
 * 根据日期Set着色强调日期的单元格
 *
 * @author SUPERTOP
 */
public class AccentDateCell extends DateCell {
    public static final String AccentColor = "#7DEB56";
    private ObservableSet<LocalDate> accents;

    AccentDateCell(ObservableSet<LocalDate> accents) {
        this.accents = accents;
    }

    @Override
    public void updateItem(LocalDate item, boolean empty) {
        super.updateItem(item, empty);
        if (accents != null && accents.contains(item)) {
            setStyle("-fx-background-color: " + AccentColor);
        }
    }

    //TODO 2021/9/16:检查单元格着色是否会自动更新
}
