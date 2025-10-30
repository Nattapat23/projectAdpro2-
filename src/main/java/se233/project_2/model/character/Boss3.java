// Boss2.java (ตัวอย่าง)
package se233.project_2.model.character;
// (imports ...)
public class Boss3 extends BaseBoss {
    public Boss3(int x, int y) {
        super(x, y, 150, 2000); // พลังชีวิตเยอะขึ้น คะแนนเยอะขึ้น
        // TODO: โหลด Sprite ของ Boss 2
        // this.imageView = new AnimatedSprite(...)
        // getChildren().add(imageView);
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