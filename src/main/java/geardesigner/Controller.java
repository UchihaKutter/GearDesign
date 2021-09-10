package geardesigner;

import geardesigner.beans.CodeException;
import geardesigner.beans.InputException;
import geardesigner.beans.Record;
import geardesigner.beans.Specifications;
import geardesigner.controls.Alerts;
import geardesigner.controls.DecimalFormatter;
import geardesigner.controls.InputParamTable;
import geardesigner.controls.OutputParamTable;
import geardesigner.units.Angle;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

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

    private Gear gear;

    public Controller() throws IOException, NoSuchMethodException {
        initTables();
        preservedDigits = new SimpleIntegerProperty(4);
    }

    /**
     * 计算任一圆参数
     *
     * @return 计算结果Gear.AnyCircle
     */
    @Contract(value = "!null,!null->!null", pure = true)
    private static Gear.AnyCircle calculateAnyCircle(Gear gear, Double diameter) {
        return gear.new AnyCircle(diameter).calculate();
    }

    private static void setNoAnchorPaneGap(@NotNull Node childOfAnchorPane) {
        AnchorPane.setLeftAnchor(childOfAnchorPane, 0.0);
        AnchorPane.setRightAnchor(childOfAnchorPane, 0.0);
        AnchorPane.setTopAnchor(childOfAnchorPane, 0.0);
        AnchorPane.setBottomAnchor(childOfAnchorPane, 0.0);
    }

    @FXML
    void initialize() {
        APaneInputParams.getChildren().add(tableInputParams);
        APaneAnyCircle.getChildren().add(tableAnyCircle);
        APaneBaseTanAndSpan.getChildren().add(tableBaseTanAndSpan);
        APaneDeviation.getChildren().add(tableDeviation);
        btCalculate.setOnAction(event -> calGear());
        btCalAnyCircle.setOnAction(event -> flushAnyCircle(true));
        /**
         * 角度值切换
         */
        groupAngles.selectedToggleProperty().addListener((observableValue, oldToggle, newToggle) -> {
            if (oldToggle != newToggle) {
                refreshAngleDisplay();
            }
        });
        setLayout();
        initChoiceBox();
        initBindings();
    }

    private void initTables() throws IOException, NoSuchMethodException {
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
        /**
         * 添加输入过滤器
         */
        tableInputParams.setTextFormatters(DecimalFormatter.class);
        /**
         * 读取历史记录并恢复上一次的记录
         */
        final Record lastRecord = Services.get().RecordBase().getLastRecord();
        if (lastRecord != null) {
            Log.info("上一次的记录时间为" + lastRecord.getTimestamp().toString());
            try {
                tableInputParams.setValues(lastRecord.getSpecs().toValueMap());
            } catch (CodeException e) {
                Log.error(e);
            }
        }
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
        /**
         * 绑定列宽
         */
        setNoAnchorPaneGap(tableInputParams);
        setNoAnchorPaneGap(tableAnyCircle);
        setNoAnchorPaneGap(tableBaseTanAndSpan);
        setNoAnchorPaneGap(tableDeviation);
    }

    private void setLayout() {
        ButtonBar.setButtonData(btCalculate, ButtonBar.ButtonData.OK_DONE);
        ButtonBar.setButtonData(btSaveParams, ButtonBar.ButtonData.RIGHT);
    }

    private @NotNull Specifications getAllSpecs() throws InputException {
        final Map<String, Number> values = tableInputParams.getValues();
        return new Specifications(values);
    }

    /**
     * 执行齿轮计算
     */
    private void calGear() {
        try {
            Specifications specs = getAllSpecs();
            gear = new Gear(specs);
            gear.calculate();
            gear.calculateDeviation();
            /**
             * 记录计算历史
             */
            Services.get().RecordBase().submitRecord(new Record(specs));
            flushTables();
        } catch (InputException e) {
            Alerts.warning(tableInputParams.getScene().getWindow(), "请输入完整有效的设计参数");
            Log.warning("参数输入错误", e);
        }
    }

    private void refreshAngleDisplay() {
        final Toggle selectedToggle = groupAngles.getSelectedToggle();
        if (selectedToggle.equals(rbToDegree)) {
            tableAnyCircle.changeUnits(Angle.RADIANS, Angle.DEGREES);
            tableBaseTanAndSpan.changeUnits(Angle.RADIANS, Angle.DEGREES);
            tableDeviation.changeUnits(Angle.RADIANS, Angle.DEGREES);
        } else if (selectedToggle.equals(rbToRadius)) {
            tableAnyCircle.changeUnits(Angle.DEGREES, Angle.RADIANS);
            tableBaseTanAndSpan.changeUnits(Angle.DEGREES, Angle.RADIANS);
            tableDeviation.changeUnits(Angle.DEGREES, Angle.RADIANS);
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
            final Double diameter = Double.valueOf(dText);
            anyCircle = calculateAnyCircle(gear, diameter);
        } else if (popup) {
            Alerts.warning(tableAnyCircle.getScene().getWindow(), "请输入有效的任一园直径");
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
                    .setValue("分度圆弧齿厚", null)
                    .setValue("任一圆弧齿厚", null)
                    .setValue("任一圆法向弦齿厚", null)
                    .setValue("任一园螺旋角", null);
        } else {
            tableAnyCircle.setValue("齿顶圆端面压力角", anyCircle.getAlphaT1())
                    .setValue("分度圆弧齿厚", anyCircle.getS())
                    .setValue("任一圆弧齿厚", anyCircle.getSa1())
                    .setValue("任一圆法向弦齿厚", anyCircle.getSn1())
                    .setValue("任一园螺旋角", anyCircle.getBeta1());
        }
    }

    //TODO 2021/8/9: 执行计算前先检查参数是否齐备,不包含输入项”任一园直径“
    private boolean checkAnyCircleInputs() {
        if (gear == null) {
            return false;
        }
        return true;
    }

    /**
     * 不包含计算
     */
    private void setTableBaseTanAndSpan(Gear gear) throws CodeException {
        if (gear != null) {
            tableBaseTanAndSpan.setValue("分度圆直径", gear.d)
                    .setValue("齿顶圆直径", gear.da)
                    .setValue("齿根圆直径", gear.df)
                    .setValue("基园", gear.db)
                    .setValue("当量齿数", gear.Zp)
                    .setValue("跨齿数", (double) gear.k)
                    .setValue("公法线长度", gear.getWk())
                    .setValue("公法线长度处直径", gear.getDWk())
                    .setValue("跨棒距测量点直径", gear.getDkm())
                    .setValue("跨棒距", gear.getM())
                    .setValue("端面压力角", gear.alphaT);
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
                    .setValue("公法线下偏差Wx", gear.getWx())
                    .setValue("跨棒距上偏差am1", gear.getAlphaM1())
                    .setValue("跨棒距下偏差am2", gear.getAlphaM2());
        }
    }

}
