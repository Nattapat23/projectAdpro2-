package se233.project_2.model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se233.project_2.Exception.SpriteException;
import se233.project_2.controller.AudioFeatures;
import se233.project_2.model.character.Boss3MEENEO;
import se233.project_2.view.GameStage;

import java.util.concurrent.TimeUnit;

public class Bullet extends Pane {
    private static final Logger logger = LogManager.getLogger(Bullet.class);
    private AnimatedSprite imageView;
    private Rectangle visual;
    private int xVelocity;
    private int direction; // 1 = right, -1 = left
    private boolean hit = false;
    private int dmg = 20;

    private AnimatedSprite explosionSprite;
    private String explosionImagePath = "/se233/project_2/boomBullet.png";
    private int explosionFrames = 3; // จำนวนเฟรมของแอนิเมชัน
    private int explosionWidth = 35;
    private int explosionHeight = 34;
    private boolean exploding = false; // กำลังแสดงเอฟเฟค
    private boolean readyToRemove = false;
    public Bullet(int x, int y, int direction) {
        this.direction = direction;
        this.xVelocity = 15 * direction; // ความเร็ว

        visual = new Rectangle(0, 0, 10, 5); // ขนาดกระสุน
        visual.setFill(Color.YELLOW);

        setTranslateX(x);
        setTranslateY(y);
        getChildren().add(visual);
    }

    public Bullet(int x, int y, int direction, String imagePath, int width, int height) {
        this.direction = direction;
        this.xVelocity = 15 * direction;

        try {
            Image img = new Image(getClass().getResourceAsStream(imagePath));
            imageView = new AnimatedSprite(img,0,1,1, 0,0,width,height);
            imageView.setFitWidth(width/10);
            imageView.setFitHeight(height/10);
            imageView.setPreserveRatio(true);
        } catch (NullPointerException e) {

            javafx.scene.shape.Rectangle fallback = new javafx.scene.shape.Rectangle(0, 0, width, height);
            fallback.setFill(javafx.scene.paint.Color.YELLOW);
            getChildren().add(fallback);
            throw new SpriteException("Not Found Paht" +e);
        }
            // ถ้าโหลดรูปไม่ได้ ใช้สีเหลืองแทน



        setTranslateX(x);
        setTranslateY(y);
        if (imageView != null) {
            getChildren().add(imageView);
        }
    }


    public void update() {
        if (exploding) {
            // กำลังแสดงเอฟเฟค - อัพเดทแอนิเมชัน
            if (explosionSprite != null) {
                explosionSprite.tick();

                // ถ้าแอนิเมชันเล่นครบแล้ว
                if (explosionSprite.getCurColumnIndex() >= explosionFrames - 1) {
                    readyToRemove = true;
                }
            }
        } else {
            // เคลื่อนที่ปกติ
            setTranslateX(getTranslateX() + xVelocity);
        }
    }


    public void triggerExplosion() {
        if (exploding) return; // ป้องกันเรียกซ้ำ

        exploding = true;
        hit = true;

        // ซ่อนกระสุนเดิม
        if (imageView != null) {
            imageView.setVisible(false);
        }
        if (visual != null) {
            visual.setVisible(false);
        }

        // สร้างเอฟเฟคระเบิด
        try {
            Image explosionImg = new Image(getClass().getResourceAsStream(explosionImagePath));
            explosionSprite = new AnimatedSprite(
                    explosionImg,
                    0,
                    explosionFrames,
                    1,
                    0,
                    0,
                    explosionWidth,
                    explosionHeight
            );
            explosionSprite.setFitWidth(explosionWidth );
            explosionSprite.setFitHeight(explosionHeight );
            explosionSprite.setPreserveRatio(true);

            // วางให้อยู่กึ่งกลาง
            explosionSprite.setTranslateX(-explosionWidth / 4);
            explosionSprite.setTranslateY(-explosionHeight / 4);

            getChildren().add(explosionSprite);
        } catch (NullPointerException e) {

            logger.error("Cannot load explosion effect: " + e.getMessage());
            readyToRemove = true;
            throw new SpriteException("Not Found Paht" +e);
        }
            // ถ้าโหลดเอฟเฟคไม่ได้ ให้ลบทันที


    }
    public boolean isOffscreen() {
        return getTranslateX() < 0 || getTranslateX() > GameStage.WIDTH;
    }

    public boolean isHit() {
        return hit;
    }

    public void setHit(boolean hit) {
        AudioFeatures.playHitSound();
        if (hit && !this.hit) {
            triggerExplosion();
        }
        this.hit = hit;
    }
    public int getDmg(){
        return dmg;
    }

    public void setExplosionEffect(String imagePath, int frames, int width, int height) {
        this.explosionImagePath = imagePath;
        this.explosionFrames = frames;
        this.explosionWidth = width;
        this.explosionHeight = height;
    }
}