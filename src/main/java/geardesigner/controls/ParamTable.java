package geardesigner.controls;

import geardesigner.CodeException;
import geardesigner.Log;
import geardesigner.beans.Decimal;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author SuperNote
 */
public abstract class ParamTable extends StackPane {
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
                        Log.info("参数名重复：" + name);
                    }
                }
            }
        }
    }


    /**
     * 为每一个加入表格的{@code Parameter}实例绑定组件宽度
     *
     * @param p 待绑定的Parameter实例
     */
    abstract void rowSizeBinding(Parameter p);

    /**
     * 重写该方法，以定义不同类型面板的排版样式。必须指定列宽的默认值
     */
    abstract void initStyle();


    public Decimal getValue(String name) throws CodeException {
        Parameter parameter = table.get(name);
        if (parameter != null) {
            return parameter.getValue();
        }
        throw new CodeException("没有这样的参数");
    }

    public ParamTable setValue(String name, Decimal value) throws CodeException {
        Parameter parameter = table.get(name);
        if (parameter != null) {
            parameter.setValue(value);
            return this;
        }
        throw new CodeException("没有这样的参数");
    }

    /**
     * 批量获取数据面板当前显示值。值可能不存在
     * @return
     */
    public Map<String,Decimal> getValues(){
        final Set<Map.Entry<String, Parameter>> params = table.entrySet();
        final Map<String, Decimal> cValues = new HashMap<>(params.size());
        for (Map.Entry<String,Parameter> p:params){
            cValues.put(p.getKey(),p.getValue().getValue());
        }
        return cValues;
    }
    /**
     * 批量设置数据面板的显示值
     * 关闭报错
     *
     * @param values 新显示值的键值对
     */
    public void setValues(Map<String, Decimal> values) throws CodeException {
        if (values != null) {
            final Set<Map.Entry<String, Decimal>> es = values.entrySet();
            for (Map.Entry<String, Decimal> e : es) {
                final Parameter p = table.get(e.getKey());
                if (p != null) {
                    p.setValue(e.getValue());
                } else {
                    throw new CodeException("未定义的参数");
                }
            }
        }
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
