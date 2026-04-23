package dk.sdu.se4.group1.CoreEngine;

import dk.sdu.se4.group1.CommonEcs.World;
import dk.sdu.se4.group1.Monitoring.CPUCounter;
import dk.sdu.se4.group1.Monitoring.FPSCounter;
import dk.sdu.se4.group1.Monitoring.MemoryCounter;
import dk.sdu.se4.group1.Pathfinding.AStarPathfinding;
import dk.sdu.se4.group1.Pathfinding.PathfindingSystem;
import dk.sdu.se4.group1.Robot.RobotFactory;
import dk.sdu.se4.group1.Robot.RobotSystem;
import dk.sdu.se4.group1.Weed.WeedSystem;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import dk.sdu.se4.group1.Map.MappingSystem;
import dk.sdu.se4.group1.Crops.cropSystem;

public class Main extends Application {

    private long lastTime = 0;

    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage window) throws Exception {
        World world = new World();
        SystemRegistry registry = new SystemRegistry();

        Pane root = new Pane();

        Image backgroundImage = new Image(Main.class.getResource("/Map.png").toExternalForm());
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitHeight(960);
        backgroundView.setFitWidth(960);
        backgroundView.setPreserveRatio(false);
        backgroundView.setSmooth(false);

        Canvas canvas = new Canvas(960, 960);
        root.getChildren().addAll(backgroundView, canvas);

        // Monitoring overlays (from MonitoringModule)
        FPSCounter    fpsCounter    = new FPSCounter();
        CPUCounter    cpuCounter    = new CPUCounter();
        MemoryCounter memoryCounter = new MemoryCounter();
        root.getChildren().addAll(fpsCounter, cpuCounter, memoryCounter);

        GraphicsContext gc = canvas.getGraphicsContext2D();

        registerSystems(registry, gc);

        Scene scene = new Scene(root, 960, 960);
        window.setTitle("Farming Game");
        window.setScene(scene);
        window.show();

        // One robot: top-left (0,0) → bottom-right (9,9)
        // Change these four numbers to set any A→B route.
        new RobotFactory().createRobot(world, 0, 0, 9, 9);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastTime == 0) { lastTime = now; return; }
                double dt = (now - lastTime) / 1_000_000_000.0;
                lastTime = now;
                registry.updateAll(world, dt);
                fpsCounter.OnFrame(dt);
                cpuCounter.OnFrame(dt);
                memoryCounter.OnFrame(dt);
            }
        };
        timer.start();
    }

    private void registerSystems(SystemRegistry registry, GraphicsContext gc) {
        registry.register(new PathfindingSystem(new AStarPathfinding())); // must be before RobotSystem
        registry.register(new RobotSystem());
        registry.register(new WeedSystem());
        registry.register(new MappingSystem(gc));
        registry.register(new cropSystem());
    }
}
