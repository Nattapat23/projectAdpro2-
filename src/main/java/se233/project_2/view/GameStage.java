package se233.project_2.view;

import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import se233.project_2.Launcher;

// MODIFIED: ให้ GameStage เป็น Pane หลัก
public class GameStage extends Pane {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 400;
    public final static int GROUND = 300; // พื้น

    private Image gameStageImg;
    private Score  scoreList;

    public GameStage() {
        setPrefSize(WIDTH, HEIGHT);

        // TODO: เปลี่ยน "background.png" เป็นภาพพื้นหลังของคุณ
        gameStageImg = new Image(Launcher.class.getResourceAsStream("background.png"));
        BackgroundImage bgImg = new BackgroundImage(
                gameStageImg,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(WIDTH, HEIGHT, false, false, false, false)
        );
        setBackground(new Background(bgImg));

        // สร้าง Score display
        scoreList = new Score(10, 10);
        getChildren().add(scoreList);
    }

    public Score getScoreList() {
        return scoreList;
    }
}