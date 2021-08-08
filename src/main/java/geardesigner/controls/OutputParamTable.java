package geardesigner.controls;

import geardesigner.Log;
import geardesigner.beans.Decimal;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * 输入参数的数据面板
 * @author SUPERSTATION
 */
public class OutputParamTable extends ParamTable {
    OutputParamTable(final String paneName, final String[] colName, final Parameter[] pcs) throws IOException {
        super(paneName, colName, pcs);
    }

    /**
     * 创建输出数据面板的静态方法
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
                    pcs[i] = new OutputParameter(pNameAndUnit[i][0], pNameAndUnit[i][1]);
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
    void initStyle() {
        super.initStyle();
        /**
         * 设置列宽的默认值
         */
        setCol0Width(100);
        setCol1Width(130);
        setCol2Width(50);
    }

    /**
     * 用于批量设置数据面板的显示值
     * 关闭报错
     * @param values
     */
    public void setValues(Map<String, Decimal> values) {
        if (values!=null) {
            final Set<Map.Entry<String, Decimal>> es = values.entrySet();
            for (Map.Entry<String, Decimal> e:es){
                final Parameter p = table.get(e.getKey());
                if (p!=null){
                    p.setValue(e.getValue());
                }else {
                    Log.warning("未定义的参数");
                }
            }
        }
    }
}
