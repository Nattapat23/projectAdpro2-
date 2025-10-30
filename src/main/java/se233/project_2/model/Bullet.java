package se233.project_2.model;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import se233.project_2.view.GameStage;

public class Bullet extends Pane {
    private int x, y;
    private int velocityX;
    private Rectangle view;

    public Bullet(int x, int y, int direction) {
        this.x = x;
        this.y = y;
        this.velocityX = 15 * direction; // direction 1 = ซ้าย, -1 = ขวา

        this.view = new Rectangle(8, 4, Color.YELLOW); // ขนาดกระสุน
        setTranslateX(x);
        setTranslateY(y);
        getChildren().add(view);
    }

    public void update() {
        x += velocityX;
        setTranslateX(x);
    }

    public boolean isOffScreen() {
        return x <= 0 || x >= GameStage.WIDTH;
    }

    public Rectangle getView() {
        return view;
    }

    // TODO: เพิ่มเมธอดเช็คการชน (checkCollision)
    // public boolean checkCollision(Boss1 boss) { ... }
}