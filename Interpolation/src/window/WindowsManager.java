package window;

import java.util.HashMap;

public class WindowsManager {
    private static HashMap<String, Window> instances = new HashMap<>(4);

    private WindowsManager() {

    }

    public static Window getWindow(String fxml) {
        if (instances.containsKey(fxml)) {
            return instances.get(fxml);
        } else {
            Window instance = new Window(fxml);
            instances.put(fxml, instance);
            return instance;

        }
    }
}
