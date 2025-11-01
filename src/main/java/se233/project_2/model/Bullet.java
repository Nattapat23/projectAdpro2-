package se233.project_2.model;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import se233.project_2.view.GameStage;

public class Bullet extends Pane {
    private Rectangle visual;
    private int xVelocity;
    private int direction; // 1 = right, -1 = left
    private boolean hit = false;

    public Bullet(int x, int y, int direction) {
        this.direction = direction;
        this.xVelocity = 15 * direction; // ความเร็ว

        visual = new Rectangle(0, 0, 10, 5); // ขนาดกระสุน
        visual.setFill(Color.YELLOW);

        setTranslateX(x);
        setTranslateY(y);
        getChildren().add(visual);
    }

    public void update() {
        setTranslateX(getTranslateX() + xVelocity);
    }

    public boolean isOffscreen() {
        return getTranslateX() < 0 || getTranslateX() > GameStage.WIDTH;
    }

    public boolean isHit() {
        return hit;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }
}