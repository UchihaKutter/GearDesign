package geardesigner.controls;

import javafx.beans.property.Property;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;


// TODO: 2020/3/19 JLatexMath符号功能
// TODO: 2020/3/20 可输入框与不可输入框视觉区分

/**
 * 参数的UI控件基类
 *
 * @author SUPERSTATION
 */
public abstract class Parameter extends HBox {
    final Label name;
    final StackPane valuePane;
    private final Text value;
    final StackPane symbolPane;
    private final ImageView symbol;
    TexFormula unit;

    public Parameter() {
        this.name = new Label();
        this.valuePane = new StackPane();
        value = new Text();
        this.symbolPane = new StackPane();
        symbol = new ImageView();
        valuePane.getChildren().add(value);
        symbolPane.getChildren().add(symbol);
    }

    public Parameter(String name) {
        this();
        this.name.setText(name);
        initStyle();
    }

    /**
     * @param name
     * @param unit 应为Latex字符串
     */
    public Parameter(String name, String unit) {
        this();
        this.name.setText(name);
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

    public final String getValue() {
        return value.getText().trim();
    }

    public final void setValue(String text) {
        value.setText(text);
    }

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
    public final int hashCode() {
        int result = name.getText().hashCode();
        result = 31 * result + value.hashCode();
        result = 31 * result + symbol.hashCode();
        return result;
    }
}
