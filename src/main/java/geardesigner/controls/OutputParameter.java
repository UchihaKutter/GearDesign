package geardesigner.controls;

import geardesigner.beans.Decimal;
import geardesigner.units.BaseUnit;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author SuperNote
 */

public class OutputParameter extends Parameter {
    /**
     * 常量配置
     */
    public static final int DEFAULT_SCALE_DIGIT = 3;

    private final Text field;
    private final IntegerProperty digit;
    private Decimal value;

    public OutputParameter(String name, Decimal value, BaseUnit unit) {
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

    public OutputParameter(String name, BaseUnit unit) {
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
        field.setText(value == null ? null : value.toString(digit.intValue()));
    }

    //待办 2021/8/9:
    @Override
    public @Nullable Decimal getValue() {
        return value;
    }

    @Override
    public void setValue(@Nullable Decimal v) {
        value = v;
        changed();
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
