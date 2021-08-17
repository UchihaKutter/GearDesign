package geardesigner.data;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLiteRecordBase extends SQLiteDatabase {
    /**
     * 共享连接
     */
    private static volatile Connection INSTANCE = null;

    public SQLiteRecordBase(final String dbFilePath, final String tableName) {
        super(dbFilePath, tableName);
    }

    /**
     * 数据库连接基本方法的实现
     */
    /**
     * 用于获取数据库连接的公用方法，
     *
     * @return 每张表持有的Connect
     */
    @Override
    public Connection connect() {
        if (INSTANCE == null) {
            /**
             * 运行时锁
             */
            synchronized (this.getClass()) {
                if (INSTANCE == null) {
                    try {
                        INSTANCE = getConnection();
                        if (!isTableExist(INSTANCE, TABLE_NAME)) {
                            onCreate(INSTANCE, SQL_CREATE_TABLE);
                        }
                    } catch (ClassNotFoundException | SQLException e) {
                        throw new IllegalStateException(e);
                    }
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void close() throws Exception {
        if (INSTANCE != null) {
            /**
             * 运行时锁
             */
            synchronized (this.getClass()) {
                if (INSTANCE != null) {
                    INSTANCE.close();
                    INSTANCE = null;
                }
            }
        }
    }
}
