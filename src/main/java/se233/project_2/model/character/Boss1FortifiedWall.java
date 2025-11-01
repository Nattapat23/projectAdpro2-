package se233.project_2.model.character;

import javafx.scene.paint.Color;
import se233.project_2.view.GameStage;

import java.util.List;

/**
 * บอสด่าน 1: กำแพงป้อมปราการ
 * - ไม่เคลื่อนที่
 * - มีป้อมปืน 2 ตัวปกป้อง
 * - ต้องทำลายป้อมปืนก่อนถึงจะโจมตีแกนหลักได้
 */
public class Boss1FortifiedWall extends BaseBoss {
    private List<Turret> turrets;

    public Boss1FortifiedWall(int x, int y, List<Turret> turrets) {
        super(x, y, 100, 200, 200, Color.rgb(80, 80, 80));
        this.turrets = turrets;
        this.vulnerable = false; // เริ่มต้นโจมตีไม่ได้
    }

    @Override
    public void update(GameStage gameStage) {
        // ตรวจสอบว่าป้อมปืนหมดหรือยัง
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
    }

    @Override
    protected void onDeath() {
        super.onDeath();
        // อาจเพิ่ม animation หรือ sound effect ตอนตาย
    }
}