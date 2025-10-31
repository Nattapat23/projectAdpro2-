module se233.project_2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    opens se233.project_2 to javafx.fxml;
    exports se233.project_2;
}