package dk.sdu.se4.group1.CoreEngine;


import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
/**
 * Hello world!
 */
public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage window) throws Exception {
        StackPane root = new StackPane();

        var scene = new Scene(root, 800, 600);

        window.setTitle("RAWR");
        window.setScene(scene);
        window.show();
    }
}
