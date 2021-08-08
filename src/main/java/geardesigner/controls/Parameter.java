package geardesigner.controls;

import geardesigner.beans.Decimal;
import javafx.beans.property.Property;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;


// TODO: 2020/3/20 可输入框与不可输入框视觉区分

/**
 * 参数的UI控件基类
 *
 * @author SUPERSTATION
 */
public abstract class Parameter extends HBox {
    final Label name;
    final StackPane valuePane;
    final StackPane symbolPane;
    private final ImageView symbol;
    private TexFormula unit;

    public Parameter(String name) {
        this(name, null);
    }

    /**
     * @param name
     * @param unit 应为Latex字符串
     */
    public Parameter(String name, String unit) {
        this.name = new Label();
        this.name.setText(name);

        this.valuePane = new StackPane();
        this.symbolPane = new StackPane();
        symbol = new ImageView();
        if (unit != null) {
            this.unit = new TexFormula(unit);
        }
        initStyle();
    }

    /**
     * 派生类应重写该方法，修改组件的布局
     */
    void initStyle() {
        this.setAlignment(Pos.BOTTOM_LEFT);
        symbolPane.getChildren().add(symbol);
        /**
         * 预设尺寸
         */
        //name.setPrefSize(100,25);
        valuePane.setAlignment(Pos.BOTTOM_LEFT);
        symbolPane.setAlignment(Pos.BOTTOM_LEFT);
        refresh();
    }

    /**
     * UI控件显示刷新
     */
    public void refresh() {
        if (unit != null) {
            symbol.setImage(unit.getPatternImage());
        }
    }


    public final String getName() {
        return name.getText().trim();
    }

    /**
     * 修改单位（不刷新）
     *
     * @param unit
     */
    public final void setUnit(String unit) {
        //待办 2021/8/5: 修改为绑定同步刷新的
        this.unit = new TexFormula(unit);
    }

    public final String getUnit() {
        return unit == null ? null : unit.getFormula();
    }

    /**
     * @return
     */
    public abstract Decimal getValue();

    /**
     * @param v
     */
    public abstract void setValue(Decimal v);

    public final void bindNamePreWidthProperty(Property<Number> width) {
        name.prefWidthProperty().bindBidirectional(width);
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
