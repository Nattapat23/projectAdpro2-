package se233.project_2.model.character;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se233.project_2.Exception.SpriteException;
import se233.project_2.Launcher;
import se233.project_2.model.AnimatedSprite;
import se233.project_2.view.GameStage;

import java.util.List;
import java.util.Random;

/**
 * บอสด่าน 3: ป้อมปราการสูงสุด
 * - เคลื่อนที่ขึ้น-ลงได้
 * - มีป้อมปืน 3 ตัว
 * - หลังป้อมหมด ยิงกระสุนแบบ spread
 * - ✅ มีท่าชาร์จก่อนยิง (เปลี่ยนรูปร่าง/เอฟเฟกต์)
 */
public class Boss3MEENEO extends BaseBoss {
    private static final Logger logger = LogManager.getLogger(Boss3MEENEO.class);
    private boolean isChargingSkill = false;
    private long skillChargeStart = 0;
    private static final long SKILL_CHARGE_TIME = 1000;

    private List<Turret> turrets;
    private long lastShotTime = 0;
    private long nextShotDelay = 2500; //
    private Random random = new Random();

    // การเคลื่อนที่
    private double velocityY = 2;
    private int minY = 60;
    private int maxY = 120;
    public double size = 0.4;

    // เก็บ sprite หลายท่า (ถ้ามี)
    private Image normalSprite;
    private Image chargingSprite;
    private boolean hasChargingSprite = false;

    // Animation state
    private static final int STATE_IDLE = 0;
    private static final int STATE_CHARGING = 1;
    private int currentState = STATE_IDLE;

    public Boss3MEENEO(int x, int y, List<Turret> turrets) {
        super(x, y,796, 1024,  400, Color.rgb(150, 50, 150), "/se233/project_2/boss3/bossJuju.png", 0, 0);
        this.turrets = turrets;
        this.setImage(size);

        this.vulnerable = false;

        //  พยายามโหลด sprite ท่าชาร์จ (ถ้ามี)
        try {
            normalSprite = new Image(Launcher.class.getResourceAsStream("/se233/project_2/boss3/bossSmail.png"));

            // ลองโหลด sprite ท่าชาร์จ (ถ้าไม่มีจะใช้เอฟเฟกต์แทน)
            chargingSprite = new Image(Launcher.class.getResourceAsStream("/se233/project_2/boss3/bossAngry.png"));
            hasChargingSprite = true;
            logger.debug(" Boss3: Charging sprite loaded successfully!");
        }  catch (NullPointerException e) {
            hasChargingSprite = false;
            logger.warn(" Boss3: No charging sprite found, using visual effects instead");
            throw new SpriteException("Not Found Paht" +e);
        }
    }

    @Override
    public void update(GameStage gameStage) {
        // เช็คว่าป้อมหมดหรือยัง
        if (!vulnerable) {
            boolean allTurretsDead = true;
            for (Turret t : turrets) {
                if (!t.isDead()) {
                    allTurretsDead = false;
                    break;
                }
            }

            if (allTurretsDead) {
                setVulnerable(true);
            }
        }

        // เคลื่อนที่ขึ้น-ลง
        if (!dead) {
            double newY = getTranslateY() + velocityY;

            if (newY <= minY) {
                newY = minY;
                velocityY = 2; // เปลี่ยนทิศลง
            } else if (newY >= maxY) {
                newY = maxY;
                velocityY = -2; // เปลี่ยนทิศขึ้น
            }

            setTranslateY(newY);

            // อัปเดตตำแหน่งป้อมปืนให้ตามบอส
            if (!turrets.isEmpty()) {
                turrets.getFirst().setTranslateY(newY + 125);
            }
        }

        //  ระบบยิงกระสุนแบบใหม่ (มีท่าชาร์จ)
        if (vulnerable && !dead) {
            long now = System.currentTimeMillis();

            if (isChargingSkill) {
                // กำลังชาร์จอยู่
                if (now - skillChargeStart > SKILL_CHARGE_TIME) {
                    // ชาร์จเสร็จแล้ว → ยิงกระสุน!
                    int x = (int) getTranslateX() + 30;
                    int y = (int) getTranslateY() + 220;

                    gameStage.bossSpreadShot(x, y);

                    // เปลี่ยนกลับเป็นท่าปกติ
                    changeToNormalState();

                    isChargingSkill = false;
                    lastShotTime = now;
                    nextShotDelay = 2000 + random.nextInt(1500); // 2-3.5 วิ
                }
            } else {
                // ไม่ได้ชาร์จ → เช็คว่าถึงเวลายิงหรือยัง
                if (now - lastShotTime > nextShotDelay) {
                    //  เริ่มชาร์จ!
                    startCharging();
                    isChargingSkill = true;
                    skillChargeStart = now;
                }
            }
        }
    }

    /**
     *  เริ่มท่าชาร์จ - เปลี่ยนรูปร่างหรือใช้เอฟเฟกต์
     */
    private void startCharging() {
        currentState = STATE_CHARGING;

        // วิธีที่ 2: ใช้เอฟเฟกต์แทน (ถ้าไม่มีรูปท่าชาร์จ)
            applyChargingEffect();

           logger.debug("⚡ Boss3 is charging attack! (using effects)");

    }

    /**
     *  กลับเป็นท่าปกติ
     */
    private void changeToNormalState() {
        currentState = STATE_IDLE;

            removeChargingEffect();

           logger.info("Boss3 returned to normal state");

    }

    /**
     *  ใช้เอฟเฟกต์แทนการเปลี่ยนรูป (กรณีไม่มีรูปท่าชาร์จ)
     */
    private void applyChargingEffect() {
        if (imageView != null) {
            // เอฟเฟกต์ 1: เปล่งแสงสีแดง
            imageView.setImage(chargingSprite);
            imageView.setStyle("-fx-effect: dropshadow(gaussian, red, 20, 0.8, 0, 0);");

            // เอฟเฟกต์ 2: ขยายขนาดนิดหน่อย
            imageView.setScaleX(1.1);
            imageView.setScaleY(1.1);

            // เอฟเฟกต์ 3: หมุนเล็กน้อย (optional)
            // imageView.setRotate(5);
        }
    }

    /**
     * ลบเอฟเฟกต์ชาร์จ
     */
    private void removeChargingEffect() {
        if (imageView != null) {
            // ลบ glow effect
            if (vulnerable) {
                imageView.setImage(normalSprite);
                // ถ้า vulnerable ให้กลับเป็น yellow glow
                imageView.setStyle("-fx-effect: dropshadow(gaussian, yellow, 10, 0.7, 0, 0);");
            } else {
                // ถ้าไม่ vulnerable ไม่มี effect
                imageView.setStyle("");
            }

            // กลับขนาดปกติ
            imageView.setScaleX(1.0);
            imageView.setScaleY(1.0);

            // กลับมุมปกติ
            // imageView.setRotate(0);
        }
    }

    @Override
    protected void onDeath() {
        //  ยกเลิกท่าชาร์จถ้ากำลังชาร์จอยู่
        if (isChargingSkill) {
            changeToNormalState();
            isChargingSkill = false;
        }

        // เรียก parent onDeath (ไม่มี explosion effect)
        super.onDeath();
    }

    /**
     * ตรวจสอบว่ากำลังชาร์จอยู่หรือไม่
     */
    public boolean isCharging() {
        return isChargingSkill;
    }

    /**
     * ได้ state ปัจจุบัน
     */
    public int getCurrentState() {
        return currentState;
    }
}