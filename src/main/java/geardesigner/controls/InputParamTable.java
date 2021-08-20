package geardesigner.controls;

import geardesigner.CodeException;
import geardesigner.Log;
import geardesigner.beans.Decimal;
import javafx.scene.control.TextFormatter;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * 输入参数的数据面板
 *
 * @author SUPERSTATION
 */
public class InputParamTable extends ParamTable {
    InputParamTable(final String paneName, final String[] colName, final Parameter[] pcs) throws IOException {
        super(paneName, colName, pcs);
    }

    /**
     * 创建输入数据面板的静态方法
     *
     * @param paneName
     * @param colName
     * @param pNameAndUnit
     * @return
     * @throws IOException
     */
    public static InputParamTable createTable(final String paneName, final String[] colName, String[][] pNameAndUnit) throws IOException {
        if (pNameAndUnit != null && pNameAndUnit.length > 0) {
            final Parameter[] pcs = new Parameter[pNameAndUnit.length];
            for (int i = 0; i < pNameAndUnit.length; i++) {
                if (pNameAndUnit[i] != null && pNameAndUnit[i].length == 2) {
                    pcs[i] = new InputParameter(pNameAndUnit[i][0], pNameAndUnit[i][1]);
                }
            }
            return new InputParamTable(paneName, colName, pcs);
        } else {
            throw new RuntimeException("虚拟机故障，无法解析可变参数列表");
        }
    }

    @Override
    final void rowSizeBinding(final Parameter p) {
        if (p != null) {
            p.bindNamePreWidthProperty(Col0Width);
            p.bindSymbolPreWidthProperty(Col1Width);
            p.bindValuePreWidthProperty(Col2Width);
        }
    }

    @Override
    void initStyle() {
        /**
         * 设置列宽的默认值
         */
        setCol0Width(100);
        setCol1Width(50);
        setCol2Width(130);
    }

    /**
     * 设置文本输入框中预置文本
     *
     * @param paramName  参数控件的名称
     * @param promptText 预设文本
     */
    public void setPromptText(String paramName, String promptText) throws CodeException {
        final Parameter parameter = table.get(paramName);
        if (parameter == null) {
            throw new CodeException("指定的参数名不存在");
        }
        if (parameter instanceof InputParameter) {
            ((InputParameter) parameter).setPromptText(promptText);
        } else {
            throw new CodeException("Parameter控件不是预期的类型");
        }
    }

    /**
     * 设置某一个参数输入框的TextFormatter
     *
     * @param paramName 参数控件名
     * @param formatter TextFormatter
     * @throws CodeException
     */
    public void setTextFormatter(String paramName, TextFormatter<Decimal> formatter) throws CodeException {
        final Parameter parameter = table.get(paramName);
        if (parameter == null) {
            throw new CodeException("指定的参数名不存在");
        }
        if (parameter instanceof InputParameter) {
            ((InputParameter) parameter).setTextFormatter(formatter);
        } else {
            throw new CodeException("Parameter控件不是预期的类型");
        }
    }


    /**
     * 为所有文本框设置同种TextFormatter
     *
     * @param cls
     * @throws NoSuchMethodException
     */
    public void setTextFormatters(Class<? extends TextFormatter> cls) throws NoSuchMethodException {
        if (cls != null) {
            final Constructor<? extends TextFormatter> constructor = cls.getDeclaredConstructor();
            table.values().parallelStream().forEach(p -> {
                try {
                    ((InputParameter) p).setTextFormatter(constructor.newInstance());
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    Log.error(e);
                }
            });
        }
    }
}
