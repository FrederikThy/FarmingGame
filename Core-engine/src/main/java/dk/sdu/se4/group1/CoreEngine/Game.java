package dk.sdu.se4.group1.CoreEngine;

import dk.sdu.se4.group1.CommonEcs.EcsSystem;
import dk.sdu.se4.group1.CommonEcs.World;
import dk.sdu.se4.group1.Inventory.InventoryFactory;
import dk.sdu.se4.group1.Inventory.InventoryPlugin;
import dk.sdu.se4.group1.Map.MapFactory;
import dk.sdu.se4.group1.Map.MappingSystem;
import dk.sdu.se4.group1.Monitoring.CPUCounter;
import dk.sdu.se4.group1.Monitoring.FPSCounter;
import dk.sdu.se4.group1.Monitoring.MemoryCounter;
import dk.sdu.se4.group1.Robot.RobotFactory;
import dk.sdu.se4.group1.Shop.ShopFactory;
import dk.sdu.se4.group1.Shop.ShopPlugin;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private final List<EcsSystem> discoveredSystems;
    private long lastTime = 0;

    Game(List<EcsSystem> discoveredSystems) {
        this.discoveredSystems = discoveredSystems;
    }

    public void start(Stage window) {
        World world = new World();

        // Create world entities
        InventoryFactory.createInventory(world);
        ShopFactory.createShop(world);
        MapFactory.createGrowthMap(world);

        // UI-bound systems
        ShopPlugin shop = new ShopPlugin(world);
        InventoryPlugin inventory = new InventoryPlugin(world, shop);

        // Build scene
        Pane root = new Pane();
        Image backgroundImage = new Image(Game.class.getResource("/Map.png").toExternalForm());
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitHeight(960);
        backgroundView.setFitWidth(960);
        backgroundView.setPreserveRatio(false);
        backgroundView.setSmooth(false);

        Canvas canvas = new Canvas(960, 960);
        root.getChildren().addAll(backgroundView, canvas);

        FPSCounter fpsCounter = new FPSCounter();
        CPUCounter cpuCounter = new CPUCounter();
        MemoryCounter memoryCounter = new MemoryCounter();
        root.getChildren().addAll(fpsCounter, cpuCounter, memoryCounter);
        root.getChildren().addAll(shop, inventory);

        GraphicsContext gc = canvas.getGraphicsContext2D();


        List<EcsSystem> allSystems = new ArrayList<>(discoveredSystems);
        allSystems.add(new MappingSystem(gc));
        allSystems.add(shop);
        allSystems.add(inventory);

        // Spawn robots
        RobotFactory robotFactory = new RobotFactory();
        robotFactory.HarvestingRobot(world, 1, 1, 2, 2);
        robotFactory.PlantingRobot(world, 2, 2, 9, 9);
        robotFactory.RemoveWeedRobot(world, 3, 3, 2, 9);

        Scene scene = new Scene(root, 960, 960);
        window.setTitle("Farming Game");
        window.setScene(scene);
        window.show();

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastTime == 0) { lastTime = now; return; }
                double dt = (now - lastTime) / 1_000_000_000.0;
                lastTime = now;
                for (EcsSystem system : allSystems) {
                    system.update(world, dt);
                }
                fpsCounter.OnFrame(dt);
                cpuCounter.OnFrame(dt);
                memoryCounter.OnFrame(dt);
            }
        }.start();
    }
}

