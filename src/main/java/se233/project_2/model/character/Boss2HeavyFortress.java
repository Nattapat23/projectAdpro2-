package se233.project_2.model.character;

import javafx.scene.paint.Color;
import se233.project_2.view.GameStage;

import java.util.Random;

/**
 * บอสด่าน 2: ป้อมปราการหนัก
 * - ไม่เคลื่อนที่ แต่ใหญ่กว่า เลือดมากกว่า
 * - ยิงกระสุนเป็นระลอกแบบสุ่ม (burst fire)
 * - โจมตีได้ทันที ไม่มีป้อมปืนคุ้มกัน
 */
public class Boss2HeavyFortress extends BaseBoss {
    private long lastShotTime = 0;
    private long nextShotDelay = 2000;
    private Random random = new Random();
    private int burstCount = 0;
    private static final int BURST_SIZE = 3; // ยิง 3 นัดติด

    public Boss2HeavyFortress(int x, int y) {
        super(x, y, 120, 300, 300, Color.rgb(50, 100, 200));
        this.vulnerable = true; // ยิงได้เลย
    }

    @Override
    public void update(GameStage gameStage) {
        if (dead) return;

        long now = System.currentTimeMillis();

        // ยิงกระสุนเป็นระลอก (burst)
        if (now - lastShotTime > 500 && burstCount < BURST_SIZE) {
            // ยิงจากตำแหน่งกลางบอส
            int x = (int) getTranslateX() + 60;
            int y = (int) getTranslateY() + 150;
            gameStage.turretShoot(x, y);

            lastShotTime = now;
            burstCount++;
        }

        // หลังยิงครบ burst แล้ว รอช่วงใหม่
        if (burstCount >= BURST_SIZE && now - lastShotTime > nextShotDelay) {
            burstCount = 0;
            nextShotDelay = 2000 + random.nextInt(1000); // 2-3 วิ
        }
    }

    @Override
    protected void onDeath() {
        super.onDeath();
    }
}