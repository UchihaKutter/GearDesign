package geardesigner.controls;

/**
 * @author SuperNote
 */

public class OutputParameter extends Parameter {

    public OutputParameter(String name, String symbol) {
        super(name, symbol);
    }

    @Override
    void initStyle() {
        super.initStyle();
        this.getChildren().addAll(name, valuePane, symbolPane);
    }
}
