package geardesigner.controls;

import geardesigner.beans.Decimal;
import geardesigner.beans.DecimalProperty;
import javafx.scene.control.TextField;
import org.jetbrains.annotations.Nullable;

/**
 * 输入型参数的UI控件
 *
 * @author SUPERSTATION
 */
public class InputParameter extends Parameter {
    private final TextField field;
    final DecimalProperty valueProperty;

    public InputParameter(String name, Decimal initialValue, String unit) {
        super(name, unit);
        valueProperty = new DecimalProperty(name, initialValue);
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
