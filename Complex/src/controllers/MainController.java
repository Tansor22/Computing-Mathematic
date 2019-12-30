package controllers;



import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import window.Window;


import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class MainController extends Controller {
    @FXML
    private ListView<Controller> programmesListView;
    @FXML
    private BorderPane root;
    private String programmesPath = File.separator + "resources" +
            File.separator + "fxml" + File.separator + "programmes";

    private void setup() {
        File programmesDirectory = new File("src" + programmesPath);
        if (programmesDirectory.isDirectory()) {
            for (String fileName : Objects.requireNonNull(programmesDirectory.list())) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(".." +
                        programmesPath + File.separator + fileName));
                try {
                    loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                programmesListView.getItems().add(loader.getController());
            }
        }

        programmesListView.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            root.setCenter(programmesListView.getItems().get(newValue.intValue()).scene.getRoot());
        });
    }

    @Override
    public void beforeShow() {
        setup();
    }
}
