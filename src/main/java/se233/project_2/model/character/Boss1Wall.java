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
public class Boss1Wall extends BaseBoss {

    private List<Turret> turrets;
    public double size =0.4 ;
    public Boss1Wall(int x, int y, List<Turret> turrets) {
        super(x, y, 656, 517, 200, Color.rgb(80, 80, 80), "/se233/project_2/Boss.png",1,1);
        this.setWidth(1.3);
        this.setImage(size);

        this.turrets = turrets;
        this.vulnerable = false;
    }

    @Override
    public void update(GameStage gameStage) {
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
    }
}