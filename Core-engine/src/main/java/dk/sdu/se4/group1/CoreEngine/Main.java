package dk.sdu.se4.group1.CoreEngine;

import dk.sdu.se4.group1.CommonEcs.World;
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
        World world = new World(); //creates world instance
        SystemRegistry registry = new SystemRegistry(); //Creates system registry instance

        registerSystems(registry); //Adds all systems to the current instance

        StackPane root = new StackPane();

        var scene = new Scene(root, 800, 600);

        window.setTitle("RAWR");
        window.setScene(scene);
        window.show();
    }


    private void registerSystems(SystemRegistry registry){
        // Insert Systems here like this:
        // registry.register(new *SystemName()*
        // Systems should be an implementation of the update method and implement the interface EcsSystem
    }
}
