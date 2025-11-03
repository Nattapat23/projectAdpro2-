package se233.project_2.model;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se233.project_2.Exception.SpriteException;
import se233.project_2.view.GameStage;

public class SpecialBullet extends Bullet {
    private static final Logger logger = LogManager.getLogger(SpecialBullet.class);
    private AnimatedSprite specialImageView;
    private Rectangle specialVisual;
    private int xVelocity;
    private int direction;
    private boolean hit = false;
    private int dmg = 100; // ดาเมจสูงกว่ากระสุนธรรมดา (ธรรมดา = 30)

    private AnimatedSprite explosionSprite;
    private String explosionImagePath = "/se233/project_2/boomBullet.png";
    private int explosionFrames = 3;
    private int explosionWidth = 50;
    private int explosionHeight = 50;
    private boolean exploding = false;
    private boolean readyToRemove = false;


    public SpecialBullet(int x, int y, int direction) {
        super(x, y, direction);
        this.direction = direction;
        this.xVelocity = 12 * direction; // ช้ากว่ากระสุนธรรมดาเล็กน้อย


        getChildren().clear();


        specialVisual = new Rectangle(0, 0, 20, 10);
        specialVisual.setFill(Color.RED); // สีแดงสำหรับกระสุนพิเศษ

        setTranslateX(x);
        setTranslateY(y);
        getChildren().add(specialVisual);
    }


    public SpecialBullet(int x, int y, int direction, String imagePath, int width, int height) {
        super(x, y, direction); // เรียก parent constructor
        this.direction = direction;
        this.xVelocity = 12 * direction;


        getChildren().clear();

        try {
            Image img = new Image(getClass().getResourceAsStream(imagePath));
            specialImageView = new AnimatedSprite(img, 0, 1, 1, 0, 0, width, height);
            specialImageView.setFitWidth(width / 6);
            specialImageView.setFitHeight(height / 6);
            specialImageView.setPreserveRatio(true);
        } catch (NullPointerException e) {
            Rectangle fallback = new Rectangle(0, 0, 20, 10);
            fallback.setFill(Color.RED);
            getChildren().add(fallback);
            throw new SpriteException("Not Found Path: " + e);
        }

        setTranslateX(x);
        setTranslateY(y);
        if (specialImageView != null) {
            getChildren().add(specialImageView);
        }
    }

    @Override
    public void update() {
        if (exploding) {
            if (explosionSprite != null) {
                explosionSprite.tick();
                if (explosionSprite.getCurColumnIndex() >= explosionFrames - 1) {
                    readyToRemove = true;
                }
            }
        } else {
            setTranslateX(getTranslateX() + xVelocity);
        }
    }

    @Override
    public void triggerExplosion() {
        if (exploding) return;

        exploding = true;
        hit = true;

        if (specialImageView != null) {
            specialImageView.setVisible(false);
        }
        if (specialVisual != null) {
            specialVisual.setVisible(false);
        }

        try {
            Image explosionImg = new Image(getClass().getResourceAsStream(explosionImagePath));
            explosionSprite = new AnimatedSprite(
                    explosionImg, 0, explosionFrames, 1, 0, 0,
                    explosionWidth, explosionHeight
            );
            explosionSprite.setFitWidth(explosionWidth);
            explosionSprite.setFitHeight(explosionHeight);
            explosionSprite.setPreserveRatio(true);
            explosionSprite.setTranslateX(-explosionWidth / 4);
            explosionSprite.setTranslateY(-explosionHeight / 4);

            getChildren().add(explosionSprite);
        } catch (NullPointerException e) {
            logger.error("Cannot load explosion effect: " + e.getMessage());
            readyToRemove = true;
            throw new SpriteException("Not Found Path: " + e);
        }
    }

    @Override
    public boolean isOffscreen() {
        return getTranslateX() < 0 || getTranslateX() > GameStage.WIDTH;
    }

    @Override
    public boolean isHit() {
        return hit;
    }

    @Override
    public void setHit(boolean hit) {
        if (hit && !this.hit) {
            triggerExplosion();
        }
        this.hit = hit;
    }

    @Override
    public int getDmg() {
        return dmg; // ดาเมจสูง 100
    }
}