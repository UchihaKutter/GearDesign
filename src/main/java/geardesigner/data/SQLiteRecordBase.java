package geardesigner.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import geardesigner.Log;
import geardesigner.beans.Record;
import geardesigner.beans.Specifications;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 存储计算记录的数据表类DAO
 *
 * @author SUPERSTATION
 */
public class SQLiteRecordBase extends SQLiteDatabase implements RecordBase {
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
            "SECOND_OF_DAY INTEGER NOT NULL" +
            ");";
    /**
     * 共享连接
     */
    private static volatile Connection INSTANCE = null;
    private final ObjectMapper objectMapper;

    /**
     * 创建一个SQLiteRecordBase对象
     *
     * @param dbFilePath 非null的本地数据库路径
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

    @Nullable
    Specifications deserialize(@NotNull String specString) {
        try {
            return objectMapper.readValue(specString, Specifications.class);
        } catch (JsonProcessingException e) {
            Log.error("反序列化Specifications对象出错", e);
            return null;
        }
    }

    /**
     * 从ResultSet中提取一条Record
     *
     * @param rs 未经检查的ResultSet输入
     * @return 有效记录->Record，无效记录->null
     * @throws SQLException 对ResultSet的操作触发异常
     */

    @Nullable
    private final Record recordParser(ResultSet rs) throws SQLException {
        final String specString = rs.getString(1);
        final int yr = rs.getInt(2);
        final int mn = rs.getInt(3);
        final int d = rs.getInt(4);
        final long secondOfDay = rs.getLong(5);
        final Specifications specs = deserialize(specString);
        if (specs != null) {
            return new Record(specs, yr, mn, d, secondOfDay);
        }
        return null;
    }

    /**
     * 基本操作方法
     */
    /**
     * 插入新的记录
     *
     * @param record 记录实例
     * @return true->成功，false->失败
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
                    ps.setLong(5, dateTime.getLong(ChronoField.SECOND_OF_DAY));
                    final int stat = ps.executeUpdate();
                    if (stat > 0) {
                        Log.info("插入记录成功");
                        return true;
                    }
                    Log.info("插入记录失败，未修改数据表");
                } catch (SQLException e) {
                    Log.error("插入记录失败", e);
                }
            }
        }
        return false;
    }

    /**
     * 删除单个年月日的记录
     *
     * @param year  年的数值表示
     * @param month 月的数值表示(1-12)，null->所有月份并忽略days
     * @param days  月中天的数值表示（1-31），null->所有天
     * @return true->成功，false->失败
     */
    boolean delete(@NotNull Integer year, @Nullable Integer month, @Nullable Integer days) {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE YEAR = ?";
        if (month != null) {
            sql += "AND MONTH = ?";
            if (days != null) {
                sql += "AND DAY = ?";
            }
        }
        try (PreparedStatement ps = connect().prepareStatement(sql)) {
            ps.setInt(1, year);
            if (month != null) {
                ps.setInt(2, month);
                if (days != null) {
                    ps.setInt(3, days);
                }
            }
            final int stat = ps.executeUpdate();
            if (stat > 0) {
                Log.info("删除记录成功");
                return true;
            }
            Log.info("删除记录失败，未修改数据表");//待办 2021/8/19: 是否符合预期
        } catch (SQLException e) {
            Log.error("删除记录失败", e);
        }
        return false;
    }

    /**
     * 查询单年月日的计算记录
     *
     * @param year  年的数值表示
     * @param month 月的数值表示(1-12)，null->所有月份并忽略days
     * @param days  月中天的数值表示（1-31），null->所有天
     * @return 非空的不可变列表
     */
    @NotNull
    List<Record> retrieve(@NotNull Integer year, @Nullable Integer month, @Nullable Integer days) {
        final List<Record> rs = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE YEAR = ?";
        if (month != null) {
            sql += "AND MONTH = ?";
            if (days != null) {
                sql += "AND DAY = ?";
            }
        }
        try (PreparedStatement ps = connect().prepareStatement(sql)) {
            ps.setInt(1, year);
            if (month != null) {
                ps.setInt(2, month);
                if (days != null) {
                    ps.setInt(3, days);
                }
            }
            final ResultSet resultSet = ps.executeQuery();
            int count = 0;
            while (resultSet.next()) {
                count++;
                final Record record = recordParser(resultSet);
                if (record != null) {
                    rs.add(record);
                }
            }
            Log.info("符合条件的记录数量：" + count);
        } catch (SQLException e) {
            Log.error("查询记录失败", e);
        }
        Log.info("有效记录数量：" + rs.size());
        return Collections.unmodifiableList(rs);
    }

    /**
     * 不允许修改记录
     *
     * @return 永远抛出异常
     */
    boolean update() {
        throw new UnsupportedOperationException("不允许的数据库操作");
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
        return insert(record);
    }

    /**
     * 检索特定日期的计算记录
     *
     * @param date 不考虑时区的日期对象
     * @return {@code List<Record>}记录的列表
     */
    @Override
    public List<Record> retrievalRecords(@NotNull final LocalDate date) {
        return retrieve(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
    }

    /**
     * 获取最新提交的一个计算记录
     *
     * @return {@code Record}最新一条记录
     */
    @Override
    public Record getLastRecord() {
        final String sql = "SELECT * FROM " + TABLE_NAME + " ORDER BY ROWID DESC LIMIT 1";
        try (Statement st = connect().createStatement()) {
            final ResultSet resultSet = st.executeQuery(sql);
            if (resultSet.next()) {
                final Record record = recordParser(resultSet);
                if (record != null) {
                    return record;
                }
            }

        } catch (SQLException e) {
            Log.error("获取记录失败", e);
        }
        Log.warning("未查询到有效记录");
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
        final List<Record> rs = new ArrayList<>(amount);
        final String sql = "SELECT * FROM " + TABLE_NAME + " ORDER BY ROWID DESC LIMIT ?";
        try (PreparedStatement ps = connect().prepareStatement(sql)) {
            ps.setInt(1, amount);
            final ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                final Record record = recordParser(resultSet);
                if (record != null) {
                    rs.add(record);
                }
            }
        } catch (SQLException e) {
            Log.error("获取记录失败", e);
        }
        Log.info("实际获得的记录数：" + rs.size());
        return rs;
    }

    /**
     * 获取近期(本月)的计算记录
     *
     * @return {@code List<Record>}全部记录的时间倒序列表
     */
    @Override
    public List<Record> getRecords() {
        return retrieve(LocalDate.now().getYear(), null, null);
    }

    /**
     * 清除库中所有记录
     *
     * @return 成功-true，失败（部分删除或未删除）
     */
    @Override
    public boolean deleteAllRecords() {
        try (Statement s = connect().createStatement()) {
            s.execute("DROP TABLE " + TABLE_NAME);
            s.execute(SQL_CREATE_TABLE);
            Log.warning("成功删除所有记录");
            return true;
        } catch (SQLException e) {
            Log.error("删除所有记录失败");
        }
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
        return delete(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
    }
}
