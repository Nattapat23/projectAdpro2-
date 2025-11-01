package se233.project_2.model.character;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import se233.project_2.view.GameStage;

public abstract class BaseBoss extends Pane {
    protected Rectangle visual;
    protected int health;
    protected int maxHealth;
    protected boolean dead = false;
    protected boolean vulnerable = true; // บอสส่วนใหญ่จะถูกโจมตีได้ทันที

    // Health Bar
    protected Rectangle healthBarBg;
    protected Rectangle healthBarFg;
    protected static final int HEALTH_BAR_WIDTH = 150;
    protected static final int HEALTH_BAR_HEIGHT = 12;
    protected static final int HEALTH_BAR_OFFSET_Y = -25;

    public BaseBoss(int x, int y, int width, int height, int health, Color color) {
        this.health = health;
        this.maxHealth = health;

        // สร้างตัวบอส
        visual = new Rectangle(0, 0, width, height);
        visual.setFill(color);
        visual.setStroke(Color.rgb(40, 40, 40));
        visual.setStrokeWidth(3);

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
        getChildren().addAll(visual, healthBarBg, healthBarFg);
    }

    // Method ที่บอสแต่ละตัวต้อง implement เอง
    public abstract void update(GameStage gameStage);

    public void setVulnerable(boolean vulnerable) {
        this.vulnerable = vulnerable;
        if (vulnerable) {
            visual.setStroke(Color.YELLOW);
            visual.setStrokeWidth(5);
        } else {
            visual.setStroke(Color.rgb(40, 40, 40));
            visual.setStrokeWidth(3);
        }
    }

    public boolean isVulnerable() {
        return vulnerable;
    }

    public void takeDamage(int dmg) {
        if (dead || !vulnerable) return;

        health -= dmg;
        if (health < 0) health = 0;

        // อัปเดต health bar
        double healthPercent = (double) health / maxHealth;
        healthBarFg.setWidth(HEALTH_BAR_WIDTH * healthPercent);

        // เปลี่ยนสีตามเลือด
        if (healthPercent > 0.5) {
            healthBarFg.setFill(Color.rgb(220, 50, 50));
        } else if (healthPercent > 0.25) {
            healthBarFg.setFill(Color.rgb(255, 150, 0));
        } else {
            healthBarFg.setFill(Color.rgb(255, 50, 50));
        }

        if (health <= 0) {
            dead = true;
            onDeath();
        }
    }

    // เรียกเมื่อบอสตาย (สำหรับ animation หรือ effect)
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
}