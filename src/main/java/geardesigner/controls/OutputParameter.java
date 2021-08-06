package geardesigner.controls;

import geardesigner.beans.Decimal;
import geardesigner.beans.DecimalProperty;
import javafx.scene.text.Text;

/**
 * @author SuperNote
 */

public class OutputParameter extends Parameter {

    private final Text value;
    final DecimalProperty valueProperty;

    public OutputParameter(String name, Decimal value, String unit) {
        super(name, unit);
        valueProperty=new DecimalProperty(name,value);
        this.value = (value == null) ? new Text() : new Text(value.toString());
        valuePane.getChildren().add(this.value);
    }

    public OutputParameter(String name) {
        this(name, null, null);
    }

    public OutputParameter(String name, String unit) {
        this(name, null, unit);
    }

    @Override
    void initStyle() {
        super.initStyle();
        this.getChildren().addAll(name, valuePane, symbolPane);
    }

    @Override
    public Decimal getValue() {
        return new Decimal(valueProperty.get());
    }

    //待办 2021/8/6: 同步修改功能
    @Override
    public void setValue(Decimal v) {
        value.setText(v.toString());
    }
}
