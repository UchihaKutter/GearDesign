package geardesigner.controls;

import geardesigner.InputException;
import geardesigner.units.ConvertibleUnit;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import org.jetbrains.annotations.Nullable;

/**
 * 输入型参数的UI控件
 *
 * @author SUPERSTATION
 */
public class InputParameter<U extends ConvertibleUnit> extends Parameter {
    private final TextField field;

    public InputParameter(String name, Number initialValue, U unit) {
        super(name, unit);
        this.field = new TextField((initialValue == null) ? null : String.valueOf(initialValue));
        valuePane.getChildren().add(this.field);
    }

    public InputParameter(String name) {
        this(name, null, null);
    }

    public InputParameter(String name, U unit) {
        this(name, null, unit);
    }

    @Override
    void initStyle() {
        super.initStyle();
        this.getChildren().addAll(namePane, symbolPane, valuePane);
    }

    private void initListener() {
        /**
         * 输入框变更度量单位时，对数值什么也不做
         */
    }

    /**
     * 获取输入值
     *
     * @return null 或基本单位的数值
     * @throws InputException
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
     * @param v
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
