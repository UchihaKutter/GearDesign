package geardesigner;

import geardesigner.controls.ParameterTable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

import javax.swing.*;

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
                new String[]{"任一圆直径", "齿顶圆端面压力角", "分度圆处弧齿厚", "任一圆处弧齿厚", "任一园螺旋角", "任一圆处法向弦齿厚"},
                new String[]{"1", "1", "1", "1", "1", "1"},
                new boolean[]{true, false, false, false, false, false});
        tableBaseTanAndSpan = new ParameterTable(
                new String[]{"分度圆直径", "齿顶圆直径", "齿根圆直径", "端面压力角", "基园", "当量齿数", "跨齿数", "公法线长度", "公法线长度处直径", "跨棒距测量点直径", "跨棒距"
                },
                new String[]{"2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2"},
                new boolean[]{false, false, false, false, false, false, false, false, false, false, false});
        tableDeviation = new ParameterTable(
                new String[]{"公法线上偏差", "跨棒距一", "跨棒距上偏差", "公法线下偏差", "跨棒距二", "跨棒距下偏差", "跨棒距上偏差am1", "公法线上偏差Ws", "跨棒距下偏差am2",
                        "公法线下偏差Wx"},
                new String[]{"3", "3", "3", "3", "3", "3", "3", "3", "3", "3"},
                new boolean[]{false, false, false, false, false, false, false, false, false, false});
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
        String value = tableAnyCircle.getValue("任一圆直径");
        if (!(gear == null || value.isEmpty())) {
            double da = Double.parseDouble(value);
            Gear.AnyCircle anyCircle = gear.new AnyCircle(da).calculate();
            tableAnyCircle.setValue("齿顶圆端面压力角", String.valueOf(anyCircle.getAlphaT1()))
                    .setValue("分度圆处弧齿厚", String.valueOf(anyCircle.getS()))
                    .setValue("任一圆处弧齿厚", String.valueOf(anyCircle.getSa1()))
                    .setValue("任一圆处法向弦齿厚", String.valueOf(anyCircle.getSn1()));
            if (isRadius) {
                tableAnyCircle.setValue("任一园螺旋角", String.valueOf(anyCircle.getBeta1()));
            } else {
                tableAnyCircle.setValue("任一园螺旋角", String.valueOf(Math.toDegrees(anyCircle.getBeta1())));
            }
        }
    }

    /**
     * 不包含计算
     */
    private void setTableBaseTanAndSpan() {
        if (gear != null) {
            tableBaseTanAndSpan.setValue("分度圆直径", String.valueOf(gear.d))
                    .setValue("齿顶圆直径", String.valueOf(gear.da))
                    .setValue("齿根圆直径", String.valueOf(gear.df))
                    .setValue("基园", String.valueOf(gear.db))
                    .setValue("当量齿数", String.valueOf(gear.Zp))
                    .setValue("跨齿数", String.valueOf(gear.k))
                    .setValue("公法线长度", String.valueOf(gear.getWk()))
                    .setValue("公法线长度处直径", String.valueOf(gear.getDWk()))
                    .setValue("跨棒距测量点直径", String.valueOf(gear.getDkm()))
                    .setValue("跨棒距", String.valueOf(gear.getM()));
            if (isRadius) {
                tableBaseTanAndSpan.setValue("端面压力角", String.valueOf(gear.alphaT));
            } else {
                tableBaseTanAndSpan.setValue("端面压力角", String.valueOf(Math.toDegrees(gear.alphaT)));
            }
        }
    }

    /**
     * 不包含计算
     */
    private void setTableDeviation() {
        if (gear != null) {
            tableDeviation.setValue("公法线上偏差", String.valueOf(gear.getX1()))
                    .setValue("跨棒距一", String.valueOf(gear.getM1()))
                    .setValue("跨棒距上偏差", String.valueOf(gear.getMs()))
                    .setValue("公法线下偏差", String.valueOf(gear.getX2()))
                    .setValue("跨棒距二", String.valueOf(gear.getM2()))
                    .setValue("跨棒距下偏差", String.valueOf(gear.getMx()))
                    .setValue("公法线上偏差Ws", String.valueOf(gear.getWs()))
                    .setValue("公法线下偏差Wx", String.valueOf(gear.getWx()));
            if (isRadius) {
                tableDeviation.setValue("跨棒距上偏差am1", String.valueOf(gear.getAlphaM1()))
                        .setValue("跨棒距下偏差am2", String.valueOf(gear.getAlphaM2()));
            } else {
                tableDeviation.setValue("跨棒距上偏差am1", String.valueOf(Math.toDegrees(gear.getAlphaM1())))
                        .setValue("跨棒距下偏差am2", String.valueOf(Math.toDegrees(gear.getAlphaM2())));
            }
        }
    }

    private void angleUnitSwitch() {
        isRadius = groupAngles.getSelectedToggle() == rbToRadius;
        setTableBaseTanAndSpan();
        setTableDeviation();
        setTableAnyCircle();
    }
}
