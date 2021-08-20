package geardesigner.data;

import geardesigner.beans.Record;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.List;

/**
 * 记录计算历史
 *
 * @author SUPERSTATION
 */
public interface RecordBase {
    /**
     * 提交计算记录
     *
     * @param record 提交的记录
     * @return 成功提交-true，提交失败-false
     */
    boolean submitRecord(@NotNull Record record);

    /**
     * 检索特定日期的计算记录
     *
     * @param date 不考虑时区的日期对象
     * @return {@code List<Record>}记录的列表
     */
    List<Record> retrievalRecords(@NotNull LocalDate date);

    /**
     * 获取最新提交的一个计算记录
     *
     * @return {@code Record}最新一条记录
     */
    Record getLastRecord();

    /**
     * 获取最新的{@code amount}个计算记录
     *
     * @param amount 最新记录数，>0
     * @return {@code List<Record>}指定数量的列表
     */
    List<Record> getLastRecords(int amount);

    /**
     * 获取近期的计算记录
     * “近期”的定义由具体实现决定，一般为当前月
     *
     * @return {@code List<Record>}全部记录的时间倒序列表
     */
    List<Record> getRecords();

    /**
     * 清除库中所有记录
     *
     * @return 成功-true，失败（部分删除或未删除）
     */
    boolean deleteAllRecords();

    /**
     * 清除库中指定日期的所有记录
     *
     * @param date 不考虑时区的日期对象
     * @return 成功-true，失败（部分删除或未删除）
     */
    boolean deleteRecords(@NotNull LocalDate date);
}
