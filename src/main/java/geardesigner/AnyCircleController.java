package geardesigner;

import geardesigner.beans.CodeException;
import geardesigner.controls.Alerts;
import geardesigner.controls.OutputParamTable;
import geardesigner.units.Angle;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static geardesigner.TableSettings.*;

/**
 * 计算任一园面板
 *
 * @author SUPERTOP
 */
public class AnyCircleController {
    private final IntegerProperty preservedDigits;
    @FXML
    private TextField tfDoubleAnyCircle;
    @FXML
    private Button btCalAnyCircle;
    @FXML
    private AnchorPane APaneAnyCircle;
    private OutputParamTable tableAnyCircle;
    private Gear gearCache;

    public AnyCircleController() {
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

    @FXML
    void initialize() {
        try {
            initTables();
        } catch (IOException e) {
            Log.error(e);
        }
        APaneAnyCircle.getChildren().add(tableAnyCircle);
        btCalAnyCircle.setOnAction(event -> flushAnyCircle(true));
        tfDoubleAnyCircle.setOnAction(actionEvent -> System.out.println(tfDoubleAnyCircle.getCharacters()));
    }

    private void initTables() throws IOException {
        tableAnyCircle = OutputParamTable.createTable(
                ANY_CIRCLE_PARAMS_PANE_NAME,
                ANY_CIRCLE_PARAMS_COLUMNS,
                ANY_CIRCLE_PARAMS_NAME_UNIT);
        tableAnyCircle.bindDigitProperty(preservedDigits);
    }

    public void run(@NotNull Angle unit, int preserved, Gear gear) throws CodeException {
        setTableAnyCircle(null);
        switch (unit) {
            case DEGREES -> tableAnyCircle.changeUnits(Angle.RADIANS, Angle.DEGREES);
            case RADIANS -> tableAnyCircle.changeUnits(Angle.DEGREES, Angle.RADIANS);
        }
        preservedDigits.set(preserved);
        gearCache = gear;
    }

    /**
     * 刷新任一圆面板
     *
     * @param popup 当输入无法运行计算时，是否弹窗提示
     */
    private void flushAnyCircle(final boolean popup) {
        if (gearCache == null) {
            return;
        }
        final String dText = tfDoubleAnyCircle.getText().trim();
        Gear.AnyCircle anyCircle = null;
        if (!dText.isBlank() && checkAnyCircleInputs()) {
            final Double diameter = Double.valueOf(dText);
            anyCircle = calculateAnyCircle(gearCache, diameter);
        } else if (popup) {
            Alerts.warning(tableAnyCircle.getScene().getWindow(), "请输入有效的任一圆直径");
        }
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
                    .setValue("任一圆螺旋角", null);
        } else {
            tableAnyCircle.setValue("齿顶圆端面压力角", anyCircle.getAlphaT1())
                    .setValue("分度圆弧齿厚", anyCircle.getS())
                    .setValue("任一圆弧齿厚", anyCircle.getSa1())
                    .setValue("任一圆法向弦齿厚", anyCircle.getSn1())
                    .setValue("任一圆螺旋角", anyCircle.getBeta1());
        }
    }

    //TODO 2021/8/9: 执行计算前先检查参数是否齐备,不包含输入项”任一园直径“
    private boolean checkAnyCircleInputs() {
        if (gearCache == null) {
            return false;
        }
        return true;
    }
}
