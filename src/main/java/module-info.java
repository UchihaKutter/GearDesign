module GearDesign {
    requires org.jetbrains.annotations;
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.swing;
    requires java.desktop;
    requires jlatexmath;
    requires org.slf4j;
    requires java.sql;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires commons.math3;

    opens geardesigner;
    opens geardesigner.controls to javafx.fxml;
    opens geardesigner.beans to com.fasterxml.jackson.databind;
    exports geardesigner;
}