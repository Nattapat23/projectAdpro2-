package se233.project_2.model.character;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import se233.project_2.view.GameStage;

import java.util.Random;

public class Turret extends Pane {
    private Rectangle visual;
    private int health;
    private int maxHealth;
    private long lastShotTime = 0;
    private long nextShotDelay = 1500 + new Random().nextInt(1500); // 1.5-3 วิ
    private boolean dead = false;

    // Health Bar
    private Rectangle healthBarBg;
    private Rectangle healthBarFg;
    private static final int HEALTH_BAR_WIDTH = 40;
    private static final int HEALTH_BAR_HEIGHT = 5;

    public Turret(int x, int y, int health) {
        this.health = health;
        this.maxHealth = health;

        // สร้างตัวป้อมปืน (สี่เหลี่ยมสีเทา)
        visual = new Rectangle(0, 0, 40, 40);
        visual.setFill(Color.rgb(100, 100, 100));
        visual.setStroke(Color.rgb(60, 60, 60));
        visual.setStrokeWidth(2);

        // ท่อปืน
        Rectangle barrel = new Rectangle(15, 35, 10, 20);
        barrel.setFill(Color.rgb(80, 80, 80));

        // Health bar
        healthBarBg = new Rectangle(0, -10, HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT);
        healthBarBg.setFill(Color.rgb(60, 60, 60));
        healthBarBg.setStroke(Color.BLACK);
        healthBarBg.setStrokeWidth(1);

        healthBarFg = new Rectangle(0, -10, HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT);
        healthBarFg.setFill(Color.rgb(255, 100, 0));

        setTranslateX(x);
        setTranslateY(y);
        getChildren().addAll(visual, barrel, healthBarBg, healthBarFg);
    }

    public void update(GameStage gameStage) {
        if (dead) return;
        long now = System.currentTimeMillis();
        if (now - lastShotTime > nextShotDelay) {
            gameStage.turretShoot((int)getTranslateX() + 20, (int)getTranslateY() + 55);
            lastShotTime = now;
            nextShotDelay = 1500 + new Random().nextInt(1500);
        }
    }

    public void takeDamage(int dmg) {
        if (dead) return;
        health -= dmg;
        if (health < 0) health = 0;

        // อัปเดต health bar
        double healthPercent = (double) health / maxHealth;
        healthBarFg.setWidth(HEALTH_BAR_WIDTH * healthPercent);

        if (health <= 0) {
            dead = true;
            this.setVisible(false);
        }
    }

    public boolean isDead() {
        return dead;
    }
}