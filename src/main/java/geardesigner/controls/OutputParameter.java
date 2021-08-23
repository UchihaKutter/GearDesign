package geardesigner.controls;

import geardesigner.units.ConvertibleUnit;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author SuperNote
 */

public class OutputParameter<U extends ConvertibleUnit> extends Parameter {
    /**
     * 常量配置
     */
    public static final int DEFAULT_SCALE_DIGIT = 3;

    private final Text field;
    private final IntegerProperty digit;
    private Number value;

    public OutputParameter(String name, Double value, U unit) {
        super(name, unit);
        this.field = new Text();
        this.value = value;
        this.digit = new SimpleIntegerProperty(DEFAULT_SCALE_DIGIT);
        valuePane.getChildren().add(this.field);
        initListener();
        changed();
    }

    public OutputParameter(String name) {
        this(name, null, null);
    }

    public OutputParameter(String name, U unit) {
        this(name, null, unit);
    }

    @Override
    void initStyle() {
        super.initStyle();
        this.getChildren().addAll(namePane, valuePane, symbolPane);
    }

    /**
     * 初始化监听器
     */
    private void initListener() {
        digit.addListener((observable, oldValue, newValue) -> this.changed());
    }

    private void changed() {
        field.setText(value == null ? null : DecimalFormatter.toString(value, digit.intValue()));
    }

    /**
     * 获取数值
     *
     * @return 当前参数框的基本单位值
     */
    @Override
    public @Nullable Number getValue() {
        return value;
    }

    @Override
    public void setValue(@Nullable Number v) {
        value = v;
        changed();
    }

    /**
     * 获取当前参数框的物理值
     *
     * @return
     */
    public Number getPhysicalValue() {
        final double v = value.doubleValue();
        return unit == null ? v : unit.toCurrentUnit(v);
    }

    public int getDigit() {
        return digit.get();
    }

    public void setDigit(int newDigit) {
        digit.set(newDigit);
    }

    public void bindDigitProperty(@NotNull Property<Number> digit) {
        this.digit.bindBidirectional(digit);
    }

    public void unbindDigitProperty(@NotNull Property<Number> digit) {
        this.digit.unbindBidirectional(digit);
    }
}
