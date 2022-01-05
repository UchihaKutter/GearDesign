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

    opens geardesigner;
    opens geardesigner.controls to javafx.fxml;
    exports geardesigner;
    exports geardesigner.beans;
    opens geardesigner.beans;
}