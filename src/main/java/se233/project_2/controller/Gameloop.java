package se233.project_2.controller;

import se233.project_2.model.Bullet;
import se233.project_2.model.Keys;
import se233.project_2.model.character.BaseBoss;
import se233.project_2.model.character.GameCharacter;
import se233.project_2.view.GameStage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// MODIFIED: เปลี่ยนเป็น Runnable เพื่อรันบน Thread แยก
public class Gameloop implements Runnable {

    private GameStage gameStage;
    private GameCharacter character;
    private Keys keys;

    private List<Bullet> bullets = new ArrayList<>();
    private BaseBoss currentBoss;
    private List<BaseBoss> bossQueue;

    private boolean isRunning;
    private boolean gameWon = false;
    private boolean playerLost = false;

    // Cooldown สำหรับยิง
    private long lastShotTime = 0;
    private final long shootCooldown = 300_000_000; // 0.3 วินาที

    public Gameloop(GameStage gameStage, GameCharacter character, Keys keys, BaseBoss... bosses) {
        this.gameStage = gameStage;
        this.character = character;
        this.keys = keys;

        this.bossQueue = new ArrayList<>(Arrays.asList(bosses));
        spawnNextBoss();
    }

    public void start() {
        isRunning = true;
        Thread gameThread = new Thread(this);
        gameThread.setDaemon(true); // ให้ Thread นี้ปิดตามโปรแกรมหลัก
        gameThread.start();
    }

    public void stop() {
        isRunning = false;
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        final double ns = 1000000000.0 / 60.0; // 60 updates per second
        double delta = 0;

        while (isRunning) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            while (delta >= 1) {
                // update() คือการอัปเดตตรรกะทั้งหมด
                update(System.nanoTime()); // ส่ง 'now' ไปด้วยเพื่อเช็ค cooldown
                delta--;
            }
        }
    }

    /**
     * เมธอดนี้คือหัวใจของ Logic Loop
     * จะถูกเรียก 60 ครั้งต่อวินาที
     */
    private void update(long now) {
        if (gameWon || playerLost) return;

        // 1. จัดการ Input
        handleInput(now);

        // 2. อัปเดตตรรกะตัวละคร
        character.update();

        // 3. อัปเดตบอส (ถ้ามี)
        if (currentBoss != null) {
            currentBoss.updateLogic(); // เราจะสร้างเมธอดนี้ใน BaseBoss

            // 4. ตรวจสอบการชน (กระสุนผู้เล่น vs บอส)
            List<Bullet> bulletsToRemove = new ArrayList<>();
            for (Bullet bullet : bullets) {
                bullet.update();

                // ตรวจสอบการชน
                if (bullet.getBoundsInParent().intersects(currentBoss.getBoundsInParent())) {
                    currentBoss.takeDamage(10);
                    bulletsToRemove.add(bullet);

                    if (currentBoss.isDead()) {
                        character.addScore(currentBoss.getScoreValue());
                        spawnNextBoss();
                        break;
                    }
                }

                if (bullet.isOffScreen()) {
                    bulletsToRemove.add(bullet);
                }
            }

            // TODO: 5. ตรวจสอบการชน (กระสุนบอส vs ผู้เล่น)
            // (ต้องเพิ่ม List<EnemyBullet> และวนลูปเช็ค)
            // if (enemyBullet.intersects(character)) {
            //    handlePlayerDeath();
            // }

            // TODO: 6. ตรวจสอบการชน (ผู้เล่น vs บอส)
            // if (character.getBoundsInParent().intersects(currentBoss.getBoundsInParent())) {
            //    handlePlayerDeath();
            // }

            // Cleanup กระสุน
            bullets.removeAll(bulletsToRemove);
            // การลบออกจาก 'gameStage' ต้องทำใน DrawingLoop (JavaFX Thread)
            // เราจะใช้วิธีให้ DrawingLoop อ่าน List นี้ไปจัดการ
        }
    }

    private void handlePlayerDeath() {
        character.die(); // เรียกเมธอดตาย
        if (character.getLives() <= 0) {
            playerLost = true;
            stop();
        } else {
            character.respawn();
        }
    }

    private void spawnNextBoss() {
        // (เมธอดนี้ซับซ้อนขึ้นเล็กน้อย เพราะต้องจัดการบน JavaFX Thread)
        // เราจะเก็บ state ไว้ แล้วให้ DrawingLoop จัดการ

        if (currentBoss != null) {
            // บอก DrawingLoop ให้ลบตัวเก่า
            gameStage.removeEntity(currentBoss);
        }

        if (!bossQueue.isEmpty()) {
            currentBoss = bossQueue.remove(0);
            // บอก DrawingLoop ให้เพิ่มตัวใหม่
            gameStage.addEntity(currentBoss);
        } else {
            currentBoss = null;
            gameWon = true;
            stop();
        }
    }

    private void handleInput(long now) {
        // (ย้ายโค้ด handleInput จาก Gameloop เดิมมาไว้ที่นี่)
        // ... (ดูโค้ดจาก Gameloop.java ที่คุณส่งมา)
        // ซ้าย-ขวา
        if (keys.isPressed(character.getLeftKey())) {
            character.moveLeft();
        } else if (keys.isPressed(character.getRightKey())) {
            character.moveRight();
        } else {
            character.stop();
        }

        // กระโดด
        if (keys.isPressed(character.getUpKey())) {
            character.jump();
        }

        // หมอบ
        if (keys.isPressed(character.getDownKey())) {
            character.crouch();
        } else {
            character.stopCrouch();
        }

        // ยิง
        if (keys.isPressed(character.getShootKey())) {
            character.shoot();

            if (now - lastShotTime >= shootCooldown) {
                spawnBullet();
                lastShotTime = now;
            }
        } else {
            character.stopShoot();
        }
    }

    private void spawnBullet() {
        // (ย้ายโค้ด spawnBullet จาก Gameloop เดิมมาไว้ที่นี่)
        // ...
        int x = character.getX();
        int y = character.getY() + 20; // TODO: ปรับตำแหน่งกระสุน
        int direction = (int) character.getScaleX();

        if (direction == -1) {
            x += character.getCharacterWidth();
        }

        Bullet bullet = new Bullet(x, y, -direction);
        bullets.add(bullet);
        gameStage.addEntity(bullet); // บอก GameStage ให้เพิ่มกระสุน
    }

    // --- เพิ่ม Getters ให้ DrawingLoop ดึงข้อมูลไปวาด ---
    public GameCharacter getCharacter() { return character; }
    public BaseBoss getCurrentBoss() { return currentBoss; }
    public List<Bullet> getBullets() { return bullets; }
    public boolean isGameWon() { return gameWon; }
    public boolean isPlayerLost() { return playerLost; }
}