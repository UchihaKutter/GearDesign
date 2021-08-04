package geardesigner.controls;

import javafx.beans.property.Property;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import org.jetbrains.annotations.Nullable;

/**
 * @author SuperNote
 */
// TODO: 2020/3/19 JLatexMath符号功能
// TODO: 2020/3/20 可输入框与不可输入框视觉区分
// TODO: 2020/3/20 输入监视器
public class GearParameter extends HBox {
    private final Label name;
    private final StackPane symbol;
    private final TextField value;
    private String unit;

    public GearParameter(String name, String symbol, boolean editable) {
        this.name = new Label(name);
        this.symbol = new StackPane(new Label(symbol));
        value = new TextField();
        value.setEditable(editable);
        this.getChildren().addAll(this.name, this.symbol, value);
        initStyle();
    }

    private void initStyle() {
        this.setAlignment(Pos.BOTTOM_LEFT);
        symbol.setAlignment(Pos.BOTTOM_LEFT);
    }

    public final boolean isEditable() {
        return value.isEditable();
    }

    public final String getValue() {
        return value.getText().trim();
    }

    public final void setValue(String text) {
        value.setText(text);
    }

    @Nullable
    public final String getUnit() {
        return unit;
    }

    public final void setUnit(String unit) {
        this.unit = unit;
    }

    public final void bindNamePreWidthProperty(Property<Number> width) {
        name.prefWidthProperty().bindBidirectional(width);
    }

    public final void bindSymbolPreWidthProperty(Property<Number> width) {
        symbol.prefWidthProperty().bindBidirectional(width);
    }

    public final void bindValuePreWidthProperty(Property<Number> width) {
        value.prefWidthProperty().bindBidirectional(width);
    }

    @Override
    public final int hashCode() {
        int result = name.getText().hashCode();
        result = 31 * result + symbol.hashCode();
        return result;
    }
}
