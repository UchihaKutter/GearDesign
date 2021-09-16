module GearDesign {
    requires javafx.graphics;
    requires org.jetbrains.annotations;
    requires javafx.fxml;
    requires javafx.controls;
    requires java.desktop;
    requires javafx.swing;
    requires jlatexmath;
    requires org.slf4j;
    requires java.sql;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires commons.math3;

    opens geardesigner to javafx.fxml;
    opens geardesigner.controls to javafx.fxml;
    opens geardesigner.beans to com.fasterxml.jackson.databind;
    exports geardesigner;
}