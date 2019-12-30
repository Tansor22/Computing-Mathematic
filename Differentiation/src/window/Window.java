package window;

import controllers.Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;


public class Window{
    private FXMLLoader fxml;
    private Parent layout;
    private Stage stage;
    private Scene scene;
    private Controller controller;
    private boolean isShown;

    protected Window(String fxmlName) {
        try {
            fxml = new FXMLLoader(getClass().getResource("../resources/fxml/" + fxmlName + ".fxml"));
            layout = fxml.load();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        stage = new Stage();
        scene = new Scene(layout);
        controller = fxml.getController();
        controller.setScene(scene);
        stage.getIcons().add(new Image("/resources/img/icon.png"));
        stage.setResizable(true);
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> isShown = false);
    }

    public Controller getController() {
        return controller;
    }
    public Stage getStage() { return stage;}
    public boolean isShown() {
        return isShown;
    }

    public void show() {
        if (!isShown) {
            stage.show();
            controller.beforeShow();
            isShown = true;
        }
    }
}
