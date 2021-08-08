package geardesigner.controls;

import geardesigner.beans.Decimal;
import geardesigner.beans.DecimalProperty;
import javafx.scene.control.TextField;

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
        this.getChildren().addAll(name, symbolPane, valuePane);
    }

    /**
     * @return
     */
    @Override
    public Decimal getValue() {
        return Decimal.valueOf(field.getText());
    }

    /**
     * @param v
     */
    @Override
    public void setValue(final Decimal v) {
        field.setText(String.valueOf(v.doubleValue()));
    }

    public void setPromptText(final String str){
        field.setPromptText(str);
    }
}
