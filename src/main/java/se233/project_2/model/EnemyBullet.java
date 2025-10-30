package se233.project_2.model;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import se233.project_2.view.GameStage;

public class EnemyBullet extends Pane {
    private int x, y;
    private int velocityX;
    private int velocityY; // ADDED: เพิ่มความเร็วแกน Y
    private Rectangle view;

    /**
     * Constructor for an enemy bullet.
     * @param x Start X position
     * @param y Start Y position
     * @param velX Velocity in X direction
     * @param velY Velocity in Y direction
     */
    public EnemyBullet(int x, int y, int velX, int velY) {
        this.x = x;
        this.y = y;
        this.velocityX = velX;
        this.velocityY = velY;

        // ทำให้กระสุนศัตรูดูแตกต่าง
        // คุณอาจจะใช้ Circle(5, Color.RED) ก็ได้ถ้าอยากให้เป็นวงกลม
        this.view = new Rectangle(10, 10, Color.RED);

        setTranslateX(x);
        setTranslateY(y);
        getChildren().add(view);
    }

    /**
     * อัปเดตตำแหน่งกระสุน
     */
    public void update() {
        x += velocityX;
        y += velocityY; // MODIFIED: อัปเดตตำแหน่งแกน Y ด้วย
        setTranslateX(x);
        setTranslateY(y);
    }

    /**
     * ตรวจสอบว่ากระสุนตกขอบจอหรือยัง
     */
    public boolean isOffScreen() {
        // MODIFIED: ตรวจสอบขอบบนและล่างด้วย
        return x <= 0 || x >= GameStage.WIDTH || y <= 0 || y >= GameStage.HEIGHT;
    }

    // (สำหรับการเช็คการชนใน Gameloop.java)
    // คุณสามารถใช้เมธอด .getBoundsInParent().intersects(player.getBoundsInParent())
    // ไม่จำเป็นต้องสร้างเมธอด checkCollision() ในนี้
}