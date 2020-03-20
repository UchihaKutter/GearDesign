package geardesigner.controls;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.NumberValidator;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * @author SuperNote
 */
// TODO: 2020/3/19 JLatexMath符号功能
// TODO: 2020/3/20 可输入框与不可输入框视觉区分
public class GearParameter extends HBox {
    private final Label name;
    private final String symbol;
    private final JFXTextField value;//TextField

    public GearParameter(String name, String symbol, boolean editable) {
        this.name = new Label(name);
        this.symbol = symbol;
        value = new JFXTextField();
        value.setEditable(editable);
        value.setValidators(new NumberValidator("请输入有效的数值"));
        this.getChildren().addAll(this.name, new Label(symbol), value);
    }

    public boolean isEditable() {
        return value.isEditable();
    }

    public final String getValue() {
        return value.getText().trim();
    }

    public final void setValue(String text) {
        value.setText(text);
    }

    @Override
    public int hashCode() {
        int result = name.getText().hashCode();
        result = 31 * result + symbol.hashCode();
        return result;
    }
}
