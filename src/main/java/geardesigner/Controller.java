package geardesigner;

import geardesigner.beans.Record;
import geardesigner.beans.*;
import geardesigner.controls.*;
import geardesigner.units.Angle;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

import static geardesigner.TableSettings.*;

/**
 * @author SUPERTOP
 */
public class Controller {
    private final IntegerProperty preservedDigits;
    private final ObjectProperty<Angle> angleUnit;
    private final BooleanProperty accessibleDetails;
    @FXML
    private Label lInfo;
    @FXML
    private Label lVersion;
    @FXML
    private AnchorPane APaneInputParams;
    @FXML
    private AnchorPane APaneBaseTanAndSpan;
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
     */ private Button btCalculate;
    @FXML
    private Button btSelectRecord;
    @FXML
    private Button btAnyCircle;
    @FXML
    private Button btDeviation;

    private InputParamTable tableInputParams;
    private OutputParamTable tableBaseTanAndSpan;

    /**
     * 因为selector需要指定Owner，所以必须使用懒加载
     */
    private RecordSelector selector;
    private AnyCircleController anyCircleController;
    private Stage anyCircleStage;
    private DeviationController deviationController;
    private Stage deviationStage;
    private Gear gear;

    public Controller() throws IOException, NoSuchMethodException {
        selector = null;
        initTables();
        preservedDigits = new SimpleIntegerProperty(4);
        angleUnit = new SimpleObjectProperty<>(Angle.DEGREES);
        accessibleDetails = new SimpleBooleanProperty(false);
    }

    private static void setNoAnchorPaneGap(@NotNull Node childOfAnchorPane) {
        AnchorPane.setLeftAnchor(childOfAnchorPane, 0.0);
        AnchorPane.setRightAnchor(childOfAnchorPane, 0.0);
        AnchorPane.setTopAnchor(childOfAnchorPane, 0.0);
        AnchorPane.setBottomAnchor(childOfAnchorPane, 0.0);
    }

    @FXML
    void initialize() {
        lInfo.setText(Config.get("Info"));
        lVersion.setText(Config.get("Version"));
        APaneInputParams.getChildren().add(tableInputParams);
        APaneBaseTanAndSpan.getChildren().add(tableBaseTanAndSpan);
        btCalculate.setOnAction(event -> calGear());
        btSelectRecord.setOnAction(event -> btaSelectRecord());
        btAnyCircle.setOnAction(actionEvent -> btaAnyCircle());
        btDeviation.setOnAction(actionEvent -> btaDeviation());
        /**
         * 角度值切换
         */
        groupAngles.selectedToggleProperty().addListener((observableValue, oldToggle, newToggle) -> {
            if (oldToggle != newToggle) {
                if (newToggle.equals(rbToDegree)) {
                    angleUnit.set(Angle.DEGREES);
                } else if (newToggle.equals(rbToRadius)) {
                    angleUnit.set(Angle.RADIANS);
                }
            }
        });
        setLayout();
        initChoiceBox();
        initBindings();
    }

    private void initTables() throws IOException, NoSuchMethodException {
        tableInputParams = InputParamTable.createTable(INPUT_PARAMS_PANE_NAME, INPUT_PARAMS_COLUMNS, INPUT_PARAMS_NAME_UNIT);
        tableBaseTanAndSpan = OutputParamTable.createTable(BASE_TAN_AND_SPAN_PARAMS_PANE_NAME, null, BASE_TAN_AND_SPAN_PARAMS_NAME_UNIT);

        /**
         * 添加输入过滤器
         */
        tableInputParams.setTextFormatters(DecimalFormatter.class);
        /**
         * 读取历史记录并恢复上一次的记录
         */
        final Record lastRecord = Services.get().RecordBase().getLastRecord();
        loadRecord(lastRecord, "上一次的记录时间为");
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
        /**
         * 当输入发生改变时，禁用任一圆面板
         */
        tableInputParams.setOnChanged(o -> accessibleDetails.set(false));
        tableBaseTanAndSpan.bindDigitProperty(preservedDigits);

        /**
         * 添加Listener，同步修改一个其他的Property
         */
        cbPreservedDigit.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> preservedDigits.setValue(newValue));
        btAnyCircle.disableProperty().bind(accessibleDetails.not());
        btDeviation.disableProperty().bind(accessibleDetails.not());
        angleUnit.addListener((observable, oldValue, newValue) -> refreshAngleDisplay(oldValue, newValue));
        /**
         * 绑定列宽
         */
        setNoAnchorPaneGap(tableInputParams);
        setNoAnchorPaneGap(tableBaseTanAndSpan);
    }

    private void setLayout() {
    }

    private @NotNull Specifications getAllSpecs() throws InputException {
        final Map<String, Number> values = tableInputParams.getValues();
        return Specifications.toSpecifications(values);
    }

    /**
     * 事件处理器
     */
    private void btaSelectRecord() {
        if (selector == null) {
            try {
                selector = new RecordSelector(btSelectRecord.getScene().getWindow());
            } catch (IOException e) {
                Log.error(e);
            }
        }
        final Record record = selector.showAndWait();
        loadRecord(record, "加载记录的时间戳为");
    }

    private void btaAnyCircle() {
        if (gear != null) {
            if (anyCircleStage == null) {
                initAnyCircle(btAnyCircle.getScene());
            }
            try {
                anyCircleController.run(angleUnit.get(), preservedDigits.get(), gear);
            } catch (CodeException e) {
                Log.error(e);
            }
            anyCircleStage.showAndWait();
        } else {
            Alerts.warning(btAnyCircle.getScene().getWindow(), "请先计算有效的基本参数");
        }
    }

    private void btaDeviation() {
        if (gear != null) {
            if (deviationStage == null) {
                initDeviation(btDeviation.getScene());
            }
            try {
                deviationController.run(angleUnit.get(), preservedDigits.get(), gear);
            } catch (CodeException e) {
                Log.error(e);
            }
            deviationStage.showAndWait();
        } else {
            Alerts.warning(btDeviation.getScene().getWindow(), "请先计算有效的基本参数");
        }
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
            //TODO 2021/12/28:优化性能
            Services.get().RecordBase().submitRecord(new Record(specs));
            flushTables();
            /**
             * 启用任一圆计算
             */
            accessibleDetails.set(true);
        } catch (InputException e) {
            Alerts.warning(tableInputParams.getScene().getWindow(), "请输入完整有效的设计参数");
            Log.warning("参数输入错误", e);
        }
    }

    /**
     * 加载历史记录到输入面板
     *
     * @param record 指定的历史记录，null则不执行动作
     * @param info   加载时的日志抬头
     */
    private void loadRecord(@Nullable Record record, @NotNull String info) {
        if (record != null) {
            Log.info(info + record.getTimestamp().toString());
            try {
                tableInputParams.setValues(record.getSpecs().toValueMap());
            } catch (CodeException e) {
                Log.error(e);
            }
        }
    }

    private void refreshAngleDisplay(Angle oldUnit, Angle newUnit) {
        if (newUnit != oldUnit) {
            tableBaseTanAndSpan.changeUnits(oldUnit, newUnit);
        }
    }

    private void flushTables() {
        flushTableBaseTanAndSpan();
    }

    private void flushTableBaseTanAndSpan() {
        try {
            setTableBaseTanAndSpan(gear);
        } catch (CodeException e) {
            Log.error(e);
        }
    }

    /**
     * 不包含计算
     */
    private void setTableBaseTanAndSpan(Gear gear) throws CodeException {
        if (gear != null) {
            tableBaseTanAndSpan.setValue("分度圆直径", gear.d()).setValue("齿顶圆直径", gear.da()).setValue("齿根圆直径", gear.df()).setValue("基圆", gear.db()).setValue("当量齿数", gear.zp()).setValue("跨齿数", (double) gear.k()).setValue("公法线长度", gear.baseTangent().Wk()).setValue("公法线长度处直径", gear.baseTangent().dWk()).setValue("跨棒距测量点直径", gear.span().dkm()).setValue("跨棒距", gear.span().M()).setValue("端面压力角", gear.alphaT());
        }
    }


    /**
     * 初始化任一圆计算面板
     */
    private void initAnyCircle(Scene parentScene) {
        if (parentScene != null) {
            URL fxml = getClass().getResource("controls/AnyCircle.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(fxml);
            Parent root;
            try {
                root = fxmlLoader.load();
                final Scene scene = new Scene(root);
                anyCircleStage = new Stage();
                anyCircleStage.initModality(Modality.WINDOW_MODAL);
                anyCircleStage.setTitle("计算任一圆...");
                anyCircleStage.setScene(scene);
                anyCircleStage.sizeToScene();
                anyCircleStage.setResizable(false);
                anyCircleStage.getIcons().addAll(((Stage) parentScene.getWindow()).getIcons());
                anyCircleStage.initOwner(parentScene.getWindow());
                anyCircleController = fxmlLoader.getController();
            } catch (IOException e) {
                Log.error("初始化任一圆面板失败", e);
            }
        }
    }

    /**
     * 初始化偏差转换面板
     */
    private void initDeviation(Scene parentScene) {
        if (parentScene != null) {
            URL fxml = getClass().getResource("controls/Deviation.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(fxml);
            Parent root;
            try {
                root = fxmlLoader.load();
                final Scene scene = new Scene(root);
                deviationStage = new Stage();
                deviationStage.initModality(Modality.WINDOW_MODAL);
                deviationStage.setTitle("偏差转换");
                deviationStage.setScene(scene);
                deviationStage.sizeToScene();
                deviationStage.setResizable(false);
                deviationStage.getIcons().addAll(((Stage) parentScene.getWindow()).getIcons());
                deviationStage.initOwner(parentScene.getWindow());
                deviationController = fxmlLoader.getController();
            } catch (IOException e) {
                Log.error("初始化偏差转换面板失败", e);
            }
        }
    }
}
