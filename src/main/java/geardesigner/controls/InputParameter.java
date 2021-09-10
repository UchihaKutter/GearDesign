package geardesigner.controls;

import geardesigner.beans.InputException;
import geardesigner.units.ConvertibleUnit;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 输入型参数的UI控件
 *
 * @author SUPERSTATION
 */
public class InputParameter<U extends ConvertibleUnit> extends Parameter {
    private static final StringConverter ConvertibleUnitStringConverter = new StringConverter() {

        @Override
        public String toString(Object o) {
            if (o instanceof ConvertibleUnit) {
                return ((ConvertibleUnit) o).getDisplay();
            }
            return o.toString();
        }

        @Override
        public Object fromString(String s) {
            return null;
        }
    };
    private final TextField field;
    private final ChoiceBox<U> units;

    public InputParameter(@NotNull String name, @Nullable Number initialValue, @Nullable U unit) {
        super(name, unit);
        this.field = new TextField((initialValue == null) ? null : String.valueOf(initialValue));
        this.units = new ChoiceBox<>();
        initStyle();
        initListener();

        valuePane.getChildren().add(this.field);
        /**
         * 填充候选单位
         */
        if (unit != null) {
            symbolPane.getChildren().add(this.units);
            final ConvertibleUnit[] enumConstants = unit.getClass().getEnumConstants();
            final ObservableList<U> items = units.getItems();
            for (ConvertibleUnit u : enumConstants) {
                items.add((U) u);
            }
            units.setValue(unit);
        }
    }

    public InputParameter(String name) {
        this(name, null, null);
    }

    public InputParameter(String name, U unit) {
        this(name, null, unit);
    }

    @Override
    void initStyle() {
        this.units.setConverter(ConvertibleUnitStringConverter);
        this.getChildren().addAll(namePane, symbolPane, valuePane);
    }

    @Override
    void refresh() {
        /**
         * 刷新什么都不做
         */
    }

    private void initListener() {
        units.setOnAction(e -> setUnit(units.getValue()));
    }

    /**
     * 获取输入值
     *
     * @return null 或基本单位的数值
     * @throws InputException 用户非法输入引发的错误
     */
    @Override
    public @Nullable Double getValue() throws InputException {
        final String vs = field.getText().trim();
        if (vs.isBlank()) {
            return null;
        } else {
            /**
             * 捕获输入不合法错误
             */
            try {
                final double v = Double.parseDouble(vs);
                return unit == null ? v : unit.toBaseUnit(v);
            } catch (NumberFormatException e) {
                throw new InputException("请输入正确的" + getName());
            }
        }
    }

    /**
     * 为输入框设定值。注意：系统内值的处理，总是使用基本单位
     *
     * @param v 基本单位值
     */
    @Override
    void setValue(final @Nullable Number v) {
        if (v == null) {
            field.setText(null);
        } else {
            field.setText(String.valueOf(unit == null ? v : unit.toCurrentUnit((Double) v)));
        }
    }

    public void setPromptText(@Nullable final String str) {
        field.setPromptText(str);
    }

    /**
     * 设置输入的格式化器
     *
     * @param formatter 不能和其他输入框共用格式化器实例
     */
    public void setTextFormatter(TextFormatter<Double> formatter) {
        field.setTextFormatter(formatter);
    }
}
