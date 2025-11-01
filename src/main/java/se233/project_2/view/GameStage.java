package se233.project_2.view;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import se233.project_2.model.Bullet;
import se233.project_2.model.EnemyBullet;
import se233.project_2.model.Keys;
import se233.project_2.model.character.*;
import se233.project_2.model.platFrom;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GameStage extends Pane {
    public static final int WIDTH = 900;
    public static final int HEIGHT = 500;
    public final static int GROUND = 440;

    private Image gameStageImg;
    private GameCharacter gameCharacter;
    private BaseBoss boss; // ใช้ BaseBoss แทน
    private Keys keys;
    private List<platFrom> platforms = new ArrayList<>();
    private List<Bullet> bullets = new ArrayList<>();
    private List<EnemyBullet> enemyBullets = new ArrayList<>();
    private List<Turret> turrets = new ArrayList<>();

    private int currentStage = 1;
    private int lives = 3;
    private int score = 0;

    private long lastPlayerShotTime = 0;
    private final long playerShotCooldown = 300;

    private Label infoLabel;
    private Label scoreLabel;
    private Label livesLabel;
    private Rectangle goalZone;
    private Pane rootPane;

    public GameStage() {
        keys = new Keys();
        gameStageImg = new Image(Objects.requireNonNull(GameStage.class.getResourceAsStream("/se233/project_2/Background.png")));
        ImageView background = new ImageView(gameStageImg);
        background.setFitWidth(WIDTH);
        background.setFitHeight(HEIGHT);

        gameCharacter = new GameCharacter( 30, 350, 32, 64, KeyCode.A, KeyCode.D, KeyCode.W, KeyCode.S, KeyCode.SPACE);

        infoLabel = new Label();
        infoLabel.setFont(new Font("Arial", 40));
        infoLabel.setTextFill(Color.WHITE);
        infoLabel.setTranslateX(WIDTH / 2.0 - 200);
        infoLabel.setTranslateY(HEIGHT / 2.0 - 100);
        infoLabel.setVisible(false);

        scoreLabel = new Label("SCORE: 0");
        scoreLabel.setFont(new Font("Arial", 24));
        scoreLabel.setTextFill(Color.WHITE);
        scoreLabel.setTranslateX(10);
        scoreLabel.setTranslateY(10);

        livesLabel = new Label("LIVES: 3");
        livesLabel.setFont(new Font("Arial", 24));
        livesLabel.setTextFill(Color.WHITE);
        livesLabel.setTranslateX(10);
        livesLabel.setTranslateY(45);

        getChildren().addAll(background, gameCharacter, infoLabel, scoreLabel, livesLabel);
    }

    public void setRootPane(Pane rootPane) {
        this.rootPane = rootPane;
    }

    public void startGame() {
        lives = gameCharacter.getLives();
        score = 0;
        currentStage = 1;
        updateScore(0);
        updateLives();
        setupStage(currentStage);
    }

    private void setupStage(int stageNumber) {
        // Clear ทุกอย่าง
        getChildren().removeAll(platforms);
        getChildren().removeAll(bullets);
        getChildren().removeAll(enemyBullets);
        getChildren().removeAll(turrets);
        if (boss != null) getChildren().remove(boss);
        if (goalZone != null) getChildren().remove(goalZone);

        platforms.clear();
        bullets.clear();
        enemyBullets.clear();
        turrets.clear();

        gameCharacter.respawn();

        showMessage("Stage " + stageNumber + " Start!", 1500);

        if (stageNumber == 1) {
            // แพลตฟอร์มตามรูป
            platforms.add(new platFrom(50, 380, 80, 15));
            platforms.add(new platFrom(150, 330, 80, 15));
            platforms.add(new platFrom(50, 280, 80, 15));
            platforms.add(new platFrom(150, 230, 80, 15));

            getChildren().addAll(platforms);
            gameCharacter.setPlatforms(platforms);

            // สร้างป้อมปืน
            Turret t1 = new Turret(760, 200, 30);
            Turret t2 = new Turret(810, 200, 30);
            turrets.add(t1);
            turrets.add(t2);
            getChildren().addAll(t1, t2);

            // บอสด่าน 1: FortifiedWall
            boss = new Boss1FortifiedWall(750, 250, turrets);
            getChildren().add(boss);

        } else if (stageNumber == 2) {
            gameCharacter.setPlatforms(platforms);

            // บอสด่าน 2: HeavyFortress
            boss = new Boss2HeavyFortress(750, 100);
            getChildren().add(boss);

        } else if (stageNumber == 3) {
            platforms.add(new platFrom(50, 380, 80, 15));
            platforms.add(new platFrom(150, 330, 80, 15));
            platforms.add(new platFrom(50, 280, 80, 15));
            platforms.add(new platFrom(150, 230, 80, 15));

            getChildren().addAll(platforms);
            gameCharacter.setPlatforms(platforms);


            gameCharacter.setPlatforms(platforms);

            // สร้างป้อมปืน 3 ตัว
            Turret t1 = new Turret(760, 80, 40);
            Turret t2 = new Turret(820, 200, 40);
            Turret t3 = new Turret(760, 320, 40);
            turrets.add(t1);
            turrets.add(t2);
            turrets.add(t3);
            getChildren().addAll(t1, t2, t3);

            // บอสด่าน 3: MegaCitadel
            boss = new Boss3MegaCitadel(750, 75, turrets);
            getChildren().add(boss);

        } else if (stageNumber == 4) {
            // จบเกม
            showMessage("You Win!\nFinal Score: " + score, 3000);
            new Thread(() -> {
                try {
                    Thread.sleep(3500);
                } catch (InterruptedException ignored) {}
                javafx.application.Platform.runLater(this::resetToMenu);
            }).start();
            return;
        }

        // สร้างเขตเป้าหมาย
        goalZone = new Rectangle(WIDTH - 10, 0, 10, HEIGHT);
        goalZone.setFill(Color.TRANSPARENT);
        getChildren().add(goalZone);
    }

    public void nextStage() {
        updateScore(100);
        currentStage++;
        setupStage(currentStage);
    }

    public void playerShoot() {
        long now = System.currentTimeMillis();
        if (now - lastPlayerShotTime > playerShotCooldown) {
            int x = gameCharacter.getX() + (gameCharacter.getScaleX() == -1 ? gameCharacter.getCharacterWidth() : 0);
            int y = gameCharacter.getY() + (gameCharacter.getCharacterHeight() / 2) - 5;
            int direction = (int) gameCharacter.getScaleX() ;
            Bullet bullet = new Bullet(x, y, direction);
            bullets.add(bullet);
            getChildren().add(bullet);
            lastPlayerShotTime = now;
        }
    }

    public void turretShoot(int x, int y) {
        EnemyBullet eb = new EnemyBullet(x, y, gameCharacter.getX() + 16, gameCharacter.getY() + 32);
        enemyBullets.add(eb);
        getChildren().add(eb);
    }

    // ยิงกระสุนแบบกระจาย (สำหรับบอสด่าน 3)
    public void bossSpreadShot(int x, int y) {
        int targetX = gameCharacter.getX() + 16;
        int targetY = gameCharacter.getY() + 32;

        // ยิง 3 ทิศทาง: ตรง, เอียงบน, เอียงล่าง
        EnemyBullet eb1 = new EnemyBullet(x, y, targetX, targetY);
        EnemyBullet eb2 = new EnemyBullet(x, y, targetX, targetY - 50);
        EnemyBullet eb3 = new EnemyBullet(x, y, targetX, targetY + 50);

        enemyBullets.add(eb1);
        enemyBullets.add(eb2);
        enemyBullets.add(eb3);

        getChildren().addAll(eb1, eb2, eb3);
    }

    public void updateBullets() {
        for (Bullet bullet : bullets) bullet.update();

        List<Bullet> toRemove = new ArrayList<>();
        for (Bullet b : bullets) {
            if (b.isOffscreen() || b.isHit()) {
                toRemove.add(b);
                getChildren().remove(b);
            }
        }
        bullets.removeAll(toRemove);
    }

    public void updateEnemyBullets() {
        for (EnemyBullet eb : enemyBullets) eb.update();

        List<EnemyBullet> toRemove = new ArrayList<>();
        for (EnemyBullet eb : enemyBullets) {
            if (eb.isOffscreen() || eb.isHit()) {
                toRemove.add(eb);
                getChildren().remove(eb);
            }
        }
        enemyBullets.removeAll(toRemove);
    }

    public void updateTurrets() {
        for (Turret t : turrets) {
            if (!t.isDead()) {
                t.update(this);
            }
        }
    }

    public void updateBoss() {
        if (boss != null && !boss.isDead()) {
            boss.update(this);
        }
    }

    public void checkCollisions() {
        // ตรวจสอบกระสุนผู้เล่นโดนป้อมปืน
        for (Turret turret : turrets) {
            if (!turret.isDead()) {
                for (Bullet bullet : bullets) {
                    if (!bullet.isHit() && bullet.getBoundsInParent().intersects(turret.getBoundsInParent())) {
                        turret.takeDamage(10);
                        bullet.setHit(true);
                        if (turret.isDead()) {
                            updateScore(20);
                        }
                    }
                }
            }
        }

        // ตรวจสอบกระสุนผู้เล่นโดนบอส
        if (boss != null && !boss.isDead() && boss.isVulnerable()) {
            for (Bullet bullet : bullets) {
                if (!bullet.isHit() && bullet.getBoundsInParent().intersects(boss.getBoundsInParent())) {
                    boss.takeDamage(10);
                    bullet.setHit(true);
                    updateScore(5);
                }
            }
        }

        // ตรวจสอบกระสุนศัตรูโดนผู้เล่น
        for (EnemyBullet eb : enemyBullets) {
            if (!eb.isHit() && eb.getBoundsInParent().intersects(gameCharacter.getBoundsInParent())) {
                eb.setHit(true);
                playerHit();
            }
        }
    }

    public void checkGameState() {
        // ตรวจสอบบอสตาย
        if (boss != null && boss.isDead()) {
            if (currentStage == 1) {
                updateScore(50);
            } else if (currentStage == 2) {
                updateScore(75);
            } else if (currentStage == 3) {
                updateScore(100);
            }

            // แสดงข้อความเพียงครั้งเดียว
            if (boss.getHealth() == 0) {
                boss.takeDamage(-1); // Hack เล็กน้อยเพื่อไม่ให้แสดงซ้ำ
                showMessage("Boss Defeated!\nGo Right!", 2000);
            }
        }

        // ตรวจสอบผู้เล่นชนเขตเป้าหมาย
        if (boss != null && boss.isDead() && goalZone != null) {
            if (gameCharacter.getBoundsInParent().intersects(goalZone.getBoundsInParent())) {
                nextStage();
            }
        }
    }

    private void playerHit() {
        lives--;
        updateLives();
        if (lives > 0) {
            showMessage("Lives left: " + lives, 1000);
            gameCharacter.respawn();
        } else {
            showMessage("GAME OVER\nFinal Score: " + score, 2000);
            new Thread(() -> {
                try {
                    Thread.sleep(2500);
                } catch (InterruptedException ignored) {}
                javafx.application.Platform.runLater(this::resetToMenu);
            }).start();
        }
    }

    private void updateScore(int points) {
        score += points;
        scoreLabel.setText("SCORE: " + score);
    }

    private void updateLives() {
        livesLabel.setText("LIVES: " + lives);
    }

    private void showMessage(String text, int durationMs) {
        infoLabel.setText(text);
        infoLabel.setVisible(true);
        new Thread(() -> {
            try {
                Thread.sleep(durationMs);
            } catch (InterruptedException ignored) {}
            javafx.application.Platform.runLater(() -> infoLabel.setVisible(false));
        }).start();
    }

    private void resetToMenu() {
        if (rootPane != null) {
            rootPane.getChildren().clear();
            MenuView menu = new MenuView();
            rootPane.getChildren().add(menu);

            menu.getStartButton().setOnAction(e -> {
                rootPane.getChildren().remove(menu);
                rootPane.getChildren().add(this);
                this.startGame();
                this.requestFocus();
            });
        }
    }

    public GameCharacter getGameCharacter() {
        return gameCharacter;
    }

    public BaseBoss getBoss() {
        return boss;
    }

    public Keys getKeys() {
        return keys;
    }

    public List<Turret> getTurrets() {
        return turrets;
    }
}