package geardesigner.controls;

import geardesigner.InputException;
import geardesigner.beans.Decimal;
import geardesigner.units.BaseUnit;
import javafx.beans.property.Property;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
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
public abstract class Parameter extends HBox {
    /**
     * Stylesheet Handling
     */

    private static final String DEFAULT_STYLE_CLASS = "Parameter";
    final StackPane namePane;
    final StackPane valuePane;
    final StackPane symbolPane;
    private final Text name;
    private final ImageView symbol;
    private BaseUnit unit;

    public Parameter(String name) {
        this(name, null);
    }

    /**
     * @param name Parameter控件实例的名称
     * @param unit 应为Latex字符串
     */
    public Parameter(String name, BaseUnit unit) {
        this.name = new Text();
        this.name.setText(name);

        this.symbol = new ImageView();
        this.namePane = new StackPane(this.name);
        this.valuePane = new StackPane();
        this.symbolPane = new StackPane(this.symbol);
        if (unit != null) {
            this.unit = unit;
        }
        initStyle();
    }

    /**
     * 派生类应重写该方法，修改组件的布局
     */
    void initStyle() {
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
        refresh();
    }

    /**
     * UI控件显示刷新
     */
    public void refresh() {
        if (unit != null) {
            symbol.setImage(new TexFormula(unit.getDisplay()).getPatternImage());
        }
    }

    public final String getName() {
        return name.getText().trim();
    }

    public final BaseUnit getUnit() {
        return unit == null ? null : unit;
    }

    /**
     * 修改单位
     *
     * @param unit Latex格式字符串表示的数值单位
     */
    public final void setUnit(@NotNull BaseUnit unit) {
        //待办 2021/8/5: 修改为绑定同步刷新的
        this.unit = unit;
        refresh();
    }

    /**
     * @return 返回 Parameter 面板实例当前的值
     * @throws InputException 对输入框，可能会抛出输入不合法错误
     */
    @Nullable
    public abstract Decimal getValue() throws InputException;

    /**
     * @param v 为 Parameter 面板实例设定新的值
     */
    public abstract void setValue(@Nullable Decimal v);

    public final void bindNamePreWidthProperty(Property<Number> width) {
        namePane.prefWidthProperty().bindBidirectional(width);
    }

    public final void bindSymbolPreWidthProperty(Property<Number> width) {
        symbolPane.prefWidthProperty().bindBidirectional(width);
    }

    public final void bindValuePreWidthProperty(Property<Number> width) {
        valuePane.prefWidthProperty().bindBidirectional(width);
    }

    @Override
    public String toString() {
        return "Parameter{" + "name=" + name.getText() + '}';
    }
}
