module se233.project_2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires org.apache.logging.log4j;

    requires java.desktop;
    requires jlayer;
    requires javafx.media;


    opens se233.project_2 to javafx.fxml;
    exports se233.project_2;
}