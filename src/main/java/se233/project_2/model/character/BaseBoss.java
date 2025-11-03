package se233.project_2.model.character;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se233.project_2.Exception.SpriteException;
import se233.project_2.Launcher;
import se233.project_2.model.AnimatedSprite;
import se233.project_2.view.GameStage;

import java.util.stream.BaseStream;

public abstract class BaseBoss extends Pane {
    private static final Logger logger = LogManager.getLogger(BaseBoss.class);
    protected AnimatedSprite imageView;
    protected Rectangle visual; // fallback ถ้าไม่มีรูป
    protected int health;
    protected int maxHealth;
    protected boolean dead = false;
    protected boolean vulnerable = true;
    protected boolean ScoreAfterDie = false;
    protected Image img ;

    // Health Bar
    protected Rectangle healthBarBg;
    protected Rectangle healthBarFg;
    protected static final int HEALTH_BAR_WIDTH = 150;
    protected static final int HEALTH_BAR_HEIGHT = 12;
    protected static final int HEALTH_BAR_OFFSET_Y = -25;

    protected double width;
    protected int height;

    public BaseBoss(int x, int y, int width, int height, int health, Color color, String imagePath,int colum,int rows) {
        this.health = health;
        this.maxHealth = health;
        this.width = width;
        this.height = height;

        // พยายามโหลดรูป
        if (imagePath != null) {
            try {
                img = new Image(Launcher.class.getResourceAsStream(imagePath));
                imageView = new AnimatedSprite(img,colum,colum,rows, 0,0, width,height);
                imageView.setFitWidth(width);
                imageView.setFitHeight(height);
                imageView.setPreserveRatio(false);
                getChildren().add(imageView);
            } catch (NullPointerException e) {
                createFallbackVisual(width, height, color);
                throw new SpriteException("Not Found Paht" +e);
            }
        } else {
            createFallbackVisual(width, height, color);
        }

        // Health bar
        healthBarBg = new Rectangle(
                (width - HEALTH_BAR_WIDTH) / 2.0,
                HEALTH_BAR_OFFSET_Y,
                HEALTH_BAR_WIDTH,
                HEALTH_BAR_HEIGHT
        );
        healthBarBg.setFill(Color.rgb(60, 60, 60));
        healthBarBg.setStroke(Color.BLACK);
        healthBarBg.setStrokeWidth(1);

        healthBarFg = new Rectangle(
                (width - HEALTH_BAR_WIDTH) / 2.0,
                HEALTH_BAR_OFFSET_Y,
                HEALTH_BAR_WIDTH,
                HEALTH_BAR_HEIGHT
        );
        healthBarFg.setFill(Color.rgb(220, 50, 50));

        setTranslateX(x);
        setTranslateY(y);
        getChildren().addAll(healthBarBg, healthBarFg);
    }

    private void createFallbackVisual(int width, int height, Color color) {
        visual = new Rectangle(0, 0, width, height);
        visual.setFill(color);
        visual.setStroke(Color.rgb(40, 40, 40));
        visual.setStrokeWidth(3);
        getChildren().add(visual);
    }

    public abstract void update(GameStage gameStage);

    public void setVulnerable(boolean vulnerable) {
        this.vulnerable = vulnerable;
        if (imageView != null) {
            if (vulnerable) {
                imageView.setStyle("-fx-effect: dropshadow(gaussian, yellow, 10, 0.7, 0, 0);");
            } else {
                imageView.setStyle("");
            }
        } else if (visual != null) {
            if (vulnerable) {
                visual.setStroke(Color.YELLOW);
                visual.setStrokeWidth(5);
            } else {
                visual.setStroke(Color.rgb(40, 40, 40));
                visual.setStrokeWidth(3);
            }
        }
    }

    public boolean isVulnerable() {
        return vulnerable;
    }

    public void takeDamage(int dmg) {
        if (dead || !vulnerable) return;

        health -= dmg;
        if (health < 0) health = 0;

        double healthPercent = (double) health / maxHealth;
        healthBarFg.setWidth(HEALTH_BAR_WIDTH * healthPercent);

        if (healthPercent > 0.5) {
            healthBarFg.setFill(Color.rgb(220, 50, 50));
        } else if (healthPercent > 0.25) {
            healthBarFg.setFill(Color.rgb(255, 150, 0));
        } else {
            healthBarFg.setFill(Color.rgb(255, 50, 50));
        }
        traceTakeDamage(dmg);
        if (health <= 0) {
            dead = true;
            ScoreAfterDie = true;
            onDeath();
        }
    }

    protected void onDeath() {
        this.setVisible(false);
    }

    public boolean isDead() {
        return dead;
    }

    public Rectangle getVisual() {
        return visual;
    }

    public int getHealth() {
        return health;
    }

    public boolean isScoreAfterDie() {
        return ScoreAfterDie;
    }

    public void setScoreAfterDie(boolean score){
        ScoreAfterDie =  score;
    }
     public void setImage(double size){
         imageView.setFitWidth(width*size);
         imageView.setFitHeight(height*size);
     }

     public AnimatedSprite getImageView() {
        return imageView;
     }

    public void setImageView(AnimatedSprite imageView) {
        this.imageView = imageView;
    }

    public void setWidth(double size) {
        this.width *= size;
    }

    public void setHeight(double size) {
        this.height *= size;
    }

    public void traceTakeDamage(int dmg) {
        logger.info("Player Take Damage! : "+ dmg);
    }

}