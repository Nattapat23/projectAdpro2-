package se233.project_2.view;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Score extends Label {
    private int score = 100;

    public Score(int x, int y) {
        setTranslateX(x);
        setTranslateY(y);
        setTextFill(Color.WHITE);
        setFont(new Font("Arial", 24));
        update(0);
    
    }

    public void update(int score) {
        this.score = score;
        setText("SCORE: " + this.score);
    }
}