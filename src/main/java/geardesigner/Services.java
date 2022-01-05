package geardesigner;

import geardesigner.data.RecordBase;
import geardesigner.data.SQLiteRecordBase;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.robot.Robot;
import javafx.stage.Screen;

/**
 * 后台服务
 *
 * @author SUPERSTATION
 */
public class Services {
    private static final String DBPath = "History.db";
    /**
     * 懒加载，双重锁
     */
    private static volatile Services INSTANCE = null;
    private final RecordBase recordBase;
    private final Screen primaryScreen;
    private final DoubleProperty scale;

    private Services() {
        recordBase = new SQLiteRecordBase(DBPath);
        primaryScreen=Screen.getPrimary();
        final double sX = primaryScreen.getOutputScaleX();
        final double sY = primaryScreen.getOutputScaleY();
        scale=new SimpleDoubleProperty((sX>sY)?sY:sX);
    }

    public static Services get() {
        if (INSTANCE == null) {
            /**
             * 运行时锁
             */
            synchronized (Services.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Services();
                }
            }
        }
        return INSTANCE;
    }

    public RecordBase RecordBase() {
        return recordBase;
    }

    public ReadOnlyDoubleProperty scaleProperty() {
        return scale;
    }
}
