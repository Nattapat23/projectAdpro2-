package se233.project_2.model;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class AnimatedSprite extends ImageView {
    int count, columns, rows, offsetX, offsetY, width, height;

    // MODIFIED: แยก curColumnIndex (เฟรมปัจจุบัน) และ curRowIndex (ท่าทางปัจจุบัน)
    int curColumnIndex = 0;
    int curRowIndex = 0;
    int frameCount; // จำนวนเฟรมของท่าทางนั้นๆ

    public AnimatedSprite(Image image, int count, int columns, int rows, int offsetX, int offsetY, int width, int height) {
        this.setImage(image);
        this.count = count; // count นี้อาจหมายถึงจำนวนเฟรมทั้งหมด หรือจำนวนเฟรมต่อแถว
        // ผมจะสมมติว่า 'columns' คือจำนวนเฟรมต่อแถว
        this.columns = columns;
        this.rows = rows;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.width = width;
        this.height = height;
        this.frameCount = columns; // สมมติว่า 1 แถว = 1 ท่า = 'columns' เฟรม
        this.setViewport(new Rectangle2D(offsetX, offsetY, width, height));
    }

    public void setAnimationRow(int rowIndex) {
        if (this.curRowIndex != rowIndex) {
            this.curRowIndex = rowIndex;
            this.curColumnIndex = 0; // รีเซ็ตเฟรมเมื่อเปลี่ยนท่า
        }
    }


    public void setAnimationRow(int rowIndex, int frameCount) {
        if (this.curRowIndex != rowIndex) {
            this.curRowIndex = rowIndex;
            this.curColumnIndex = 0;
            this.frameCount = frameCount;
        }
    }


    public void tick() {
        // MODIFIED: วนลูปเฉพาะเฟรมในแถว (columns) ที่เลือก
        curColumnIndex = (curColumnIndex + 1) % frameCount;
        interpolate();
    }

    protected void interpolate() {
        // MODIFIED: ใช้ curRowIndex ในการคำนวณ 'y'
        final int x = curColumnIndex * width + offsetX;
        final int y = curRowIndex * height + offsetY;
        this.setViewport(new Rectangle2D(x, y, width, height));
    }
}