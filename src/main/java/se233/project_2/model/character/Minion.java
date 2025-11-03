package se233.project_2.model.character;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import se233.project_2.Exception.SpriteException;
import se233.project_2.model.AnimatedSprite;
import se233.project_2.view.GameStage;

public class Minion extends Pane {
    private AnimatedSprite imageView;
    private int health = 20;
    private boolean dead = false;
    private boolean hit = false;
    private double vX, vY;
    public int target;
    // Health bar
    private Rectangle healthBarBg;
    private Rectangle healthBarFg;
    private static final int HEALTH_BAR_WIDTH = 30;
    private static final int HEALTH_BAR_HEIGHT = 4;

    public Minion(int x, int y, String imagePath, int targetX, int targetY) {
        try {
            Image img = new Image(getClass().getResourceAsStream(imagePath));
            imageView = new AnimatedSprite(img,3,3,1, 0,0,25,33);
            imageView.setFitWidth(80);
            imageView.setFitHeight(80);
            imageView.setPreserveRatio(true);
        } catch (NullPointerException e) {
            Rectangle fallback = new Rectangle(0, 0, 40, 40);
            fallback.setFill(Color.RED);
            getChildren().add(fallback);

            throw new SpriteException("Not Found Paht" +e);
        }



        // Health bar
        healthBarBg = new Rectangle(5, -8, HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT);
        healthBarBg.setFill(Color.rgb(60, 60, 60));

        healthBarFg = new Rectangle(5, -8, HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT);
        healthBarFg.setFill(Color.rgb(255, 100, 100));

        double angle = Math.atan2(targetY - y, targetX - x);
        double speed = 5.0;
        vX = Math.cos(angle) * speed;
        vY = Math.sin(angle) * speed;
        setTranslateX(x);
        setTranslateY(y);

        if (imageView != null) {
            getChildren().add(imageView);
        }
        getChildren().addAll(healthBarBg, healthBarFg);
    }

    public void update() {
        if (dead) return;

        // เคลื่อนที่ไปทางซ้าย
            setTranslateX(getTranslateX() + vX);
            setTranslateY(getTranslateY() + vY);
            imageView.tick();
    }

    public void takeDamage(int dmg) {
        if (dead) return;
        health -= dmg;
        if (health < 0) health = 0;

        // อัปเดต health bar
        double healthPercent = health / 20.0;
        healthBarFg.setWidth(HEALTH_BAR_WIDTH * healthPercent);

        if (health <= 0) {
            dead = true;
            this.setVisible(false);
        }
    }

    public boolean isOffscreen() {
        return getTranslateX() < -50 || getTranslateX() > GameStage.WIDTH + 50;
    }

    public boolean isDead() {
        return dead;
    }

    public boolean isHit() {
        return hit;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }
}