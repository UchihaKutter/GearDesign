package geardesigner.controls;

import geardesigner.Config;
import geardesigner.beans.InputException;
import geardesigner.units.ConvertibleUnit;
import javafx.beans.property.Property;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * 参数的UI控件基类
 *
 * @author SUPERSTATION
 */
public abstract class Parameter<U extends ConvertibleUnit> extends HBox {
    private static final Double SPACING = Config.get("Parameter.SPACING");
    /**
     * Stylesheet Handling
     */
    private static final String DEFAULT_STYLE_CLASS = "Parameter";
    final StackPane namePane;
    final StackPane valuePane;
    final StackPane symbolPane;
    private final Text name;

    U unit;

    /**
     * @param name Parameter控件实例的名称
     * @param unit 应为Latex字符串
     */
    Parameter(String name, U unit) {
        this.name = new Text();
        this.name.setText(name);
        this.namePane = new StackPane(this.name);
        this.valuePane = new StackPane();
        this.symbolPane = new StackPane();
        if (unit != null) {
            this.unit = unit;
        }
        initCommonStyle();
    }

    private void initCommonStyle() {
        /**
         * 设定CSS类选择器名和ID选择器
         */
        getStyleClass().setAll(DEFAULT_STYLE_CLASS);
        name.setId("ParamName");
        /**
         * 预设布局
         */
        this.setAlignment(Pos.CENTER_LEFT);
        namePane.setAlignment(Pos.BOTTOM_LEFT);
        valuePane.setAlignment(Pos.BOTTOM_RIGHT);
        symbolPane.setAlignment(Pos.BOTTOM_LEFT);
        this.setSpacing(SPACING);
    }

    /**
     * 派生类应在构造方法赋值的末尾，调用该方法
     */
    abstract void initStyle();

    /**
     * UI控件显示刷新
     */
    abstract void refresh();

    public final String getName() {
        return name.getText().trim();
    }

    public final U getUnit() {
        return unit == null ? null : unit;
    }

    /**
     * 修改单位
     *
     * @param unit 可转换单位的枚举类型
     */
    public final void setUnit(@NotNull U unit) {
        /**
         * 先检查传入的Unit和已有Unit是否属于同一系列，否则拒绝修改
         */
        if (this.unit != null) {
            if (!this.unit.getClass().isInstance(unit)) {
                throw new ClassCastException("不同类型的度量单位不能相互转换");
            }
        }
        this.unit = unit;
        refresh();
    }

    /**
     * 获取Parameter的值。注意：系统内值的处理，总是使用基本单位
     *
     * @return 返回 Parameter 面板实例当前的基本单位值
     * @throws InputException 对输入框，可能会抛出输入不合法错误
     */
    @Nullable
    public abstract Number getValue() throws InputException;

    /**
     * 注意：系统内值的处理，转换为基本单位
     *
     * @param v 为 Parameter 面板实例设定新的值,应为基本单位值
     */
    abstract void setValue(@Nullable Number v);

    public final void bindNamePreWidthProperty(Property<Number> width) {
        namePane.prefWidthProperty().bindBidirectional(width);
    }

    public final void bindSymbolPreWidthProperty(Property<Number> width) {
        symbolPane.prefWidthProperty().bindBidirectional(width);
    }

    public final void bindValuePreWidthProperty(Property<Number> width) {
        valuePane.prefWidthProperty().bindBidirectional(width);
    }

    public final void bindRowPreHeightProperty(Property<Number> height) {
        this.prefHeightProperty().bind(height);
    }

    @Override
    public String toString() {
        return "Parameter{" + "name=" + name.getText() + '}';
    }
}
