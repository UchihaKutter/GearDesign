package geardesigner.controls;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

/**
 * 自动更新序列号的Cell工厂
 *
 * @author SUPERTOP
 */
public class SerialNumCellFactory implements Callback<TableColumn, TableCell> {
    @Override
    public TableCell call(TableColumn param) {
        return new TableCell() {
            @Override
            public void updateItem(Object i, boolean empty) {
                super.updateItem(i, empty);
                this.setText(empty ? null : String.valueOf(this.getIndex() + 1));
                this.setGraphic(null);
            }
        };
    }
}
