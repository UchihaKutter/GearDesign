package geardesigner.controls;

import geardesigner.units.ConvertibleUnit;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.ImageView;
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

    private final ImageView symbol;
    private final Text field;
    private final IntegerProperty digit;
    /**
     * 内部存储的数值，采用基本度量单位
     */
    private Number value;

    public OutputParameter(String name, Double value, U unit) {
        super(name, unit);
        this.symbol = new ImageView();
        this.field = new Text();
        this.value = value;
        this.digit = new SimpleIntegerProperty(DEFAULT_SCALE_DIGIT);

        symbolPane.getChildren().add(this.symbol);
        valuePane.getChildren().add(this.field);
        initStyle();
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
        this.getChildren().addAll(namePane, valuePane, symbolPane);
        refresh();
    }

    /**
     * 重写refresh，根据单位的改变，转换参数框中的显示数值
     */
    @Override
    void refresh() {
        if (unit != null) {
            symbol.setImage(new TexFormula(unit.getDisplay()).getPatternImage());
        }
        changed();
    }

    /**
     * 初始化监听器
     */
    private void initListener() {
        digit.addListener((observable, oldValue, newValue) -> this.changed());
    }

    private void changed() {
        Number physicalValue = getPhysicalValue();
        if (physicalValue != null && digit != null) {
            field.setText(DecimalFormatter.toString(physicalValue, digit.intValue()));
        }
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

    /**
     * @param v 为 Parameter 面板实例设定新的值，应为基本单位值
     */
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
    @Nullable
    public Number getPhysicalValue() {
        if (value == null) {
            return null;
        } else {
            return unit == null ? value : unit.toCurrentUnit(value.doubleValue());
        }
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
