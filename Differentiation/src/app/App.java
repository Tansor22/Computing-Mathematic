package app;

import javafx.application.Application;
import javafx.stage.Stage;
import window.Window;
import window.WindowsManager;

import java.util.concurrent.ThreadLocalRandom;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
         Window mainWindow = WindowsManager.getWindow("mainForm", "Дифференцирование");
         mainWindow.getStage().setMaximized(true);
		 mainWindow.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
