package se233.project_2;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import se233.project_2.controller.DrawingLoop;
import se233.project_2.controller.GameLoop;
import se233.project_2.view.GameStage;

public class Launcher extends Application {
    public static void main(String[] args) {launch(args);}
    @Override
    public void start(Stage stage) {
        GameStage gameStage = new GameStage();
        GameLoop gameLoop = new GameLoop(gameStage);
        DrawingLoop drawingLoop = new DrawingLoop(gameStage);
        Scene scene = new Scene(gameStage, GameStage.WIDTH, GameStage.HEIGHT);
        scene.setOnKeyPressed(e -> gameStage.getKeys().add(e.getCode()));
        scene.setOnKeyReleased(e -> gameStage.getKeys().remove(e.getCode()));
        stage.setTitle("Mario");
        stage.setScene(scene);
        stage.show();
        gameStage.requestFocus();
        (new Thread(gameLoop)).start();
        (new Thread(drawingLoop)).start();
    }
}
