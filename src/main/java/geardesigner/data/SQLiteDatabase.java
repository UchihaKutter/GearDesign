package geardesigner.data;

import geardesigner.Log;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 数据库公用类
 * 懒加载+单例模式
 * 所有派生类（代表一张表）共用一个数据库连接
 *
 * @author SUPERSTATION
 */
public abstract class SQLiteDatabase implements AutoCloseable {

    private static final String DRIVER_NAME = "org.sqlite.JDBC";
    /**
     * 配置二进制数据库的URL dir_path
     */
    private static final String DB_PREFIX = "jdbc:sqlite:";

    private static final int BUFFER_SIZE = 1024;

    /**
     * 数据库文件名
     */
    final String DB_FILE_PATH;
    final String TABLE_NAME;
    final String SQL_CREATE_TABLE;

    /**
     * 公用的构造方法
     *
     * @param dbFilePath  SQLite.db文件的存储路径
     * @param tableName   表名
     * @param createTable 表创建语句
     */
    public SQLiteDatabase(@NotNull String dbFilePath, @NotNull String tableName, @NotNull String createTable) {
        DB_FILE_PATH = dbFilePath;
        TABLE_NAME = tableName;
        SQL_CREATE_TABLE = createTable;
    }

    /**
     * 数据库Helper类的连接、建表基础方法
     */

    /**
     * 定义连接数据库的方法（快速失败）
     * 连接SQLite的代码
     *
     * @return 一个数据库连接
     * @throws ClassNotFoundException 数据库驱动不存在
     * @throws SQLException           建表失败
     */
    Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName(DRIVER_NAME);
        final Connection connection = DriverManager.getConnection(DB_PREFIX + DB_FILE_PATH);
        Log.info(this.getClass().getName() + ": 成功连接数据库");
        return connection;
    }

    /**
     * 用于获取数据库连接的公用方法，
     *
     * @return 每张表持有的Connect
     */
    public abstract Connection connect();

    /**
     * 首次创建数据库时调用,一般进行建库建表操作
     */
    boolean onCreate(Connection conn, String createTable) {
        try (Statement stat = conn.createStatement()) {
            stat.execute(createTable);
        } catch (SQLException ts) {
            Log.error("创建表" + DB_FILE_PATH + '.' + TABLE_NAME + "失败", ts);
        }
        return true;
    }

    boolean isTableExist(Connection conn, String tableName) {
        try {
            final DatabaseMetaData metaData = conn.getMetaData();
            /**返回名称为tableName的所有表*/
            final ResultSet tts = metaData.getTables(null, null, tableName, null);
            return tts.next();
        } catch (SQLException ts) {
            Log.error("查询表" + this.getClass().getName() + "时触发异常", ts);
            return false;
        }
    }

    /**
     * 对象数据库工具类
     */

    /**
     * 数据压缩（Gzip）
     *
     * @param raw 输入数据
     * @return byte[] 输出压缩后的数据
     * @throws IOException
     */
    static byte[] compress(byte[] raw) throws IOException {
        try (final ByteArrayInputStream is = new ByteArrayInputStream(raw);
             final ByteArrayOutputStream os = new ByteArrayOutputStream();
             GZIPOutputStream gzip = new GZIPOutputStream(os)) {
            int count;
            final byte[] buff = new byte[BUFFER_SIZE];
            while ((count = is.read(buff, 0, BUFFER_SIZE)) != -1) {
                gzip.write(buff, 0, count);
            }
            gzip.finish();
            gzip.flush();
            return os.toByteArray();
        }
    }

    /**
     * 数据解压
     *
     * @param compressed 压缩的数据
     * @return byte[] 输出解压后的数据
     * @throws IOException
     */
    static byte[] decompress(byte[] compressed) throws IOException {
        try (final ByteArrayInputStream is = new ByteArrayInputStream(compressed);
             final ByteArrayOutputStream os = new ByteArrayOutputStream();
             GZIPInputStream gzip = new GZIPInputStream(is)) {
            int count;
            final byte[] buff = new byte[BUFFER_SIZE];
            while ((count = gzip.read(buff, 0, BUFFER_SIZE)) != -1) {
                os.write(buff, 0, count);
            }
            return os.toByteArray();
        }
    }

}
