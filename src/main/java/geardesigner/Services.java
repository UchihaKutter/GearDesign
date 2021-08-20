package geardesigner;

import geardesigner.data.RecordBase;
import geardesigner.data.SQLiteRecordBase;

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

    private Services() {
        recordBase = new SQLiteRecordBase(DBPath);
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
}
