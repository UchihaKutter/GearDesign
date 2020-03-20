package geardesigner.controls;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ParameterTable extends VBox {
    private final ObservableMap<String,GearParameter> table;
    private static final HBox header=new HBox(new Label("参数名"),new Label("参数符号"),new Label("参数值"));

    public ParameterTable(String[] names,String[] symbols,boolean[] editable) {
        if (names.length==symbols.length&&symbols.length==editable.length) {
            ObservableList<Node> children=this.getChildren();
            children.add(header);
            table = FXCollections.observableHashMap();
            for (int i = 0; i < names.length; i++) {
                GearParameter parameter=new GearParameter(names[i],symbols[i],editable[i]);
                // TODO: 2020/3/19 设置宽度bind
                if (table.put(names[i],parameter)!=null){
                    throw new IllegalArgumentException("参数名重复");
                }
                children.add(parameter);
            }
        }else {
            throw new IllegalArgumentException("集合创建失败");
        }
    }

    public String getValue(String name){
        GearParameter parameter= table.get(name);
        if (parameter!=null){
            return parameter.getValue();
        }
        throw new IllegalArgumentException("没有这样的参数");
    }

    public ParameterTable setValue(String name,String value){
        GearParameter parameter= table.get(name);
        if (parameter!=null){
            parameter.setValue(value);
            return this;
        }
        throw new IllegalArgumentException("没有这样的参数");
    }
}
