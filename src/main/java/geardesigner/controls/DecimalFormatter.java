package geardesigner.controls;

import geardesigner.MathUtils;
import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.function.UnaryOperator;

/**
 * 用于规范用户输入十进制数值的TextFormatter实现
 *
 * @author SUPERSTATION
 */
public class DecimalFormatter<T extends Double> extends TextFormatter<T> {

    /**
     * 内置的格式化器
     */
    private static final DecimalFormat[] Formatter = new DecimalFormat[]{
            new DecimalFormat("0"),
            new DecimalFormat("0.#"),
            new DecimalFormat("0.##"),
            new DecimalFormat("0.###"),
            new DecimalFormat("0.####"),
            new DecimalFormat("0.#####"),
            new DecimalFormat("0.######")
    };

    /**
     * 使用指定的Converter和Filter创建Formatter,Converter必须指定 default value.
     *
     * @param valueConverter 指定Converter 或 null.
     * @param defaultValue   the default value.
     * @param filter         指定Filter 或 null
     */
    DecimalFormatter(final StringConverter<T> valueConverter, final T defaultValue,
                     final UnaryOperator<Change> filter) {
        super(valueConverter, defaultValue, filter);
    }

    DecimalFormatter() {
        this(null, null, new DecimalFilter());
    }

    public static String toString(Number num, int digit) {
        if (digit < 0 || digit >= Formatter.length) {
            throw new NumberFormatException("不支持的保留位数");
        }
        return Formatter[digit].format(num);
    }

    static class DecimalFilter implements UnaryOperator<Change> {
        /**
         * 允许的文本，不允许粘贴
         */
        private final static HashSet<String> cFilter =
                new HashSet(Arrays.asList("+", "-", ".", "0", "1", "2", "3", "4", "5",
                        "6", "7", "8", "9", "e", "E"));

        /**
         * 输入过滤器，允许删除动作
         *
         * @param change 触发动作
         * @return 是否允许通过
         */
        @Override
        public Change apply(final Change change) {
            final String text = change.getText();
            /**
             * 为支持setValue正常写入值，加入正则判断
             */
            return (change.isDeleted() || cFilter.contains(text) || MathUtils.isDecimal(text)) ? change : null;
        }
    }

    static class DecimalConverter extends StringConverter<Double> {

        /**
         * Converts the object provided into its string form.
         * Format of the returned string is defined by the specific converter.
         *
         * @param object the object of type {@code T} to convert
         * @return a string representation of the object passed in.
         */
        @Override
        public String toString(final Double object) {
            return null;
        }

        /**
         * Converts the string provided into an object defined by the specific converter.
         * Format of the string and type of the resulting object is defined by the specific converter.
         *
         * @param string the {@code String} to convert
         * @return an object representation of the string passed in.
         */
        @Override
        public Double fromString(final String string) {
            return null;
        }
    }
}
