/**
 * Created by SimplyBallistic on 24/02/2019
 *
 * @author SimplyBallistic
 **/module javafx.athletics.carnival {

    requires javafx.controls;
    requires javafx.fxml;
    requires controlsfx;
    requires com.fasterxml.jackson.dataformat.xml;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;

    opens me.ryan_s.xml to javafx.graphics, javafx.fxml;
    opens me.ryan_s.athletics to javafx.graphics, javafx.fxml;
    opens me.ryan_s.movie to javafx.graphics, javafx.fxml;
    opens me.ryan_s.xml.data to com.fasterxml.jackson.databind, javafx.base;

}