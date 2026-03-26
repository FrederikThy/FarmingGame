package dk.sdu.se4.group1.CoreEngine;

import dk.sdu.se4.group1.CommonEcs.World;
import dk.sdu.se4.group1.Robot.RobotPlugin;
import dk.sdu.se4.group1.Robot.RobotSystem;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
/**
 * Hello world!
 */
public class Main extends Application {

    private long lastTime = 0;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage window) throws Exception {
        World world = new World(); //creates world instance
        SystemRegistry registry = new SystemRegistry(); //Creates system registry instance

        //Jeg ved godt vi har snakket om at bruge factories, men jeg er ikke sikker
        // på hvordan vi skal gøre det. Derfor bliver det bare lige i main for nu
        RobotPlugin robot = new RobotPlugin(2.0, 100, 100);
        world.AddEntity(robot);
        registry.register(new RobotSystem());
        //Laver cirkel
        Circle robotCircle = new Circle(10);
        robotCircle.setFill(Color.RED);
        robotCircle.setCenterX(robot.getX());
        robotCircle.setCenterY(robot.getY());


        registerSystems(registry); //Adds all systems to the current instance

        // Jeg har ændret det fra stackpane til pane, fordi stackpane nogle gange
        // kan være træls
        Pane root = new Pane();

        Scene scene = new Scene(root, 800, 600);

        // Opretter cirkel
        root.getChildren().add(robotCircle);
        // En måde hvorpå vi kan kalde koden ca 60 gange i sekundet.
        // Vi kalder UpdateAll metoden, som kører alle systems.
        // Alle systemer bliver opdateret. Fordi robot implementerer Common-ecs
        // vil den også blive opdateret.
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastTime == 0) {
                    lastTime = now;
                    return;
                }

                double deltaTime = (now - lastTime) / 1_000_000_000.0;
                lastTime = now;
                // Kalder systems
                registry.updateAll(world, deltaTime);
                // Opdaterer cirkels x og y koordinater
                robotCircle.setCenterX(robot.getX());
                robotCircle.setCenterY(robot.getY());
            }
        };

        timer.start();

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
