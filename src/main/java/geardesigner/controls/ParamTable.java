package geardesigner.controls;

import geardesigner.Log;
import geardesigner.beans.Decimal;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author SuperNote
 */
public abstract class ParamTable extends Pane {
    final ObservableMap<String, Parameter> table;
    final Property<Number> Col0Width;
    final Property<Number> Col1Width;
    final Property<Number> Col2Width;

    @FXML
    private VBox vBox;

    @FXML
    private Text tTitle;

    @FXML
    private Label lCol0;

    @FXML
    private Label lCol1;

    @FXML
    private Label lCol2;

    ParamTable(String paneName, String[] colName, Parameter[] pcs) throws IOException {
        super();
        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ParamTable.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        fxmlLoader.load();
        /**
         * 初始化宽度可变对象
         */
        Col0Width = new SimpleDoubleProperty();
        Col1Width = new SimpleDoubleProperty();
        Col2Width = new SimpleDoubleProperty();
        table = FXCollections.observableMap(new HashMap<>(pcs.length));
        setBinds();
        setNames(paneName, colName);
        addParams(pcs);
        initStyle();
    }

    /**
     * 设置数据面板的列名
     *
     * @param paneName 数据面板的名称
     * @param colName  列名数组，前3个传入值有效
     */
    private void setNames(String paneName, String[] colName) {
        tTitle.setText(paneName);
        if (colName != null && colName.length > 0) {
            switch (colName.length) {
                case 3:
                    lCol2.setText(colName[2]);
                case 2:
                    lCol1.setText(colName[1]);
                case 1:
                    lCol0.setText(colName[0]);
                    break;
                default:
                    Log.info("传入的列名过多");
            }
        }
    }

    private void setBinds() {
        lCol0.prefWidthProperty().bindBidirectional(Col0Width);
        lCol1.prefWidthProperty().bindBidirectional(Col1Width);
        lCol2.prefWidthProperty().bindBidirectional(Col2Width);
    }

    private void addParams(Parameter[] pcs) {
        if (pcs != null) {
            final ObservableList<Node> children = vBox.getChildren();
            for (Parameter p : pcs) {
                if (p != null) {
                    final String name = p.getName();
                    rowSizeBinding(p);
                    if (table.put(name, p) == null) {
                        children.add(p);
                    } else {
                        Log.info("参数名重复："+name);
                    }
                }
            }
        }
    }

    abstract void rowSizeBinding(Parameter p);

    /**
     * 重写该方法，以定义不同类型输入面板的初始样式。必须指定列宽的默认值
     */
    void initStyle() {
        /**
         * 设置面板边距
         */
        vBox.setPadding(new Insets(10, 10, 20, 10));
        setStyle("-fx-border-color:midnightblue;-fx-border-width:2;-fx-border-radius:8");
        /**
         * 设置列名的布局方式
         */
        lCol0.setAlignment(Pos.TOP_CENTER);
        lCol1.setAlignment(Pos.TOP_CENTER);
        lCol2.setAlignment(Pos.TOP_CENTER);
    }


    public Decimal getValue(String name) {
        Parameter parameter = table.get(name);
        if (parameter != null) {
            return parameter.getValue();
        }
        throw new IllegalArgumentException("没有这样的参数");
    }

    public ParamTable setValue(String name, Decimal value) {
        Parameter parameter = table.get(name);
        if (parameter != null) {
            parameter.setValue(value);
            return this;
        }
        throw new IllegalArgumentException("没有这样的参数");
    }

    public void setCol0Width(double width) {
        Col0Width.setValue(width);
    }

    public void setCol1Width(double width) {
        Col1Width.setValue(width);
    }

    public void setCol2Width(double width) {
        Col2Width.setValue(width);
    }


}
