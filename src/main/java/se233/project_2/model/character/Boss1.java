package se233.project_2.model.character;

import javafx.scene.image.Image;
import se233.project_2.Launcher;
import se233.project_2.model.AnimatedSprite;

// MODIFIED: extends BaseBoss
public class Boss1 extends BaseBoss {

    public Boss1(int x, int y) {
        // MODIFIED: เรียก super constructor
        super(x, y, 100, 1000); // (x, y, health, score)

        // TODO: เปลี่ยน "boss1.png"
        Image bossImg = new Image(Launcher.class.getResourceAsStream("boss1.png"));
        // TODO: ปรับค่า sprite (count, col, row, w, h)
        this.imageView = new AnimatedSprite(bossImg, 4, 4, 1, 0, 0, 100, 100);

        getChildren().add(imageView);
    }

    @Override
    public void updateLogic() { // MODIFIED: ใช้ชื่อเมธอดใหม่
        // TODO: ใส่ AI ของ Boss 1 (Defense Wall) [cite: 27]
        // เช่น ยิงกระสุนเป็นระยะ
        // if (Math.random() < 0.01) {
        //    spawnEnemyBullet();
        // }

        // *** ลบบรรทัด this.imageView.tick() ออกจากที่นี่ ***
        // (ย้ายไป Drawingloop แล้ว)
    }
}