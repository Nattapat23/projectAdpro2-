package se233.project_2.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class MenuView extends VBox {
    private Button startButton;

    public MenuView() {
        Label title = new Label("Con-mhe");
        title.setFont(new Font("Arial", 40));
        title.setTextFill(Color.WHITE);

        startButton = new Button("START GAME");
        startButton.setFont(new Font("Arial", 24));

        this.setSpacing(20);
        this.setAlignment(Pos.CENTER);
        this.getChildren().addAll(title, startButton);
        this.setStyle("-fx-background-color: #222222;");
        this.setPrefSize(GameStage.WIDTH, GameStage.HEIGHT);
    }

    public Button getStartButton() {
        return startButton;
    }
}