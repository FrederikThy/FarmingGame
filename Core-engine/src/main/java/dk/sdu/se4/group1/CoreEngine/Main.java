package dk.sdu.se4.group1.CoreEngine;

import dk.sdu.se4.group1.Monitoring.CPUCounter;
import dk.sdu.se4.group1.Monitoring.FPSCounter;
import dk.sdu.se4.group1.CommonEcs.EntityID;
import dk.sdu.se4.group1.CommonEcs.World;
import dk.sdu.se4.group1.Monitoring.MemoryCounter;
import dk.sdu.se4.group1.Robot.RobotFactory;
import dk.sdu.se4.group1.Robot.RobotSystem;
import dk.sdu.se4.group1.Weed.WeedSystem;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import dk.sdu.se4.group1.Map.MappingSystem;
import dk.sdu.se4.group1.Crops.cropSystem;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;



public class Main extends Application {

    private long lastTime = 0;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage window) throws Exception {
        World world = new World(); //creates world instance
        SystemRegistry registry = new SystemRegistry(); //Creates system registry instance

        Pane root = new Pane();

        //Get Map picture from resources
        Image OriginalbackgroundImage = new Image(Main.class.getResource("/Map.png").toExternalForm());

        ImageView backgroundView = new ImageView(OriginalbackgroundImage);
        backgroundView.setFitHeight(960);
        backgroundView.setFitWidth(960);
        backgroundView.setPreserveRatio(false);
        backgroundView.setSmooth(false);

        Canvas canvas = new Canvas(960,960);
        root.getChildren().addAll(backgroundView, canvas);

        CPUCounter cpuCounter = new CPUCounter();
        root.getChildren().add(cpuCounter);

        FPSCounter fpsCounter = new FPSCounter(); //Making an instance of FPSCounter Module
        root.getChildren().add(fpsCounter);

        MemoryCounter memoryCounter = new MemoryCounter();
        root.getChildren().add(memoryCounter);

        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Opret RenderSystem med gc
        //Adds graphic content to mappingsystem

        registerSystems(registry, gc); //Adds all systems to the current instance

        // set scene and stage
        Scene scene = new Scene(root, 960, 960);
        window.setTitle("Farming Game");
        window.setScene(scene);
        window.show();

        RobotFactory robotFactory = new RobotFactory();

        EntityID firstrobotid = robotFactory.createRobot(world);

        EntityID firstrobotid2 = robotFactory.createRobot(world);

        EntityID firstrobotid3 = robotFactory.createRobot(world);

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
                fpsCounter.OnFrame(deltaTime);
                cpuCounter.OnFrame(deltaTime);
                memoryCounter.OnFrame(deltaTime);

            }
        };

        timer.start();
    }


    private void registerSystems(SystemRegistry registry, GraphicsContext gc) {
        // Insert Systems here like this:
        // registry.register(new *SystemName()*)
        // Systems should be an implementation of the update method and implement the interface EcsSystem
        registry.register(new RobotSystem());
        registry.register(new WeedSystem());
        registry.register(new MappingSystem(gc));
        registry.register(new cropSystem());
    }
}