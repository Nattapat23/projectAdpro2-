package se233.project_2.controller;

import javafx.animation.AnimationTimer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.control.Label;
import se233.project_2.model.Bullet;
import se233.project_2.model.character.BaseBoss;
import se233.project_2.model.character.GameCharacter;
import se233.project_2.view.GameStage;
import java.util.ArrayList;

// MODIFIED: นี่คือ AnimationTimer ของเรา
public class Drawingloop extends AnimationTimer {

    private GameStage gameStage;
    private Gameloop gameloop; // MODIFIED: เก็บ Logic Loop
    private GameCharacter character;
    private BaseBoss currentBoss;

    public Drawingloop(GameStage gameStage, Gameloop gameloop) {
        this.gameStage = gameStage;
        this.gameloop = gameloop;
        this.character = gameloop.getCharacter();
    }

    @Override
    public void handle(long now) {
        // --- 1. ดึงข้อมูลล่าสุดจาก Gameloop ---
        character = gameloop.getCharacter();
        currentBoss = gameloop.getCurrentBoss();

        // --- 2. สั่งให้ Model อัปเดตแอนิเมชัน (tick()) ---
        if (character != null) {
            character.getImageView().tick();
        }
        if (currentBoss != null) {
            currentBoss.getImageView().tick();
        }

        // --- 3. จัดการการเพิ่ม/ลบ Entities (กระสุน) ---
        // (ทำในนี้เพราะต้องรันบน JavaFX Thread)

        // ลบกระสุนที่ชนแล้ว
        ArrayList<Bullet> bulletsToRemove = new ArrayList<>();
        for (Bullet bullet : gameloop.getBullets()) {
            if (!gameStage.getChildren().contains(bullet)) {
                // ถ้ายังไม่มีในฉาก ให้เพิ่มเข้าไป
                gameStage.getChildren().add(bullet);
            }
            if (bullet.isOffScreen() || !bullet.isVisible()) { // สมมติว่ากระสุนที่ชนจะ setVisible(false)
                bulletsToRemove.add(bullet);
            }
        }

        // Cleanup
        gameloop.getBullets().removeAll(bulletsToRemove);
        gameStage.getChildren().removeAll(bulletsToRemove);

        // --- 4. อัปเดต UI (Score/Lives) ---
        gameStage.getScoreList().update(character.getScore());
        // TODO: อัปเดต UI ชีวิต (Lives)
        // gameStage.getLivesDisplay().update(character.getLives());

        // --- 5. ตรวจสอบสถานะจบเกม ---
        if (gameloop.isGameWon()) {
            showGameScreen("YOU WIN!", Color.GREEN);
            this.stop();
        } else if (gameloop.isPlayerLost()) {
            showGameScreen("GAME OVER", Color.RED);
            this.stop();
        }
    }

    private void showGameScreen(String message, Color color) {
        Label label = new Label(message);
        label.setFont(new Font("Arial", 60));
        label.setTextFill(color);
        label.setTranslateX(GameStage.WIDTH / 2 - 150);
        label.setTranslateY(GameStage.HEIGHT / 2 - 30);
        gameStage.getChildren().add(label);
    }
}