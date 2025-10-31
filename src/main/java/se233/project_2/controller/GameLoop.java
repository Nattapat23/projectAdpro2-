package se233.project_2.controller;

import se233.project_2.model.Keys;
import se233.project_2.model.character.GameCharacter;
import se233.project_2.view.GameStage;

public class GameLoop implements Runnable {
    private final GameStage gameStage;
    private final int frameRate = 30;
    private final float intervalMs = 1000.0f / frameRate;
    private volatile boolean running = true;

    public GameLoop(GameStage gameStage) {
        this.gameStage = gameStage;
    }

    private void update(GameCharacter c, Keys keys) {

        boolean leftPressed  = keys.isPressed(c.getLeftKey());
        boolean rightPressed = keys.isPressed(c.getRightKey());
        boolean upPressed    = keys.isPressed(c.getUpKey());

        if (leftPressed && rightPressed) {
            c.stop();
        } else if (leftPressed) {
            c.getImageView();
            c.moveLeft();
        } else if (rightPressed) {
            c.getImageView();
            c.moveRight();
        } else {
            c.stop();
        }
        if (upPressed) {
            c.getImageView(); c.jump();
        }

        c.update();
        c.checkReachGameWall();
        c.checkReachHighest();
        c.checkReachFloor();

    }

    @Override
    public void run() {
        while (running) {
            long start = System.currentTimeMillis();

            GameCharacter c = gameStage.getGameCharacter();
            Keys keys = gameStage.getKeys();
            javafx.application.Platform.runLater(() -> {
                update(c, keys);

            });

            long elapsed = System.currentTimeMillis() - start;
            float sleep = intervalMs - elapsed;
            if (sleep < 1) sleep = 1;
            try {
                Thread.sleep((long)sleep);
            } catch (InterruptedException ignored) {}
        }
    }
}