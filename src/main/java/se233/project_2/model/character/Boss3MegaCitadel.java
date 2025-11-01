package se233.project_2.model.character;

import javafx.scene.paint.Color;
import se233.project_2.view.GameStage;

import java.util.List;
import java.util.Random;

/**
 * บอสด่าน 3: ป้อมปราการสูงสุด
 * - ใหญ่ที่สุด เลือดมากที่สุด
 * - มีป้อมปืน 3 ตัว (บน-กลาง-ล่าง)
 * - หลังป้อมหมด จะยิงกระสุนแบบ spread (กระจาย)
 * - Boss ท้าทายสุดของเกม
 */
public class Boss3MegaCitadel extends BaseBoss {
    private List<Turret> turrets;
    private long lastShotTime = 0;
    private long nextShotDelay = 1500;
    private Random random = new Random();

    public Boss3MegaCitadel(int x, int y, List<Turret> turrets) {
        super(x, y, 140, 350, 400, Color.rgb(150, 50, 150)); // สีม่วง
        this.turrets = turrets;
        this.vulnerable = false; // ต้องทำลายป้อมก่อน
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

        // หลังเป็น vulnerable แล้ว ยิงกระสุนแบบ spread
        if (vulnerable && !dead) {
            long now = System.currentTimeMillis();
            if (now - lastShotTime > nextShotDelay) {
                // ยิงกระสุน 3 นัดกระจาย
                int x = (int) getTranslateX() + 70;
                int y = (int) getTranslateY() + 175;

                gameStage.bossSpreadShot(x, y);

                lastShotTime = now;
                nextShotDelay = 1500 + random.nextInt(1000);
            }
        }
    }

    @Override
    protected void onDeath() {
        super.onDeath();
    }
}