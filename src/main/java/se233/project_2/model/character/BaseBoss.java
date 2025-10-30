package se233.project_2.model.character;

import javafx.scene.layout.Pane;
import se233.project_2.model.AnimatedSprite;

// 1. สร้างเป็น abstract class
public abstract class BaseBoss extends Pane {

    // 2. ย้ายคุณสมบัติร่วมมาไว้ที่นี่
    protected int health;
    protected boolean isDead = false;
    protected AnimatedSprite imageView;
    protected int scoreValue; // ADDED: คะแนนที่จะให้ผู้เล่น

    public BaseBoss(int x, int y, int initialHealth, int scoreValue) {
        setTranslateX(x);
        setTranslateY(y);
        this.health = initialHealth;
        this.scoreValue = scoreValue;
    }

    // 3. สร้าง abstract method ให้คลาสลูกไป implement เอง
    // AI ของบอสแต่ละตัวไม่เหมือนกัน
    public abstract void updateLogic();

    // 4. สร้างเมธอดที่ใช้ร่วมกัน
    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0 && !isDead) {
            isDead = true;
            onDeath(); // เรียกเมธอดเมื่อตาย
        }
    }

    protected void onDeath() {
        // TODO: เล่นแอนิเมชันระเบิด
        setVisible(false); // ซ่อนบอส
    }

    public boolean isDead() {
        return isDead;
    }

    public int getScoreValue() {
        return scoreValue;
    }

    public AnimatedSprite getImageView() {
        return imageView;
    }
}