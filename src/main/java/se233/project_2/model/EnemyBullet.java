package se233.project_2.model;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import se233.project_2.view.GameStage;

public class EnemyBullet extends Pane {
    private Rectangle visual;
    private double vX, vY;
    private boolean hit = false;

    public EnemyBullet(int startX, int startY, int targetX, int targetY) {
        visual = new Rectangle(0, 0, 12, 12);
        visual.setFill(Color.ORANGE);

        setTranslateX(startX);
        setTranslateY(startY);
        getChildren().add(visual);

        // คำนวณทิศทางยิง
        double angle = Math.atan2(targetY - startY, targetX - startX);
        double speed = 5.0;
        vX = Math.cos(angle) * speed;
        vY = Math.sin(angle) * speed;
    }

    public void update() {
        setTranslateX(getTranslateX() + vX);
        setTranslateY(getTranslateY() + vY);
    }

    public boolean isOffscreen() {
        return getTranslateX() < 0 || getTranslateX() > GameStage.WIDTH ||
                getTranslateY() < 0 || getTranslateY() > GameStage.HEIGHT;
    }

    public boolean isHit() {
        return hit;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }
}