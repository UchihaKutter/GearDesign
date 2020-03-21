package geardesigner.controls;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * @author SuperNote
 */
public class ParameterTable extends VBox {
    private final HBox header;
    private final ObservableMap<String, GearParameter> table;
    private final Property<Number> nameWidth;
    private final Property<Number> symbolWidth;
    private final Property<Number> valueWidth;

    private final StackPane nameLabel;
    private final StackPane symbolLabel;
    private final StackPane valueLabel;

    public ParameterTable(String[] names, String[] symbols, boolean[] editable) {
        /**
         * 带宽度默认值
         */
        nameWidth = new SimpleDoubleProperty(100);
        symbolWidth = new SimpleDoubleProperty(80);
        valueWidth = new SimpleDoubleProperty(100);
        nameLabel = new StackPane(new Label("参数名"));
        symbolLabel = new StackPane(new Label("参数符号"));
        valueLabel = new StackPane(new Label("参数值"));
        header = new HBox();
        initHeader();
        /**
         * 设置面板间距
         */
        setStyle(4, 20, 20, 30, 20);
        if (names.length == symbols.length && symbols.length == editable.length) {
            ObservableList<Node> children = this.getChildren();
            children.add(header);
            table = FXCollections.observableHashMap();
            for (int i = 0; i < names.length; i++) {
                GearParameter parameter = new GearParameter(names[i], symbols[i], editable[i]);
                parameter.bindNamePreWidthProperty(nameWidth);
                parameter.bindSymbolPreWidthProperty(symbolWidth);
                parameter.bindValuePreWidthProperty(valueWidth);
                if (table.put(names[i], parameter) != null) {
                    throw new IllegalArgumentException("参数名重复");
                }
                children.add(parameter);
            }
        } else {
            throw new IllegalArgumentException("集合创建失败");
        }
    }

    private void initHeader() {
        nameLabel.prefWidthProperty().bindBidirectional(nameWidth);
        symbolLabel.prefWidthProperty().bindBidirectional(symbolWidth);
        valueLabel.prefWidthProperty().bindBidirectional(valueWidth);
        nameLabel.setAlignment(Pos.TOP_CENTER);
        symbolLabel.setAlignment(Pos.TOP_CENTER);
        valueLabel.setAlignment(Pos.TOP_CENTER);
        header.getChildren().addAll(nameLabel, symbolLabel, valueLabel);
    }

    public void setStyle(double spacing, double top, double right, double bottom, double left) {
        setSpacing(spacing);
        setPadding(new Insets(top, right, bottom, left));
        setStyle("-fx-border-color:midnightblue;-fx-border-width:2;-fx-border-radius:8");
    }

    public String getValue(String name) {
        GearParameter parameter = table.get(name);
        if (parameter != null) {
            return parameter.getValue();
        }
        throw new IllegalArgumentException("没有这样的参数");
    }

    public ParameterTable setValue(String name, String value) {
        GearParameter parameter = table.get(name);
        if (parameter != null) {
            parameter.setValue(value);
            return this;
        }
        throw new IllegalArgumentException("没有这样的参数");
    }

    public void setNameWidth(double width) {
        nameWidth.setValue(width);
    }

    public void setSymbolWidth(double width) {
        symbolWidth.setValue(width);
    }

    public void setValueWidth(double width) {
        valueWidth.setValue(width);
    }
}
