package geardesigner;

import geardesigner.beans.Decimal;
import geardesigner.controls.ParameterTable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

public class Controller {
    @FXML
    private AnchorPane anchorAnyCircle;

    @FXML
    private AnchorPane anchorBaseTanAndSpan;

    @FXML
    private AnchorPane anchorDeviation;

    @FXML
    private TextField tfIntMn;

    @FXML
    private TextField tfIntZ;

    @FXML
    private TextField tfDoubleAlphaN;

    @FXML
    private TextField tfDoubleBeta;

    @FXML
    private TextField tfDoubleXn;

    @FXML
    private TextField tfDoubleHa;

    @FXML
    private TextField tfDoubleHf;

    @FXML
    private TextField tfDoubleCf;

    @FXML
    private TextField tfDoubleDp;

    @FXML
    private TextField tfDoubleWs;

    @FXML
    private TextField tfDoubleWx;

    @FXML
    private TextField tfDoubleMs;

    @FXML
    private TextField tfDoubleMx;

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
    /**
     * 重算所有值
     */
    private Button btCalculate;

    @FXML
    private Button btSaveParams;

    private ParameterTable tableAnyCircle;
    private ParameterTable tableBaseTanAndSpan;
    private ParameterTable tableDeviation;

    // TODO: 2020/3/21 显示数值保留位数
    private IntegerProperty preservedDigits;
    private boolean isRadius = false;

    private Gear gear;

    private Specifications.SpecificationsBuilder specificationsBuilder;

    private EventHandler<KeyEvent> anyCirclePressedEvent = event -> {
        if (event.getCode() == KeyCode.ENTER) {
            event.consume();
            if (event.isShiftDown()) {

            } else {
                setTableAnyCircle();
            }
        }
    };

    public Controller() {
        specificationsBuilder = Specifications.SpecificationsBuilder.aSpecifications();
        tableAnyCircle = new ParameterTable(
                new String[]{"齿顶圆端面压力角", "分度圆处弧齿厚", "任一圆处弧齿厚", "任一园螺旋角", "任一圆处法向弦齿厚"},
                new String[]{"m^2", "1", "1", "1", "1"});
        tableBaseTanAndSpan = new ParameterTable(
                new String[]{"分度圆直径", "齿顶圆直径", "齿根圆直径", "端面压力角", "基园", "当量齿数", "跨齿数", "公法线长度", "公法线长度处直径", "跨棒距测量点直径", "跨棒距"
                },
                new String[]{"2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2"});
        tableDeviation = new ParameterTable(
                new String[]{"公法线上偏差", "跨棒距一", "跨棒距上偏差", "公法线下偏差", "跨棒距二", "跨棒距下偏差", "跨棒距上偏差am1", "公法线上偏差Ws", "跨棒距下偏差am2",
                        "公法线下偏差Wx"},
                new String[]{"3", "3", "3", "3", "3", "3", "3", "3", "3", "3"});
        preservedDigits = new SimpleIntegerProperty(6);
    }

    @FXML
    void initialize() {
        anchorAnyCircle.getChildren().add(tableAnyCircle);
        anchorBaseTanAndSpan.getChildren().add(tableBaseTanAndSpan);
        anchorDeviation.getChildren().add(tableDeviation);
        btCalculate.setOnAction(event -> refreshGear());
        btCalAnyCircle.setOnAction(event -> setTableAnyCircle());
        rbToDegree.setOnAction(event -> angleUnitSwitch());
        rbToRadius.setOnAction(event -> angleUnitSwitch());
        tableAnyCircle.setOnKeyPressed(anyCirclePressedEvent);
        setLayout();
    }

    private void setLayout() {
        tableAnyCircle.setNameWidth(160);
        tableAnyCircle.setSymbolWidth(80);
        tableAnyCircle.setValueWidth(160);
        tableBaseTanAndSpan.setNameWidth(160);
        tableBaseTanAndSpan.setSymbolWidth(80);
        tableBaseTanAndSpan.setValueWidth(160);
        tableDeviation.setNameWidth(160);
        tableDeviation.setSymbolWidth(80);
        tableDeviation.setValueWidth(160);
    }

    private Specifications getAllSpecs() {
        try {
            specificationsBuilder.alphaN(Math.toRadians(Double.parseDouble(tfDoubleAlphaN.getText().trim())))
                    .beta(Math.toRadians(Double.parseDouble(tfDoubleBeta.getText().trim())))
                    .Cf(Double.parseDouble(tfDoubleCf.getText().trim()))
                    .dp(Double.parseDouble(tfDoubleDp.getText().trim()))
                    .ha(Double.parseDouble(tfDoubleHa.getText().trim()))
                    .hf(Double.parseDouble(tfDoubleHf.getText().trim()))
                    .Mn(Integer.parseInt(tfIntMn.getText().trim()))
                    .Ms(Double.parseDouble(tfDoubleMs.getText().trim()))
                    .Mx(Double.parseDouble(tfDoubleMx.getText().trim()))
                    .Ws(Double.parseDouble(tfDoubleWs.getText().trim()))
                    .Wx(Double.parseDouble(tfDoubleWx.getText().trim()))
                    .Xn(Double.parseDouble(tfDoubleXn.getText().trim()))
                    .Z(Integer.parseInt(tfIntZ.getText().trim()));
            return specificationsBuilder.build();
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void refreshGear() {
        Specifications specs = getAllSpecs();
        if (specs == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("请输入参数值");
            alert.showAndWait();
        } else {
            gear = new Gear(specs);
            gear.calculate();
            gear.calculateDeviation();
            refreshTables();
        }
    }

    private void autoRefreshGear() {
        Specifications specs = getAllSpecs();
        if (specs != null) {
            gear = new Gear(getAllSpecs());
            gear.calculate();
            gear.calculateDeviation();
            refreshTables();
        }
    }

    private void refreshTables() {
        setTableBaseTanAndSpan();
        setTableDeviation();
        setTableAnyCircle();
    }

    /**
     * 包含计算
     */
    private void setTableAnyCircle() {
        Decimal value = tableAnyCircle.getValue("任一圆直径");
        if (gear != null) {
            Gear.AnyCircle anyCircle = gear.new AnyCircle(value).calculate();
            tableAnyCircle.setValue("齿顶圆端面压力角", anyCircle.getAlphaT1())
                    .setValue("分度圆处弧齿厚", anyCircle.getS())
                    .setValue("任一圆处弧齿厚", anyCircle.getSa1())
                    .setValue("任一圆处法向弦齿厚", anyCircle.getSn1());
            if (isRadius) {
                tableAnyCircle.setValue("任一园螺旋角", anyCircle.getBeta1());
            } else {
                tableAnyCircle.setValue("任一园螺旋角", MathUtils.toDegrees(anyCircle.getBeta1()));
            }
        }
    }

    /**
     * 不包含计算
     */
    private void setTableBaseTanAndSpan() {
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
    private void setTableDeviation() {
        if (gear != null) {
            tableDeviation.setValue("公法线上偏差", (gear.getX1()))
                    .setValue("跨棒距一", gear.getM1())
                    .setValue("跨棒距上偏差",gear.getMs())
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

    private void angleUnitSwitch() {
        //待办 2021/8/6: 先检查待转换的参数是否完备
        isRadius = groupAngles.getSelectedToggle() == rbToRadius;
        setTableBaseTanAndSpan();
        setTableDeviation();
        setTableAnyCircle();
    }
}
