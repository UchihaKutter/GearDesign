package geardesigner.controls;

import geardesigner.Config;
import geardesigner.units.ConvertibleUnit;
import javafx.beans.property.IntegerProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * 输入参数的数据面板
 *
 * @author SUPERSTATION
 */
public class OutputParamTable extends ParamTable<OutputParameter> {
    private static final Number INIT_COL0_WIDTH = Config.get("OutputParamTable.INIT_COL0_WIDTH");
    private static final Number INIT_COL1_WIDTH = Config.get("OutputParamTable.INIT_COL1_WIDTH");
    private static final Number INIT_COL2_WIDTH = Config.get("OutputParamTable.INIT_COL2_WIDTH");
    private static final Number INIT_ROW_HEIGHT = Config.get("OutputParamTable.INIT_ROW_HEIGHT");
    private IntegerProperty digit;

    OutputParamTable(final String paneName, final String[] colName, final OutputParameter[] pcs) throws IOException {
        super(paneName, colName, pcs);
    }

    /**
     * 创建输出数据面板的静态方法
     *
     * @param paneName
     * @param colName
     * @param pNameAndUnit 参数名和单位的数组，pNameAndUnit[i][0]必须是String，pNameAndUnit[i][1]必须是BaseUnit
     * @return
     * @throws IOException
     */
    public static OutputParamTable createTable(final String paneName, final String[] colName,
                                               Object[][] pNameAndUnit) throws IOException {
        if (pNameAndUnit != null && pNameAndUnit.length > 0) {
            final OutputParameter[] pcs = new OutputParameter[pNameAndUnit.length];
            for (int i = 0; i < pNameAndUnit.length; i++) {
                if (pNameAndUnit[i] != null && pNameAndUnit[i].length == 2) {
                    final OutputParameter pc = new OutputParameter((String) pNameAndUnit[i][0], (ConvertibleUnit) pNameAndUnit[i][1]);
                    pcs[i] = pc;
                }
            }
            return new OutputParamTable(paneName, colName, pcs);
        } else {
            throw new RuntimeException("虚拟机故障，无法解析可变参数列表");
        }
    }

    @Override
    final void rowSizeBinding(final OutputParameter p) {
        if (p != null) {
            p.bindNamePreWidthProperty(Col0Width);
            p.bindValuePreWidthProperty(Col1Width);
            p.bindSymbolPreWidthProperty(Col2Width);
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
        /**
         * 设置行高
         */
        RowHeight.setValue(INIT_ROW_HEIGHT);
    }

    /**
     * 为面板上所有Parameter控件绑定小数位数属性
     *
     * @param ip null->清除绑定
     */
    public void bindDigitProperty(@Nullable IntegerProperty ip) {
        final Set<Map.Entry<String, OutputParameter>> pcs = table.entrySet();
        if (ip == null) {
            if (digit == null) {
                return;
            }
            for (Map.Entry<String, OutputParameter> ep : pcs) {
                (ep.getValue()).unbindDigitProperty(digit);
            }
        } else {
            if (digit == null) {
                for (Map.Entry<String, OutputParameter> ep : pcs) {
                    (ep.getValue()).bindDigitProperty(ip);
                }

            } else {
                for (Map.Entry<String, OutputParameter> ep : pcs) {
                    final OutputParameter p = ep.getValue();
                    p.unbindDigitProperty(digit);
                    p.bindDigitProperty(ip);
                }
            }
        }
        digit = ip;
    }

    /**
     * 批量切换参数值度量单位
     *
     * @param oldUnit ConvertibleUnit
     * @param newUnit ConvertibleUnit
     */
    public void changeUnits(@NotNull final ConvertibleUnit oldUnit, @NotNull final ConvertibleUnit newUnit) {
        if (oldUnit.getClass().isInstance(newUnit)) {
            table.entrySet().parallelStream().filter(sp -> sp.getValue().getUnit() == oldUnit).forEach(sp -> sp.getValue().setUnit(newUnit));
        } else {
            throw new ClassCastException("不同类型的度量单位不能相互转换");
        }
    }
}
