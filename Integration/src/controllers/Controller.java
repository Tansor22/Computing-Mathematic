package controllers;

import javafx.fxml.Initializable;
import javafx.scene.Scene;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class Controller implements Initializable {
    protected Scene scene;
    public void setScene(Scene scene) {
        this.scene = scene;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {}
    public void beforeShow() {}

}
