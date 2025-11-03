package se233.project_2.model;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se233.project_2.Exception.SpriteException;
import se233.project_2.Launcher;
import se233.project_2.controller.AudioFeatures;
import se233.project_2.model.character.Boss3MEENEO;
import se233.project_2.view.GameStage;

public class EnemyBullet extends Pane {
    private static final Logger logger = LogManager.getLogger(EnemyBullet.class);
    private AnimatedSprite imageView;
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
    public EnemyBullet(int startX, int startY, int targetX, int targetY,String path) {
        try {
            Image img = new Image(Launcher.class.getResourceAsStream(path));
            imageView = new AnimatedSprite(img,0,1,1, 0,0,165,156);
            imageView.setFitWidth(40);
            imageView.setFitHeight(40);
            imageView.setPreserveRatio(true);
        } catch (NullPointerException e) {

            logger.error("Error loading image");
            throw new SpriteException("Not Found Paht" +e);
        }


        setTranslateX(startX);
        setTranslateY(startY);
        if (imageView != null) {
            getChildren().add(imageView);
        }
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
        AudioFeatures.playHitSound();
        this.hit = hit;
    }
}