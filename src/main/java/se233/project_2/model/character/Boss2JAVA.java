package se233.project_2.model.character;

import javafx.scene.paint.Color;
import se233.project_2.view.GameStage;

import java.util.Random;

/**
 * บอสด่าน 2: ป้อมปราการหนัก
 * - ไม่เคลื่อนที่
 * - มี 2 ท่า: ปกติ และท่าปล่อยสกิล (ปล่อยมินเนี่ยน)
 * - ปล่อยมินเนี่ยน 2-3 ตัวต่อรอบ
 */
public class Boss2JAVA extends BaseBoss {
    private long lastActionTime = 0;
    private long nextActionDelay = 3000;
    private Random random = new Random();
    private boolean isChargingSkill = false;
    private long skillChargeStart = 0;
    private static final long SKILL_CHARGE_TIME = 1500; // ชาร์จ 1.5 วิ
    public double size =2 ;
    public Boss2JAVA(int x, int y) {
        super(x, y, 112, 110, 300, Color.rgb(50, 100, 200), "/se233/project_2/Boss2.png",2,1);
       this.setImage(size);
        this.vulnerable = true;


    }

    @Override
    public void update(GameStage gameStage) {
        if (dead) return;

        long now = System.currentTimeMillis();

        // ถ้ากำลังชาร์จสกิล
        if (isChargingSkill) {
            if (now - skillChargeStart > SKILL_CHARGE_TIME) {
                // ปล่อยมินเนี่ยน 2-3 ตัว
                int minionCount = 2 + random.nextInt(2);
                for (int i = 0; i < minionCount; i++) {
                    int x = (int) getTranslateX() + 60;
                    int y = (int) getTranslateY() + 100 + (i * 60);
                    gameStage.spawnMinion(x, y);

                }

                isChargingSkill = false;
                lastActionTime = now;
                nextActionDelay = 4000 + random.nextInt(2000); // 4-6 วิ

                // กลับมาท่าปกติ
                changeToNormalPose();

            }
        } else {
            // เช็คว่าถึงเวลาใช้สกิลหรือยัง
            if (now - lastActionTime > nextActionDelay) {
                isChargingSkill = true;
                skillChargeStart = now;
                changeToSkillPose();
            }
        }
    }

    private void changeToSkillPose() {
            imageView.tick();
    }

    private void changeToNormalPose() {
            imageView.tick();

    }

    @Override
    protected void onDeath() {

        super.onDeath();
    }
}