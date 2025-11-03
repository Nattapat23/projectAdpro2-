package se233.project_2;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import se233.project_2.controller.AudioFeatures;
import se233.project_2.controller.DrawingLoop;
import se233.project_2.controller.GameLoop;
import se233.project_2.view.GameStage;
import se233.project_2.view.MenuView;

public class Launcher extends Application {
    private GameLoop gameLoop;
    private DrawingLoop drawingLoop;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Pane root = new Pane();
        Scene scene = new Scene(root, GameStage.WIDTH, GameStage.HEIGHT);
        AudioFeatures.playOpenSound();
        MenuView menuView = new MenuView();
        root.getChildren().add(menuView);

        menuView.getStartButton().setOnAction(e -> {
            // หยุดลูปเก่าถ้ามี
            if (gameLoop != null) gameLoop.stop();
            if (drawingLoop != null) drawingLoop.stop();

            // ลบฉากเก่า
            root.getChildren().clear();

            // สร้างฉากเกมใหม่
            GameStage newGameStage = new GameStage();
            newGameStage.setRootPane(root);
            newGameStage.setScene(scene); // ส่ง Scene ไปด้วย
            root.getChildren().add(newGameStage);

            // เริ่มต้นเกม
            newGameStage.startGame();

            // ตั้งค่าการควบคุม
            scene.setOnKeyPressed(keyEvent -> newGameStage.getKeys().add(keyEvent.getCode()));
            scene.setOnKeyReleased(keyEvent -> newGameStage.getKeys().remove(keyEvent.getCode()));
            newGameStage.requestFocus();

            // เริ่ม Loop ใหม่
            gameLoop = new GameLoop(newGameStage);
            drawingLoop = new DrawingLoop(newGameStage);

            Thread gameThread = new Thread(gameLoop);
            Thread drawThread = new Thread(drawingLoop);

            gameThread.setDaemon(true);
            drawThread.setDaemon(true);

            gameThread.start();
            drawThread.start();
        });

        stage.setTitle("Project2");
        stage.setScene(scene);
        stage.show();
    }
}