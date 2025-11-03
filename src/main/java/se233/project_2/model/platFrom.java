package se233.project_2.model;

import javafx.scene.layout.Pane;

public class platFrom extends Pane {
    int x;
    int y;
    int width;
    int height;

    public platFrom(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.setTranslateX(x);
        this.setTranslateY(y);
        this.setPrefWidth(width);
        this.setPrefHeight(height);

//        this.setStyle("-fx-background-color: #6E2C00; -fx-border-color: #D35400; -fx-border-width: 2;");
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getwidth() {
        return width;
    }
    public int getheight() {
        return height;
    }

}
