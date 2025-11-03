package se233.project_2.view;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se233.project_2.Exception.SpriteException;
import se233.project_2.controller.AudioFeatures;
import se233.project_2.controller.DrawingLoop;
import se233.project_2.controller.GameLoop;
import se233.project_2.model.*;
import se233.project_2.model.character.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GameStage extends Pane {
    private static final Logger logger = LogManager.getLogger(GameStage.class);
    public static final int WIDTH = 900;
    public static final int HEIGHT = 500;
    public static int GROUND = 440;

    private GameLoop gameLoop;
    private DrawingLoop drawingLoop;

    private Image gameStageImg;
    private ImageView background;
    private GameCharacter gameCharacter;
    private BaseBoss boss; // ใช้ BaseBoss แทน
    private Keys keys;
    private List<platFrom> platforms = new ArrayList<>();
    private List<Bullet> bullets = new ArrayList<>();
    private List<EnemyBullet> enemyBullets = new ArrayList<>();
    private List<Turret> turrets = new ArrayList<>();
    private List<Minion> minions = new ArrayList<>();


    private int currentStage = 1;
    private int lives = 3;
    private int score = 0;

    private long lastPlayerShotTime = 0;
    private final long playerShotCooldown = 300;
    private long lastSpecialShotTime = 0;
    private final long specialShotCooldown = 3000;

    private Label infoLabel;
    private Label scoreLabel;
    private Label livesLabel;
    private Rectangle goalZone;
    private Pane rootPane;
    private Scene scene;

    public GameStage() {
        keys = new Keys();
        gameStageImg = new Image(Objects.requireNonNull(GameStage.class.getResourceAsStream("/se233/project_2/Background.png")));
        background = new ImageView(gameStageImg); // เก็บไว้เป็น field
        background.setFitWidth(WIDTH);
        background.setFitHeight(HEIGHT);

        gameCharacter = new GameCharacter( 30, 350, 75, 80, KeyCode.A, KeyCode.D, KeyCode.W, KeyCode.S, KeyCode.SPACE,KeyCode.Z );

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
    public void setScene(Scene scene) {
        this.scene = scene;
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
        AudioFeatures.stopSound();
        getChildren().removeAll(platforms);
        getChildren().removeAll(bullets);
        getChildren().removeAll(enemyBullets);
        getChildren().removeAll(turrets);
        getChildren().removeAll(minions);
        if (boss != null) getChildren().remove(boss);
        if (goalZone != null) getChildren().remove(goalZone);

        platforms.clear();
        bullets.clear();
        enemyBullets.clear();
        turrets.clear();

        gameCharacter.respawn();
        changeBackground(stageNumber);
        showMessage("Stage " + stageNumber + " Start!", 1500);

        if (stageNumber == 1) {
            AudioFeatures.playGameSound();
            setGROUND(440);

            // แพลตฟอร์มตามรูป
            platforms.add(new platFrom(485, 380, 30, 15));
            platforms.add(new platFrom(110, 350, 230, 15));
            platforms.add(new platFrom(390, 300, 30, 15));
            platforms.add(new platFrom(0, 240, 340, 15));

            getChildren().addAll(platforms);
            gameCharacter.setPlatforms(platforms);

            // บอสด่าน 1: FortifiedWall
            boss = new Boss1Wall(575, 300, turrets);
            getChildren().add(boss);

            // สร้างป้อมปืน
            Turret t1 = new Turret(580, 275, 30,"/se233/project_2/Turret1.png");
            Turret t2 = new Turret(670, 275, 30,"/se233/project_2/Turret2.png");
            turrets.add(t1);
            turrets.add(t2);
            getChildren().addAll(t1, t2);


        } else if (stageNumber == 2) {
            AudioFeatures.playGameSound();
            setGROUND(440);

            platforms.add(new platFrom(0, 360, 250, 15));
            platforms.add(new platFrom(0, 240, 80, 15));

            getChildren().addAll(platforms);
            gameCharacter.setPlatforms(platforms);


            // บอสด่าน 2: HeavyFortress
            boss = new Boss2JAVA(700, 0);
            getChildren().add(boss);

        } else if (stageNumber == 3) {
            setGROUND(400);
            AudioFeatures.playBossSound();
            gameCharacter.setPlatforms(platforms);

            // สร้างป้อมปืน 3 ตัว

            Turret t2 = new Turret(600, 500, 200,"/se233/project_2/boss3/kitty.png");


            turrets.add(t2);


            // บอสด่าน 3: MegaCitadel
            boss = new Boss3MEENEO(600, 75, turrets);
            getChildren().add(boss);
            getChildren().addAll(t2);


        } else if (stageNumber == 4) {
            // จบเกม
          AudioFeatures.stopSound();
            showMessage("You Win!\nFinal Score: " + score, 3000);
            new Thread(() -> {
                try {
                    Thread.sleep(3500);
                } catch (InterruptedException ignored) {}
                javafx.application.Platform.runLater(this::resetToMenu);
            }).start();
            currentStage=1;
            return;
        }

        // สร้างเขตเป้าหมาย
        goalZone = new Rectangle(WIDTH - 10, 0, 10, HEIGHT);
        goalZone.setFill(Color.TRANSPARENT);
        getChildren().add(goalZone);
    }

    public void nextStage() {
        updateScore(100);
        AudioFeatures.stopSound();
        currentStage++;
        setupStage(currentStage);
    }
    public void playerSpecialShoot() {
        long now = System.currentTimeMillis();
        if (now - lastSpecialShotTime > specialShotCooldown) {
            int x = gameCharacter.getX() +
                    (gameCharacter.getScaleX() == -1 ? 0 : gameCharacter.getCharacterWidth());
            int y = gameCharacter.getY() + (gameCharacter.getCharacterHeight() / 2) - 5;
            int direction = (int) gameCharacter.getScaleX();

            try {
                // ใช้ SpecialBullet แทน Bullet ธรรมดา
                SpecialBullet specialBullet = new SpecialBullet(
                        x, y, direction,
                        "/se233/project_2/bullet.png", // ใช้รูปเดียวกันหรือเปลี่ยนเป็นรูปพิเศษก็ได้
                        156, 156
                );
                bullets.add(specialBullet); // เพิ่มลงใน list เดียวกัน
                getChildren().add(specialBullet);
                AudioFeatures.playShootSound();
                logger.info("Special Shot! Cooldown: " + specialShotCooldown + "ms, Damage: 100");
            } catch (NullPointerException e) {
                SpecialBullet specialBullet = new SpecialBullet(x, y, direction);
                bullets.add(specialBullet);
                getChildren().add(specialBullet);
                throw new SpriteException("Not Found Path: " + e);
            }

            lastSpecialShotTime = now;
        } else {
            // คำนวณเวลาคงเหลือ
            long timeLeft = specialShotCooldown - (now - lastSpecialShotTime);
           logger.info("Special shot on cooldown! " + (timeLeft / 1000.0) + " seconds left");
        }

    }
    public void playerShoot() {
        long now = System.currentTimeMillis();
        if (now - lastPlayerShotTime > playerShotCooldown) {
            int x = gameCharacter.getX() + (gameCharacter.getScaleX() == -1 ? 0 : gameCharacter.getCharacterWidth());
            int y = gameCharacter.getY() + (gameCharacter.getCharacterHeight() / 2) - 5;
            int direction = (int) gameCharacter.getScaleX() ;

            try {
                Bullet bullet = new Bullet(x, y, direction, "/se233/project_2/bullet.png", 156, 156);
                bullets.add(bullet);
                AudioFeatures.playShootSound();
                getChildren().add(bullet);
            } catch (NullPointerException e) {
                Bullet bullet = new Bullet(x, y, direction);
                bullets.add(bullet);
                getChildren().add(bullet);

                throw new SpriteException("Not Found Paht" +e);

            }

            lastPlayerShotTime = now;
        }
    }

    public void turretShoot(int x, int y) {
        if (currentStage == 3) {
            EnemyBullet eb = new EnemyBullet(x, y, gameCharacter.getX() + 16, gameCharacter.getY() + 32,"/se233/project_2/bulletBoss.png");
            enemyBullets.add(eb);
            getChildren().add(eb);
        }else {
            EnemyBullet eb = new EnemyBullet(x, y, gameCharacter.getX() + 16, gameCharacter.getY() + 32);
            enemyBullets.add(eb);
            getChildren().add(eb);
        }

    }

    // ยิงกระสุนแบบกระจาย (สำหรับบอสด่าน 3)
    public void bossSpreadShot(int x, int y) {
        int targetX = gameCharacter.getX() + 16;
        int targetY = gameCharacter.getY() + 32;

        // ยิง 3 ทิศทาง: ตรง, เอียงบน, เอียงล่าง
        EnemyBullet eb1 = new EnemyBullet(x, y, targetX, targetY, "/se233/project_2/bulletBoss.png");
        EnemyBullet eb2 = new EnemyBullet(x, y, targetX, targetY - 50, "/se233/project_2/bulletBoss.png");
        EnemyBullet eb3 = new EnemyBullet(x, y, targetX, targetY + 50, "/se233/project_2/bulletBoss.png");

        enemyBullets.add(eb1);
        enemyBullets.add(eb2);
        enemyBullets.add(eb3);

        getChildren().addAll(eb1, eb2, eb3);
    }
    public void spawnMinion(int x, int y) {
        int direction = (int) gameCharacter.getScaleX() ;
        Minion minion = new Minion(x, y, "/se233/project_2/minion.png", gameCharacter.getX() + 16, gameCharacter.getY() + 32);
        minions.add(minion);
        getChildren().add(minion);
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

    public void updateMinions() {
        for (Minion minion : minions) minion.update();

        List<Minion> toRemove = new ArrayList<>();
        for (Minion m : minions) {
            if (m.isOffscreen() || m.isDead()) {
                toRemove.add(m);
                getChildren().remove(m);
            }
        }
        minions.removeAll(toRemove);
    }

    public void checkCollisions() {
        // ตรวจสอบกระสุนผู้เล่นโดนป้อมปืน
        for (Turret turret : turrets) {
            if (!turret.isDead()) {
                for (Bullet bullet : bullets) {
                    if (!bullet.isHit() && bullet.getBoundsInParent().intersects(turret.getBoundsInParent())) {
                        turret.takeDamage(bullet.getDmg());
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
                    boss.takeDamage(bullet.getDmg());
                    bullet.setHit(true);
                    updateScore(5);
                }
            }
        }

        //  ตรวจสอบผู้เล่นชนบอส - ถ้าชนจะตายทันที
        if (boss != null && !boss.isDead()) {
            if (gameCharacter.getBoundsInParent().intersects(boss.getBoundsInParent())) {
                playerHit();
            }
        }

        // ตรวจสอบกระสุนศัตรูโดนผู้เล่น
        for (EnemyBullet eb : enemyBullets) {
            if (!eb.isHit() && eb.getBoundsInParent().intersects(gameCharacter.getBoundsInParent())) {
                eb.setHit(true);
                playerHit();
            }
        }

        for (Minion minion : minions) {
            if (!minion.isDead()) {
                for (Bullet bullet : bullets) {
                    if (!bullet.isHit() && bullet.getBoundsInParent().intersects(minion.getBoundsInParent())) {
                        minion.takeDamage(bullet.getDmg());
                        bullet.setHit(true);
                        if (minion.isDead()) updateScore(10);
                    }
                }

            }
        }

        // มินเนี่ยนชนผู้เล่น
        for (Minion minion : minions) {
            if (!minion.isDead() && minion.getBoundsInParent().intersects(gameCharacter.getBoundsInParent())) {
                minion.setHit(true);
                minion.takeDamage(100);
                playerHit();
            }
        }
    }

    public void checkGameState() {
        // ตรวจสอบบอสตาย
        if (boss != null && boss.isDead() && boss.isScoreAfterDie()) {
            if (currentStage == 1) {
                updateScore(50);
                boss.setScoreAfterDie(false);
            } else if (currentStage == 2) {
                updateScore(75);
                boss.setScoreAfterDie(false);
            } else if (currentStage == 3) {
                updateScore(100);
                boss.setScoreAfterDie(false);
            }

            // แสดงข้อความเพียงครั้งเดียว
            if (boss.getHealth() == 0) {
                boss.takeDamage(-1); // Hack เล็กน้อยเพื่อไม่ให้แสดงซ้ำ
                showMessage("Boss Defeated!\nGo >>>!", 2000);
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
            AudioFeatures.stopSound();
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
        AudioFeatures.stopSound();
        if (rootPane != null) {
            rootPane.getChildren().clear();
            MenuView menu = new MenuView();
            rootPane.getChildren().add(menu);

            menu.getStartButton().setOnAction(e -> {
                // ลบเมนู
                rootPane.getChildren().remove(menu);

                // สร้าง GameStage ใหม่ทุกครั้ง
                GameStage newGameStage = new GameStage();
                newGameStage.setRootPane(rootPane);
                newGameStage.setScene(scene);
                rootPane.getChildren().add(newGameStage);

                // ตั้งค่าการควบคุมใหม่
                scene.setOnKeyPressed(keyEvent -> newGameStage.getKeys().add(keyEvent.getCode()));
                scene.setOnKeyReleased(keyEvent -> newGameStage.getKeys().remove(keyEvent.getCode()));

                newGameStage.startGame();
                newGameStage.requestFocus();

                // สร้าง Loop ใหม่
                GameLoop gameLoop = new GameLoop(newGameStage);
                DrawingLoop drawingLoop = new DrawingLoop(newGameStage);

                Thread gameThread = new Thread(gameLoop);
                Thread drawThread = new Thread(drawingLoop);
                gameThread.setDaemon(true);
                drawThread.setDaemon(true);
                gameThread.start();
                drawThread.start();
            });
        }
    }


    private void changeBackground(int stageNumber) {
        try {
            String bgPath = "";

            if (stageNumber == 1) {
                bgPath = "/se233/project_2/Background.png";
            } else if (stageNumber == 2) {
                bgPath = "/se233/project_2/Background2.png";
            } else if (stageNumber == 3) {
                bgPath = "/se233/project_2/Background3.png";
            } else {
                bgPath = "/se233/project_2/BackgroundWin.png";
            }

            Image newBg = new Image(Objects.requireNonNull(
                    GameStage.class.getResourceAsStream(bgPath)
            ));
            background.setImage(newBg);

        }catch (NullPointerException e) {

            // ถ้าโหลดรูปไม่ได้ ใช้สีพื้นหลังแทน
            javafx.scene.shape.Rectangle colorBg = new javafx.scene.shape.Rectangle(0, 0, WIDTH, HEIGHT);

            if (stageNumber == 1) {
                colorBg.setFill(Color.rgb(40, 80, 40)); // สีเขียวเข้ม (ป่า)
            } else if (stageNumber == 2) {
                colorBg.setFill(Color.rgb(60, 60, 90)); // สีน้ำเงินเข้ม (เมือง)
            } else if (stageNumber == 3) {
                colorBg.setFill(Color.rgb(80, 40, 80)); // สีม่วงเข้ม (อวกาศ)
            }

            // แทนที่ background ด้วยสี
            getChildren().remove(background);
            getChildren().add(0, colorBg);
            throw new SpriteException("Not Found Paht" +e);

        }
    }
    public void setGROUND(int GROUND) {
        this.GROUND = GROUND;
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