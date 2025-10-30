package se233.project_2;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import se233.project_2.controller.Drawingloop;
import se233.project_2.controller.Gameloop;
import se233.project_2.model.Keys;
import se233.project_2.model.character.Boss1;
import se233.project_2.model.character.Boss2; // ADDED
import se233.project_2.model.character.Boss3; // ADDED
import se233.project_2.model.character.GameCharacter;
import se233.project_2.view.GameStage;

public class Launcher extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // --- 1. สร้าง Model และ View ---
        Keys keys = new Keys();
        GameStage gameStage = new GameStage();

        // --- 2. สร้าง Entities (ตัวละคร) ---
        GameCharacter player = new GameCharacter(
                0,
                100, GameStage.GROUND - 64, // x, y
                "player.png", // imgName
                6, 6, 8, // count, column, row
                64, 64,  // width, height
                KeyCode.A, KeyCode.D, KeyCode.W, KeyCode.S, KeyCode.SPACE // L, R, Up, Down, Shoot
        );

        // --- 3. สร้างบอส (เรียงลำดับ) ---
        Boss1 boss1 = new Boss1(600, GameStage.GROUND - 100);
        Boss2 boss2 = new Boss2(600, GameStage.GROUND - 120); // (ต้องสร้างคลาส Boss2.java)
        Boss3 boss3 = new Boss3(500, GameStage.GROUND - 150); // (ต้องสร้างคลาส Boss3.java)

        // --- 4. เพิ่ม Entities ลงในฉาก (เพิ่มแค่ Player) ---
        // *** บอสจะถูกเพิ่มโดย Gameloop ***
        gameStage.getChildren().add(player);

        // --- 5. สร้าง Scene ---
        Scene scene = new Scene(gameStage, GameStage.WIDTH, GameStage.HEIGHT);

        // --- 6. ตั้งค่า Input ---
        scene.setOnKeyPressed(event -> keys.press(event.getCode()));
        scene.setOnKeyReleased(event -> keys.release(event.getCode()));

        // --- 7. สร้าง Controller (GameLoop และ DrawingLoop) ---
        // MODIFIED: สร้าง Logic Loop ก่อน
        Gameloop gameloop = new Gameloop(gameStage, player, keys, boss1, boss2, boss3);

        // MODIFIED: สร้าง Drawing Loop โดยส่ง Logic Loop เข้าไป
        Drawingloop drawingloop = new Drawingloop(gameStage, gameloop);

        // --- 8. Start Loops ---
        gameloop.start(); // สั่งให้ Thread ตรรกะ เริ่มทำงาน
        drawingloop.start(); // สั่งให้ JavaFX (วาดภาพ) เริ่มทำงาน

        // --- 9. แสดงผล ---
        primaryStage.setTitle("SE233 Project II - Contra Boss Rush");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}