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
        boolean leftPressed = keys.isPressed(c.getLeftKey());
        boolean rightPressed = keys.isPressed(c.getRightKey());
        boolean upPressed = keys.isPressed(c.getUpKey());
        boolean downPressed = keys.isPressed(c.getDownKey());
        boolean shoot = keys.isPressed(c.getShootKey());
        boolean specialShoot = keys.isPressed(c.getSpecialShootKey()); // เพิ่มบรรทัดนี้

        if (leftPressed && rightPressed) {
            c.stop();
        } else if (leftPressed && rightPressed && downPressed) {
            c.traceCrouch();
            c.crouch();
        } else if (leftPressed) {
            c.getImageView().tick();
            c.moveLeft();
        } else if (rightPressed) {
            c.getImageView().tick();
            c.moveRight();
        } else {
            c.stop();
        }

        if (upPressed) {
            c.traceJump();
            c.getImageView().tick();
            c.jump();
        }

        if (downPressed) {
            c.getImageView().tick();
            c.traceCrouch();
            c.crouch();
        } else {
            c.stopCrouch();
        }

        // ยิงธรรมดา (Space)
        if (shoot) {
            c.traceShoot();
            c.shoot();
            gameStage.playerShoot();
        } else {
            c.stopShoot();
        }

        // ยิงพิเศษ (Z) - เพิ่มส่วนนี้
        if (specialShoot) {

            c.specialShoot();
            gameStage.playerSpecialShoot();
        } else {
            c.stopShoot();
        }

        c.update();
    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            long start = System.currentTimeMillis();
            GameCharacter c = gameStage.getGameCharacter();
            Keys keys = gameStage.getKeys();

            javafx.application.Platform.runLater(() -> {
                update(c, keys);
                gameStage.updateBoss();
                gameStage.updateTurrets();
                gameStage.updateBullets();
                gameStage.updateEnemyBullets();
                gameStage.updateMinions();
                gameStage.checkCollisions();
                gameStage.checkGameState();
            });

            long elapsed = System.currentTimeMillis() - start;
            float sleep = intervalMs - elapsed;
            if (sleep < 1) sleep = 1;
            try {
                Thread.sleep((long) sleep);
            } catch (InterruptedException ignored) {}
        }
    }
}