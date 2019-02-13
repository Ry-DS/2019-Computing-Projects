package me.ryan_s;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Labeled;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;

import java.util.function.Supplier;

public enum Unit {
    TIME("seconds", Unit::genericNumberField),LENGTH("meters",Unit::genericNumberField);

    private String unit;
    private Supplier<Node> input;

    Unit(String unit, Supplier<Node> input) {

        this.unit = unit;
        this.input = input;
    }

    public String getUnitName() {
        return unit;
    }

    public Supplier<Node> getInput() {
        return input;
    }
    private static TextField genericNumberField(){
        TextField textField=new TextField();
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,7}([\\.]\\d{0,4})?")) {
                textField.setText(oldValue);
            }
        });
        textField.setAlignment(Pos.CENTER_RIGHT);

        return textField;
    }
//TODO just pretends its a textfield since we don't have anything else here. Will break if new fancy unit comes
    public String read(Node node) {
        return ((TextInputControl)node).getText();
    }
}
