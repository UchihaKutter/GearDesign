package geardesigner.controls;

import geardesigner.InputException;
import geardesigner.beans.Decimal;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import org.jetbrains.annotations.Nullable;

/**
 * 输入型参数的UI控件
 *
 * @author SUPERSTATION
 */
public class InputParameter extends Parameter {
    private final TextField field;

    public InputParameter(String name, Decimal initialValue, String unit) {
        super(name, unit);
        this.field = (initialValue == null) ? new TextField() : new TextField(initialValue.toString());
        valuePane.getChildren().add(this.field);
    }

    public InputParameter(String name) {
        this(name, null, null);
    }

    public InputParameter(String name, String unit) {
        this(name, null, unit);
    }

    @Override
    void initStyle() {
        super.initStyle();
        this.getChildren().addAll(namePane, symbolPane, valuePane);
    }

    private void initListener() {

    }

    /**
     * @return null 或数值对象，
     * @throws
     */
    @Override
    public @Nullable Decimal getValue() throws InputException {
        final String vs = field.getText().trim();
        if (vs.isBlank()) {
            return null;
        } else {
            /**
             * 捕获输入不合法错误
             */
            try {
                return Decimal.valueOf(vs);
            } catch (NumberFormatException e) {
                throw new InputException("请输入正确的" + getName());
            }
        }
    }

    /**
     * @param v
     */
    @Override
    public void setValue(final @Nullable Decimal v) {
        field.setText(v == null ? null : String.valueOf(v.doubleValue()));
    }

    public void setPromptText(@Nullable final String str) {
        field.setPromptText(str);
    }

    /**
     * 设置输入的格式化器
     *
     * @param formatter 不能和其他输入框共用格式化器实例
     */
    public void setTextFormatter(TextFormatter<Decimal> formatter) {
        field.setTextFormatter(formatter);
    }
}
