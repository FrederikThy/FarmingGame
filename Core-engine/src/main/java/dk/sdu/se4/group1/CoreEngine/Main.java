package dk.sdu.se4.group1.CoreEngine;

import dk.sdu.se4.group1.CommonEcs.EntityID;
import dk.sdu.se4.group1.CommonEcs.PositionComponent;
import dk.sdu.se4.group1.CommonEcs.World;
import dk.sdu.se4.group1.Map.GoalFactory;
import dk.sdu.se4.group1.Map.MapFactory;
import dk.sdu.se4.group1.Map.MappingSystem;
import dk.sdu.se4.group1.Pathfinding.PathfindingSystem;
import dk.sdu.se4.group1.Robot.RobotFactory;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;




public class Main extends Application {

    private long lastTime = 0;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage window) throws Exception {
        World world = new World(); //creates world instance
        MapFactory.registerTiles(world, 10, 10); // initialize map tiles (must match MapSize 10x10)
        SystemRegistry registry = new SystemRegistry(); //Creates system registry instance

        Pane root = new Pane();
        Canvas canvas = new Canvas(800,600);
        root.getChildren().add(canvas);

        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Opret RenderSystem med gc
        //Adds graphic content to mappingsystem
        registry.register(new MappingSystem(gc));
        registerSystems(registry); //Adds all systems to the current instance

        // set scene and stage
        Scene scene = new Scene(root, 800, 600);
        window.setTitle("RAWR");
        window.setScene(scene);
        window.show();


        // --- Define point A (start) and point B (destination) ---
        int startX = 1, startY = 1; // Point A
        int goalX  = 8, goalY  = 8; // Point B

        // Spawn the gold "B" marker on the grid
        GoalFactory.createGoal(world, goalX, goalY);

        // Spawn the robot at point A, with B as its pathfinding target
        PositionComponent target = new PositionComponent(goalX, goalY);
        EntityID firstRobotId = RobotFactory.createRobot(world, startX, startY, 10, 10, target);
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
            }
        };

        timer.start();
    }


    private void registerSystems(SystemRegistry registry){
        // Insert Systems here like this:
        // registry.register(new *SystemName()*)
        // Systems should be an implementation of the update method and implement the interface EcsSystem
        registry.register(new PathfindingSystem());
    }
}
