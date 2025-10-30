package se233.project_2.model;

import javafx.scene.input.KeyCode;
import java.util.HashMap;

public class Keys {
    private HashMap<KeyCode, Boolean> keys = new HashMap<>();

    public boolean isPressed(KeyCode key) {
        return keys.getOrDefault(key, false);
    }

    public void press(KeyCode key) {
        keys.put(key, true);
    }

    public void release(KeyCode key) {
        keys.put(key, false);
    }
}