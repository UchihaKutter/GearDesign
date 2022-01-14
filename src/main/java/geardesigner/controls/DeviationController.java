package geardesigner.controls;

import geardesigner.beans.CodeException;
import geardesigner.beans.Gear;
import geardesigner.units.Angle;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static geardesigner.TableSettings.DEVIATION_PARAMS_NAME_UNIT;

/**
 * @author SUPERTOP
 */
public class DeviationController {
    private final IntegerProperty preservedDigits;

    @FXML
    private AnchorPane pContent;

    private OutputParamTable tableDeviation;

    public DeviationController() throws IOException {
        preservedDigits = new SimpleIntegerProperty(4);
        initTables();
    }

    private void initTables() throws IOException {
        tableDeviation = OutputParamTable.createTable(
                null,
                null,
                DEVIATION_PARAMS_NAME_UNIT
        );
    }

    @FXML
    void initialize() {
        pContent.getChildren().add(tableDeviation);
        initBindings();
        setLayout();
    }

    private void initBindings() {
        tableDeviation.bindDigitProperty(preservedDigits);
    }

    private void setLayout() {
        AnchorPane.setTopAnchor(tableDeviation, 0.0);
        AnchorPane.setBottomAnchor(tableDeviation, 0.0);
        AnchorPane.setLeftAnchor(tableDeviation, 0.0);
        AnchorPane.setRightAnchor(tableDeviation, 0.0);
    }

    public void run(@NotNull Angle unit, int preserved, Gear gear) throws CodeException {
        setTableDeviation(gear);
        switch (unit) {
            case DEGREES -> tableDeviation.changeUnits(Angle.RADIANS, Angle.DEGREES);
            case RADIANS -> tableDeviation.changeUnits(Angle.DEGREES, Angle.RADIANS);
        }
        preservedDigits.set(preserved);
    }

    private void setTableDeviation(@NotNull Gear gear) throws CodeException {
        tableDeviation.setValue("公法线上偏差X1", (gear.btDeviation().x1()))
                .setValue("跨棒距一M1", gear.btDeviation().M1())
                .setValue("跨棒距上偏差Ms", gear.btDeviation().Ms())
                .setValue("公法线下偏差X2", gear.btDeviation().x2())
                .setValue("跨棒距二M2", gear.btDeviation().M2())
                .setValue("跨棒距下偏差Mx", gear.btDeviation().Mx())
                .setValue("公法线上偏差Ws", gear.sDeviation().Ws())
                .setValue("公法线下偏差Wx", gear.sDeviation().Wx())
                .setValue("跨棒距上偏差am1", gear.sDeviation().alphaM1())
                .setValue("跨棒距下偏差am2", gear.sDeviation().alphaM2());
    }

}
