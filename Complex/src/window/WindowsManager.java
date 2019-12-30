package window;

import java.util.HashMap;

public class WindowsManager {
    private static HashMap<String, Window> instances = new HashMap<>(4);

    private WindowsManager() {

    }

    public static Window getWindow(String fxml) {
        return getWindow(fxml, fxml);
    }

    public static Window getWindow(String fxml, String windowTitle) {
        if (instances.containsKey(fxml)) {
            Window instance = instances.get(fxml);
            instance.getStage().setTitle(windowTitle);
            return instance;
        } else {
            Window instance = new Window(fxml);
            instance.getStage().setTitle(windowTitle);
            instances.put(fxml, instance);
            return instance;

        }
    }
}
