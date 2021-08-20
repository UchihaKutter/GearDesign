package geardesigner;

/**
 * 存储表格设置常量
 *
 * @author SUPERSTATION
 */
public class TableSettings {
    /**
     * 输入参数数据列表设置
     */
    public static final String INPUT_PARAMS_PANE_NAME = "输入已知参数";
    public static final String[] INPUT_PARAMS_COLUMNS = new String[]{"参数名", "度量单位", "参数值"};
    public static final String[][] INPUT_PARAMS_NAME_UNIT
            = new String[][]{
            {"法向模数", null}, {"齿数(内齿为负)", null}, {"法向压力角", "°"},
            {"螺旋角", "°"}, {"法向变位系数", null}, {"齿顶高系数", null},
            {"齿根高系数", null}, {"顶隙系数", null}, {"量棒直径", "mm"},
            {"公法线上偏差", "mm"}, {"公法线下偏差", "mm"}, {"跨棒距上偏差", "mm"}, {"跨棒距下偏差", "mm"}};
    /**
     * 任一园计算结果展示数据面板设置
     */
    public static final String ANY_CIRCLE_PARAMS_PANE_NAME = null;
    public static final String[] ANY_CIRCLE_PARAMS_COLUMNS = new String[]{"参数名", "计算值", "单位"};
    public static final String[][] ANY_CIRCLE_PARAMS_NAME_UNIT
            = new String[][]{{"齿顶圆端面压力角", "°"}, {"分度圆处弧齿厚", "mm"}, {"任一圆处弧齿厚", "mm"}, {"任一园螺旋角", "°"}, {"任一圆处法向弦齿厚", "mm"}};
    /**
     * 公法线与跨帮距计算结果数据面板设置
     */
    public static final String BASE_TAN_AND_SPAN_PARAMS_PANE_NAME = "公法线与跨棒距";
    public static final String[] BASE_TAN_AND_SPAN_PARAMS_COLUMNS = new String[]{"参数名", "计算值", "单位"};
    public static final String[][] BASE_TAN_AND_SPAN_PARAMS_NAME_UNIT
            = new String[][]{{"分度圆直径", "mm"}, {"齿顶圆直径", "mm"}, {"齿根圆直径", "mm"}, {"端面压力角", "mm"}, {"基园", "mm"}, {"当量齿数", null}, {"跨齿数", null}, {"公法线长度", "mm"}, {"公法线长度处直径", "mm"}, {"跨棒距测量点直径", "mm"}, {"跨棒距", "mm"}};
    /**
     * 偏差转换计算结果数据面板设置
     */
    public static final String DEVIATION_PARAMS_PANE_NAME = "偏差转换";
    public static final String[] DEVIATION_PARAMS_COLUMNS = new String[]{"参数名", "计算值", "单位"};
    public static final String[][] DEVIATION_PARAMS_NAME_UNIT
            = new String[][]{{"公法线上偏差", "mm"}, {"跨棒距一", "mm"}, {"跨棒距上偏差", "mm"}, {"公法线下偏差", "mm"}, {"跨棒距二", "mm"}, {"跨棒距下偏差", "mm"}, {"跨棒距上偏差am1", "mm"}, {"公法线上偏差Ws", "mm"}, {"跨棒距下偏差am2", "mm"}, {"公法线下偏差Wx", "mm"}};

    private TableSettings() {
    }
}
