package se233.project_2.model;

import javafx.scene.input.KeyCode;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Keys {
    private final Set<KeyCode> pressed = ConcurrentHashMap.newKeySet();

    public void add(KeyCode code)    { pressed.add(code); }
    public void remove(KeyCode code) { pressed.remove(code); }
    public boolean isPressed(KeyCode code) { return pressed.contains(code); }
}