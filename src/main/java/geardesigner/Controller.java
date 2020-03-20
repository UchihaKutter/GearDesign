package geardesigner;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import geardesigner.controls.ParameterTable;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;

public class Controller {
    @FXML
    private AnchorPane anchorAnyCircle;

    @FXML
    private AnchorPane anchorBaseTanAndSpan;

    @FXML
    private AnchorPane anchorDeviation;

    @FXML
    private JFXTextField tfIntMn;

    @FXML
    private JFXTextField tfIntZ;

    @FXML
    private JFXTextField tfDoubleAlphaN;

    @FXML
    private JFXTextField tfDoubleBeta;

    @FXML
    private JFXTextField tfDoubleXn;

    @FXML
    private JFXTextField tfDoubleHa;

    @FXML
    private JFXTextField tfDoubleHf;

    @FXML
    private JFXTextField tfDoubleCf;

    @FXML
    private JFXTextField tfDoubleDp;

    @FXML
    private JFXTextField tfDoubleWs;

    @FXML
    private JFXTextField tfDoubleWx;

    @FXML
    private JFXTextField tfDoubleMs;

    @FXML
    private JFXTextField tfDoubleMx;

    @FXML
    private JFXButton btCalAnyCircle;

    @FXML
    private JFXRadioButton rbToDegree;

    @FXML
    private ToggleGroup groupAngles;

    @FXML
    private JFXRadioButton rbToRadius;

    @FXML
    private JFXButton btCalculate;

    @FXML
    private JFXButton btSaveParams;

    private ParameterTable tableAnyCircle;
    private ParameterTable tableBaseTanAndSpan;
    private ParameterTable tableDeviation;

    private Gear gear;

    private Specifications.SpecificationsBuilder specificationsBuilder;

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
    }

    @FXML
    void initialize() {
        anchorAnyCircle.getChildren().add(tableAnyCircle);
        anchorBaseTanAndSpan.getChildren().add(tableBaseTanAndSpan);
        anchorDeviation.getChildren().add(tableDeviation);
        btCalculate.setOnAction(event -> refreshGear());
        btCalAnyCircle.setOnAction(event -> setTableAnyCircle());
    }

    private Specifications getAllSpecs() {
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
    }

    private void refreshGear() {
        gear = new Gear(getAllSpecs());
        gear.calculate();
        gear.calculateDeviation();
        refreshTables();
    }

    private void refreshTables() {
        setTableBaseTanAndSpan();
        setTableDeviation();
    }

    private void setTableAnyCircle() {
        if (gear != null) {
            double da = Double.parseDouble(tableAnyCircle.getValue("任一圆直径"));
            Gear.AnyCircle anyCircle = gear.new AnyCircle(da).calculate();
            tableAnyCircle.setValue("齿顶圆端面压力角", String.valueOf(anyCircle.getAlphaT1()))
                    .setValue("分度圆处弧齿厚", String.valueOf(anyCircle.getS()))
                    .setValue("任一圆处弧齿厚", String.valueOf(anyCircle.getSa1()))
                    .setValue("任一园螺旋角", String.valueOf(anyCircle.getBeta1()))
                    .setValue("任一圆处法向弦齿厚", String.valueOf(anyCircle.getSn1()));
        }
    }

    private void setTableBaseTanAndSpan() {
        tableBaseTanAndSpan.setValue("分度圆直径", String.valueOf(gear.d))
                .setValue("齿顶圆直径", String.valueOf(gear.da))
                .setValue("齿根圆直径", String.valueOf(gear.df))
                .setValue("端面压力角", String.valueOf(gear.alphaT))
                .setValue("基园", String.valueOf(gear.db))
                .setValue("当量齿数", String.valueOf(gear.Zp))
                .setValue("跨齿数", String.valueOf(gear.k))
                .setValue("公法线长度", String.valueOf(gear.getWk()))
                .setValue("公法线长度处直径", String.valueOf(gear.getDWk()))
                .setValue("跨棒距测量点直径", String.valueOf(gear.getDkm()))
                .setValue("跨棒距", String.valueOf(gear.getM()));
    }

    private void setTableDeviation() {
        tableDeviation.setValue("公法线上偏差", String.valueOf(gear.getX1()))
                .setValue("跨棒距一", String.valueOf(gear.getM1()))
                .setValue("跨棒距上偏差", String.valueOf(gear.getMs()))
                .setValue("公法线下偏差", String.valueOf(gear.getX2()))
                .setValue("跨棒距二", String.valueOf(gear.getM2()))
                .setValue("跨棒距下偏差", String.valueOf(gear.getMx()))
                .setValue("跨棒距上偏差am1", String.valueOf(gear.getAlphaM1()))
                .setValue("公法线上偏差Ws", String.valueOf(gear.getWs()))
                .setValue("跨棒距下偏差am2", String.valueOf(gear.getAlphaM2()))
                .setValue("公法线下偏差Wx", String.valueOf(gear.getWx()));
    }
}
