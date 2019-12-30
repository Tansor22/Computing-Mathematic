package app;

import javafx.application.Application;
import javafx.stage.Stage;
import window.Window;
import window.WindowsManager;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        Window mainWindow = WindowsManager.getWindow("mainForm", "Численное интегрирование");
        mainWindow.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
