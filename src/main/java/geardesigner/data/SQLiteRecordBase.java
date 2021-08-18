package geardesigner.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import geardesigner.Log;
import geardesigner.Specifications;
import geardesigner.beans.Record;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

/**
 * 存储计算记录的数据表类DAO
 *
 * @author SUPERSTATION
 */
public class SQLiteRecordBase extends SQLiteDatabase implements RecordBase {
    /**
     * 共享连接
     */
    private static volatile Connection INSTANCE = null;
    /**
     * 数据库表名
     */
    private static final String TABLE_NAME = "RecordHistory";
    /**
     * 创建表的语句
     */
    private static final String SQL_CREATE_TABLE
            = "Create TABLE " + TABLE_NAME + "(" +
            "SPECS TEXT NOT NULL " +
            "YEAR INTEGER NOT NULL" +
            "MONTH INTEGER NOT NULL" +
            "DAY INTEGER NOT NULL" +
            "HOUR INTEGER NOT NULL" +
            "MINUTE INTEGER NOT NULL" +
            "SECOND INTEGER NOT NULL" +
            ");";

    private final ObjectMapper objectMapper;

    /**
     * 创建一个SQLiteRecordBase对象
     *
     * @param dbFilePath
     */
    public SQLiteRecordBase(@NotNull String dbFilePath) {
        super(dbFilePath, TABLE_NAME, SQL_CREATE_TABLE);
        objectMapper = new ObjectMapper();
    }


    /**
     * 数据库连接基本方法实现
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

    /**
     * VO方法
     */
    @Nullable
    String serializeSpecs(@NotNull Specifications specs) {
        try {
            return objectMapper.writeValueAsString(specs);
        } catch (JsonProcessingException e) {
            Log.error("序列化Specifications对象出错", e);
            return null;
        }
    }


    /**
     * 基本操作方法
     */

    boolean insert(Record record) {
        if (record != null) {
            final String json = serializeSpecs(record.getSpecs());
            final LocalDateTime dateTime = record.getTimestamp();
            if (json != null) {
                try (PreparedStatement ps = connect().prepareStatement("INSERT INTO " + TABLE_NAME + " VALUES(?,?,?,?,?,?,?);")) {
                    ps.setString(1, json);
                    ps.setInt(2, dateTime.getYear());
                    ps.setInt(3, dateTime.getMonthValue());
                    ps.setInt(4, dateTime.getDayOfMonth());
                    ps.setInt(5, dateTime.getHour());
                    ps.setInt(6, dateTime.getMinute());
                    ps.setInt(7, dateTime.getSecond());
                    return ps.executeUpdate() > 0 ? true : false;
                } catch (SQLException e) {
                    Log.error("插入新的记录失败", e);
                }
            }
        }
        return false;
    }

    public boolean delete() {
        return false;
    }

    public boolean retrieve() {
        return true;
    }

    public boolean update() {
        return false;
    }

    /**
     * 实用操作方法
     */
    /**
     * 提交计算记录
     *
     * @param record 提交的记录
     * @return 成功提交-true，提交失败-false
     */
    @Override
    public boolean submitRecord(@NotNull final Record record) {
        return false;
    }

    /**
     * 检索特定日期的计算记录
     *
     * @param date 不考虑时区的日期对象
     * @return {@code List<Record>}记录的列表
     */
    @Override
    public List<Record> retrievalRecords(@NotNull final LocalDate date) {
        return null;
    }

    /**
     * 获取最新提交的一个计算记录
     *
     * @return {@code Record}最新一条记录
     */
    @Override
    public Record getLastRecord() {
        return null;
    }

    /**
     * 获取最新的{@code amount}个计算记录
     *
     * @param amount 最新记录数，>0
     * @return {@code List<Record>}指定数量的列表
     */
    @Override
    public List<Record> getLastRecords(final int amount) {
        return null;
    }

    /**
     * 获取前{@code amount}个计算记录，使用指定的{@code Comparator<Record>}
     *
     * @param amount     指定的数量，>0
     * @param comparator {@code Comparator<Record>} 指定的比较器，null则默认按时间倒序排列
     * @return {@code List<Record>}指定数量的排序列表
     */
    @Override
    public List<Record> getSortedRecords(final int amount, final Comparator<Record> comparator) {
        return null;
    }

    /**
     * 获取全部的计算记录
     * 慎用！可能引发溢出
     *
     * @return {@code List<Record>}全部记录的时间倒序列表
     */
    @Override
    public List<Record> getAllRecords() {
        return null;
    }

    /**
     * 清除库中所有记录
     *
     * @return 成功-true，失败（部分删除或未删除）
     */
    @Override
    public boolean deleteAllRecords() {
        return false;
    }

    /**
     * 清除库中指定日期的所有记录
     *
     * @param date 不考虑时区的日期对象
     * @return 成功-true，失败（部分删除或未删除）
     */
    @Override
    public boolean deleteRecords(@NotNull final LocalDate date) {
        return false;
    }
}
