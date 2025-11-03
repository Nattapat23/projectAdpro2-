package se233.project_2.model;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class AnimatedSprite extends ImageView {
    int count, columns, rows, offsetX, offsetY, width, height, curIndex, curColumnIndex = 0, curRowIndex = 0;
    int[] framesPerRow; // จำนวนเฟรมในแต่ละแถว

    public AnimatedSprite(Image image, int count, int columns, int rows, int offsetX, int offsetY, int width, int height) {
        this.setImage(image);
        this.count = count;
        this.columns = columns;
        this.rows = rows;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.width = width;
        this.height = height;
        this.setViewport(new Rectangle2D(offsetX, offsetY, width, height));
    }

    // Constructor ใหม่ที่รับจำนวนเฟรมแต่ละแถว
    public AnimatedSprite(Image image, int[] framesPerRow, int offsetX, int offsetY, int width, int height) {
        this.setImage(image);
        this.framesPerRow = framesPerRow;
        this.rows = framesPerRow.length;
        this.columns = 0;
        for (int frames : framesPerRow) {
            if (frames > this.columns) this.columns = frames;
        }
        this.count = 0;
        for (int frames : framesPerRow) {
            this.count += frames;
        }
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.width = width;
        this.height = height;
        this.setViewport(new Rectangle2D(offsetX, offsetY, width, height));
    }
    public void tick() {
        curColumnIndex++;

        // ถ้ามีการกำหนด framesPerRow ให้ใช้จำนวนเฟรมของแถวปัจจุบัน
        int maxFramesInCurrentRow = (framesPerRow != null && curRowIndex < framesPerRow.length)
                ? framesPerRow[curRowIndex]
                : columns;

        if (curColumnIndex >= maxFramesInCurrentRow) {
            curColumnIndex = 0;
        }

        interpolate();
    }
    protected void interpolate() {
        final int x = curColumnIndex * width + offsetX;
        final int y = curRowIndex * height + offsetY;
        this.setViewport(new Rectangle2D(x, y, width, height));
    }

    public int getCurIndex() {
        return curIndex;
    }

    public void setCurIndex(int curIndex) {
        this.curIndex = curIndex;
    }

    public int getCurColumnIndex() {
        return curColumnIndex;
    }

    public void setCurColumnIndex(int curColumnIndex) {
        this.curColumnIndex = curColumnIndex;
        interpolate();  // อัปเดต viewport ทันที
    }

    public int getCurRowIndex() {
        return curRowIndex;
    }

    public void setCurRowIndex(int curRowIndex) {
        if (this.curRowIndex != curRowIndex) {
            this.curRowIndex = curRowIndex;
            this.curColumnIndex = 0;  // รีเซ็ตเฟรมกลับไปเริ่มต้นของแถวใหม่
            interpolate();  // อัปเดต viewport ทันที
        }
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    public int[] getFramesPerRow() {
        return framesPerRow;
    }

    public void setFramesPerRow(int[] framesPerRow) {
        this.framesPerRow = framesPerRow;
    }
}