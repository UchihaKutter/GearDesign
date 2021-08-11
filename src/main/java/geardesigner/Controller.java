package geardesigner;

import geardesigner.beans.Decimal;
import geardesigner.controls.InputParamTable;
import geardesigner.controls.OutputParamTable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.jetbrains.annotations.Contract;

import java.io.IOException;
import java.util.Map;

import static geardesigner.TableSettings.*;

public class Controller {
    @FXML
    private AnchorPane APaneInputParams;

    @FXML
    private AnchorPane APaneAnyCircle;

    @FXML
    private AnchorPane APaneBaseTanAndSpan;

    @FXML
    private AnchorPane APaneDeviation;

    @FXML
    private TextField tfDoubleAnyCircle;

    @FXML
    private AnchorPane APaneAnyCircleUnit;

    @FXML
    private Button btCalAnyCircle;

    @FXML
    private RadioButton rbToDegree;

    @FXML
    private RadioButton rbToRadius;

    @FXML
    private ToggleGroup groupAngles;

    @FXML
    private ChoiceBox<Integer> cbPreservedDigit;

    @FXML
    /**
     * 重算所有值
     */
    private Button btCalculate;

    @FXML
    private Button btSaveParams;

    private InputParamTable tableInputParams;
    private OutputParamTable tableAnyCircle;
    private OutputParamTable tableBaseTanAndSpan;
    private OutputParamTable tableDeviation;

    private IntegerProperty preservedDigits;
    private boolean isRadius = false;

    private Gear gear;


    public Controller() throws IOException {
        initTables();
        preservedDigits = new SimpleIntegerProperty(4);
    }

    @FXML
    void initialize() {
        APaneInputParams.getChildren().add(tableInputParams);
        APaneAnyCircle.getChildren().add(tableAnyCircle);
        APaneBaseTanAndSpan.getChildren().add(tableBaseTanAndSpan);
        APaneDeviation.getChildren().add(tableDeviation);
        btCalculate.setOnAction(event -> refreshGear());
        btCalAnyCircle.setOnAction(event -> flushAnyCircle(true));
//        rbToDegree.setOnAction(event -> angleUnitSwitch());
//        rbToRadius.setOnAction(event -> angleUnitSwitch());
        setLayout();
        initChoiceBox();
        initBindings();
    }

    private void initTables() throws IOException {
        tableInputParams = InputParamTable.createTable(
                INPUT_PARAMS_PANE_NAME,
                INPUT_PARAMS_COLUMNS,
                INPUT_PARAMS_NAME_UNIT);
        tableAnyCircle = OutputParamTable.createTable(
                ANY_CIRCLE_PARAMS_PANE_NAME,
                ANY_CIRCLE_PARAMS_COLUMNS,
                ANY_CIRCLE_PARAMS_NAME_UNIT);
        tableBaseTanAndSpan = OutputParamTable.createTable(
                BASE_TAN_AND_SPAN_PARAMS_PANE_NAME,
                BASE_TAN_AND_SPAN_PARAMS_COLUMNS,
                BASE_TAN_AND_SPAN_PARAMS_NAME_UNIT
        );
        tableDeviation = OutputParamTable.createTable(
                DEVIATION_PARAMS_PANE_NAME,
                DEVIATION_PARAMS_COLUMNS,
                DEVIATION_PARAMS_NAME_UNIT
        );
    }

    private void initChoiceBox() {
        /**
         * 获取Items列表，然后添加元素
         */
        final ObservableList<Integer> items = cbPreservedDigit.getItems();
        items.addAll(0, 1, 2, 3, 4, 5, 6);
        /**
         * 或者使用setAll，清空列表并添加新值的列表
         */
        items.setAll(0, 1, 2, 3, 4, 5, 6);
        /**
         * 设置默认选中项
         */
        cbPreservedDigit.getSelectionModel().select(preservedDigits.getValue());
    }

    private void initBindings() {
        tableBaseTanAndSpan.bindDigitProperty(preservedDigits);
        tableDeviation.bindDigitProperty(preservedDigits);
        tableAnyCircle.bindDigitProperty(preservedDigits);
        /**
         * 添加Listener，同步修改一个其他的Property
         */
        cbPreservedDigit.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> preservedDigits.setValue(newValue));
    }

    private void setLayout() {

    }


    private Specifications getAllSpecs() throws InputException {
        final Map<String, Decimal> values = tableInputParams.getValues();
        return new Specifications(values);
    }

    private void refreshGear() {
        try {
            Specifications specs = getAllSpecs();
            gear = new Gear(specs);
            gear.calculate();
            gear.calculateDeviation();
            flushTables();
        } catch (InputException e) {
            //待办 2021/8/9: 警告弹窗
            Log.warning("参数输入错误", e);
        }
    }

    private void flushTables() {
        flushTableBaseTanAndSpan();
        flushTableDeviation();
        flushAnyCircle(false);
    }

    private void flushTableBaseTanAndSpan() {
        try {
            setTableBaseTanAndSpan(gear);
        } catch (CodeException e) {
            Log.error(e);
        }
    }

    private void flushTableDeviation() {
        try {
            setTableDeviation(gear);
        } catch (CodeException e) {
            Log.error(e);
        }
    }

    /**
     * 刷新任一圆面板
     *
     * @param popup 当输入无法运行计算时，是否弹窗提示
     */
    private void flushAnyCircle(final boolean popup) {
        final String dText = tfDoubleAnyCircle.getText().trim();
        Gear.AnyCircle anyCircle = null;
        if (!dText.isBlank() && checkAnyCircleInputs()) {
            final Decimal diameter = Decimal.valueOf(dText);
            anyCircle = calculateAnyCircle(gear, diameter);
        } else if (popup) {
            //todo"参数输入不全，无法计算任一圆";
        }
        /**
         * 捕获编程错误
         */
        try {
            setTableAnyCircle(anyCircle);
        } catch (CodeException e) {
            Log.error(e);
        }
    }

    /**
     * 设置任一圆数据面板值
     *
     * @param anyCircle 计算过的AnyCircle，如果是null，则清空面板
     * @throws CodeException
     */
    private void setTableAnyCircle(Gear.AnyCircle anyCircle) throws CodeException {
        if (anyCircle == null) {
            tableAnyCircle.setValue("齿顶圆端面压力角", null)
                    .setValue("分度圆处弧齿厚", null)
                    .setValue("任一圆处弧齿厚", null)
                    .setValue("任一圆处法向弦齿厚", null)
                    .setValue("任一园螺旋角", null);
        } else {
            tableAnyCircle.setValue("齿顶圆端面压力角", anyCircle.getAlphaT1())
                    .setValue("分度圆处弧齿厚", anyCircle.getS())
                    .setValue("任一圆处弧齿厚", anyCircle.getSa1())
                    .setValue("任一圆处法向弦齿厚", anyCircle.getSn1());
            //待办 2021/8/9: 单位转换
            if (isRadius) {
                tableAnyCircle.setValue("任一园螺旋角", anyCircle.getBeta1());
            } else {
                tableAnyCircle.setValue("任一园螺旋角", MathUtils.toDegrees(anyCircle.getBeta1()));
            }
        }
    }

    //待办 2021/8/9: 执行计算前先检查参数是否齐备,不包含输入项”任一园直径“
    private boolean checkAnyCircleInputs() {
        if (gear == null) {
            return false;
        }
        return true;
    }

    /**
     * 计算任一圆参数
     *
     * @return 计算结果Gear.AnyCircle
     */
    @Contract(value = "!null,!null->!null", pure = true)
    private static Gear.AnyCircle calculateAnyCircle(Gear gear, Decimal diameter) {
        return gear.new AnyCircle(diameter).calculate();
    }


    /**
     * 不包含计算
     */
    private void setTableBaseTanAndSpan(Gear gear) throws CodeException {
        if (gear != null) {
            tableBaseTanAndSpan.setValue("分度圆直径", Decimal.valueOf(gear.d))
                    .setValue("齿顶圆直径", Decimal.valueOf(gear.da))
                    .setValue("齿根圆直径", Decimal.valueOf(gear.df))
                    .setValue("基园", Decimal.valueOf(gear.db))
                    .setValue("当量齿数", Decimal.valueOf(gear.Zp))
                    .setValue("跨齿数", Decimal.valueOf(gear.k))
                    .setValue("公法线长度", gear.getWk())
                    .setValue("公法线长度处直径", gear.getDWk())
                    .setValue("跨棒距测量点直径", gear.getDkm())
                    .setValue("跨棒距", gear.getM());
            if (isRadius) {
                tableBaseTanAndSpan.setValue("端面压力角", Decimal.valueOf(gear.alphaT));
            } else {
                tableBaseTanAndSpan.setValue("端面压力角", Decimal.valueOf(Math.toDegrees(gear.alphaT)));
            }
        }
    }

    /**
     * 不包含计算
     */
    private void setTableDeviation(Gear gear) throws CodeException {
        if (gear != null) {
            tableDeviation.setValue("公法线上偏差", (gear.getX1()))
                    .setValue("跨棒距一", gear.getM1())
                    .setValue("跨棒距上偏差", gear.getMs())
                    .setValue("公法线下偏差", gear.getX2())
                    .setValue("跨棒距二", gear.getM2())
                    .setValue("跨棒距下偏差", gear.getMx())
                    .setValue("公法线上偏差Ws", gear.getWs())
                    .setValue("公法线下偏差Wx", gear.getWx());
            if (isRadius) {
                tableDeviation.setValue("跨棒距上偏差am1", gear.getAlphaM1())
                        .setValue("跨棒距下偏差am2", gear.getAlphaM2());
            } else {
                tableDeviation.setValue("跨棒距上偏差am1", Decimal.valueOf(Math.toDegrees(gear.getAlphaM1().doubleValue())))
                        .setValue("跨棒距下偏差am2", MathUtils.toDegrees(gear.getAlphaM2()));
            }
        }
    }

//    private void angleUnitSwitch() {
//        //待办 2021/8/6: 先检查待转换的参数是否完备
//        isRadius = groupAngles.getSelectedToggle() == rbToRadius;
//        setTableBaseTanAndSpan();
//        setTableDeviation();
//        setTableAnyCircle();
//    }
}
