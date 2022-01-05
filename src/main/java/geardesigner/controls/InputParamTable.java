package geardesigner.controls;

import geardesigner.beans.Changeable;
import geardesigner.Config;
import geardesigner.Log;
import geardesigner.beans.CodeException;
import geardesigner.units.ConvertibleUnit;
import javafx.scene.control.TextFormatter;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Consumer;

/**
 * 输入参数的数据面板
 *
 * @author SUPERSTATION
 */
public class InputParamTable extends ParamTable<InputParameter> implements Changeable {
    private static final Number INIT_COL0_WIDTH = Config.get("InputParamTable.INIT_COL0_WIDTH");
    private static final Number INIT_COL1_WIDTH = Config.get("InputParamTable.INIT_COL1_WIDTH");
    private static final Number INIT_COL2_WIDTH = Config.get("InputParamTable.INIT_COL2_WIDTH");

    InputParamTable(final String paneName, final String[] colName, final InputParameter[] pcs) throws IOException {
        super(paneName, colName, pcs);
    }

    /**
     * 创建输入数据面板的静态方法
     *
     * @param paneName
     * @param colName
     * @param pNameAndUnit 参数名和单位的pair数组，pNameAndUnit[i][0]必须是String，pNameAndUnit[i][1]必须是BaseUnit
     * @return 新的InputParamTable实例
     * @throws IOException ParamTable.fxml读取错误
     */
    public static InputParamTable createTable(final String paneName, final String[] colName, Object[][] pNameAndUnit) throws IOException {
        if (pNameAndUnit != null && pNameAndUnit.length > 0) {
            final InputParameter[] pcs = new InputParameter[pNameAndUnit.length];
            for (int i = 0; i < pNameAndUnit.length; i++) {
                if (pNameAndUnit[i] != null && pNameAndUnit[i].length == 2) {
                    pcs[i] = new InputParameter((String) pNameAndUnit[i][0], (ConvertibleUnit) pNameAndUnit[i][1]);
                }
            }
            return new InputParamTable(paneName, colName, pcs);
        } else {
            throw new RuntimeException("虚拟机故障，无法解析可变参数列表");
        }
    }

    @Override
    final void rowSizeBinding(final InputParameter p) {
        if (p != null) {
            p.bindNamePreWidthProperty(Col0Width);
            p.bindSymbolPreWidthProperty(Col1Width);
            p.bindValuePreWidthProperty(Col2Width);
            p.bindRowPreHeightProperty(RowHeight);
        }
    }

    @Override
    void initLayout() {
        /**
         * 设置列宽
         */
        Col0Width.setValue(INIT_COL0_WIDTH);
        Col1Width.setValue(INIT_COL1_WIDTH);
        Col2Width.setValue(INIT_COL2_WIDTH);
    }

    /**
     * 设置文本输入框中预置文本
     *
     * @param paramName  参数控件的名称
     * @param promptText 预设文本
     */
    public void setPromptText(String paramName, String promptText) throws CodeException {
        final InputParameter parameter = table.get(paramName);
        if (parameter == null) {
            throw new CodeException("指定的参数名不存在");
        }
        parameter.setPromptText(promptText);
    }

    /**
     * 设置某一个参数输入框的TextFormatter
     *
     * @param paramName 参数控件名
     * @param formatter TextFormatter
     * @throws CodeException 编码错误
     */
    public void setTextFormatter(String paramName, TextFormatter<Double> formatter) throws CodeException {
        final InputParameter parameter = table.get(paramName);
        if (parameter == null) {
            throw new CodeException("指定的参数名不存在");
        }
    }


    /**
     * 为所有文本框设置同种TextFormatter
     *
     * @param cls TextFormatter的Class
     * @throws NoSuchMethodException 编码错误
     */
    public void setTextFormatters(Class<? extends TextFormatter> cls) throws NoSuchMethodException {
        if (cls != null) {
            final Constructor<? extends TextFormatter> constructor = cls.getDeclaredConstructor();
            table.values().parallelStream().forEach(p -> {
                try {
                    p.setTextFormatter(constructor.newInstance());
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    Log.error(e);
                }
            });
        }
    }

    /**
     * 设置发生变化时的处理器
     *
     * @param consumer
     */
    @Override
    public void setOnChanged(Consumer<Object> consumer) {
        table.values().parallelStream().forEach(inputParameter -> inputParameter.setOnChanged(consumer));
    }
}
