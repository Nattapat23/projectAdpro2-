package se233.project_2;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import se233.project_2.controller.DrawingLoop;
import se233.project_2.controller.GameLoop;
import se233.project_2.view.GameStage;
import se233.project_2.view.MenuView;

public class Launcher extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        // สร้าง root Pane เพื่อสลับฉาก
        Pane root = new Pane();
        Scene scene = new Scene(root, GameStage.WIDTH, GameStage.HEIGHT);

        // สร้างหน้าจอเมนูและเกม
        MenuView menuView = new MenuView();
        GameStage gameStage = new GameStage();

        // ส่ง reference ของ root pane ให้ GameStage เพื่อให้สามารถกลับมาเมนูได้
        gameStage.setRootPane(root);

        // เพิ่มเฉพาะหน้าเมนูก่อน
        root.getChildren().add(menuView);

        // ตั้งค่าปุ่ม Start
        menuView.getStartButton().setOnAction(e -> {
            // เอาเมนูออก
            root.getChildren().remove(menuView);
            // เพิ่มฉากเกม
            root.getChildren().add(gameStage);

            // เริ่มต้นเกม (ตั้งค่าด่าน 1)
            gameStage.startGame();

            // ตั้งค่าการควบคุม
            scene.setOnKeyPressed(keyEvent -> gameStage.getKeys().add(keyEvent.getCode()));
            scene.setOnKeyReleased(keyEvent -> gameStage.getKeys().remove(keyEvent.getCode()));
            gameStage.requestFocus();

            // เริ่ม Game Loops
            GameLoop gameLoop = new GameLoop(gameStage);
            DrawingLoop drawingLoop = new DrawingLoop(gameStage);
            (new Thread(gameLoop)).start();
            (new Thread(drawingLoop)).start();
        });

        stage.setTitle("Mario Boss Shooter");
        stage.setScene(scene);
        stage.show();
    }
}