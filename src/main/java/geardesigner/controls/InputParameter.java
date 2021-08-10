package geardesigner.controls;

import geardesigner.beans.Decimal;
import javafx.scene.control.TextField;
import org.jetbrains.annotations.Nullable;

/**
 * 输入型参数的UI控件
 *
 * @author SUPERSTATION
 */
public class InputParameter extends Parameter {
    private final TextField field;

    //待办 2021/8/10: 输入过滤器

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
     * @return
     */
    @Override
    public @Nullable Decimal getValue() {
        final String vs = field.getText().trim();
        return vs.isBlank() ? null : Decimal.valueOf(vs);
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
}
