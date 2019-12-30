package app;

import javafx.application.Application;
import javafx.stage.Stage;
import window.WindowsManager;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        WindowsManager.getWindow("mainForm").show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
