package geardesigner.controls;

import javafx.beans.property.IntegerProperty;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * 输入参数的数据面板
 *
 * @author SUPERSTATION
 */
public class OutputParamTable extends ParamTable {
    private IntegerProperty digit;

    OutputParamTable(final String paneName, final String[] colName, final Parameter[] pcs) throws IOException {
        super(paneName, colName, pcs);
    }

    /**
     * 创建输出数据面板的静态方法
     *
     * @param paneName
     * @param colName
     * @param pNameAndUnit
     * @return
     * @throws IOException
     */
    public static OutputParamTable createTable(final String paneName, final String[] colName,
                                               String[][] pNameAndUnit) throws IOException {
        if (pNameAndUnit != null && pNameAndUnit.length > 0) {
            final Parameter[] pcs = new Parameter[pNameAndUnit.length];
            for (int i = 0; i < pNameAndUnit.length; i++) {
                if (pNameAndUnit[i] != null && pNameAndUnit[i].length == 2) {
                    final OutputParameter pc = new OutputParameter(pNameAndUnit[i][0], pNameAndUnit[i][1]);
                    pcs[i] = pc;
                }
            }
            return new OutputParamTable(paneName, colName, pcs);
        } else {
            throw new RuntimeException("虚拟机故障，无法解析可变参数列表");
        }
    }

    @Override
    final void rowSizeBinding(final Parameter p) {
        if (p != null) {
            p.bindNamePreWidthProperty(Col0Width);
            p.bindValuePreWidthProperty(Col1Width);
            p.bindSymbolPreWidthProperty(Col2Width);
        }
    }

    @Override
    void initLayout() {
        /**
         * 设置列宽
         */
        Col0Width.setValue(120);
        Col1Width.setValue(120);
        Col2Width.setValue(60);
    }

    /**
     * 为面板上所有Parameter控件绑定小数位数属性
     *
     * @param ip null->清除绑定
     */
    public void bindDigitProperty(@Nullable IntegerProperty ip) {
        final Set<Map.Entry<String, Parameter>> pcs = table.entrySet();
        if (ip == null) {
            if (digit == null) {
                return;
            }
            for (Map.Entry<String, Parameter> ep : pcs) {
                ((OutputParameter) ep.getValue()).unbindDigitProperty(digit);
            }
        } else {
            if (digit == null) {
                for (Map.Entry<String, Parameter> ep : pcs) {
                    ((OutputParameter) ep.getValue()).bindDigitProperty(ip);
                }

            } else {
                for (Map.Entry<String, Parameter> ep : pcs) {
                    final OutputParameter p = (OutputParameter) ep.getValue();
                    p.unbindDigitProperty(digit);
                    p.bindDigitProperty(ip);
                }
            }
        }
        digit = ip;
    }
}
