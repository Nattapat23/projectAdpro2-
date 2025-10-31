package se233.project_2.view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import se233.project_2.model.Keys;
import se233.project_2.model.character.GameCharacter;

import java.util.Objects;

public class GameStage extends Pane {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 400;
    public final static int GROUND = 300;
    private Image gameStageImg;
    private GameCharacter gameCharacter;
    private Keys keys;
    public GameStage(){
        keys = new Keys();
        gameStageImg = new Image(Objects.requireNonNull(GameStage.class.getResourceAsStream("/se233/project_2/Background.png")));
        ImageView background = new ImageView(gameStageImg);
        background.setFitWidth(WIDTH);
        background.setFitHeight(HEIGHT);
        gameCharacter = new GameCharacter(0,30,30, 32, 64,KeyCode.A,KeyCode.D,KeyCode.W,KeyCode.S,KeyCode.SPACE);
        getChildren().addAll(background, gameCharacter);
    }
    public GameCharacter getGameCharacter() {
        return gameCharacter;
    }
    public Keys getKeys() {
        return keys;
    }
}