package dk.sdu.se4.group1.CoreEngine;

import dk.sdu.se4.group1.CommonApi.SeedType;
import dk.sdu.se4.group1.CommonEcs.Components.InventoryComponent;
import dk.sdu.se4.group1.CommonEcs.EntityID;
import dk.sdu.se4.group1.CommonEcs.MapSize;
import dk.sdu.se4.group1.Crops.IntercroppingSystem;
import dk.sdu.se4.group1.Inventory.InventoryPlugin;
import dk.sdu.se4.group1.Inventory.InventoryFactory;
import dk.sdu.se4.group1.CommonEcs.World;
import dk.sdu.se4.group1.Map.MapFactory;
import dk.sdu.se4.group1.CommonEcs.Components.PathfindingUpgradeComponent;
import dk.sdu.se4.group1.Monitoring.CPUCounter;
import dk.sdu.se4.group1.Monitoring.FPSCounter;
import dk.sdu.se4.group1.Monitoring.MemoryCounter;
import dk.sdu.se4.group1.Monitoring.PathfindingHUDLabel;
import dk.sdu.se4.group1.Pathfinding.PathfindingSystem;
import dk.sdu.se4.group1.Robot.*;
import dk.sdu.se4.group1.Shop.ShopStore;
import dk.sdu.se4.group1.Robot.RobotFactory;
import dk.sdu.se4.group1.Shop.ShopFactory;
import dk.sdu.se4.group1.Shop.ShopPlugin;
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

import java.util.Random;


public class Main extends Application {
    private ShopPlugin shop;
    private InventoryPlugin inventory;
    private long lastTime = 0;

    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage window) throws Exception {
        World world = new World(); //creates world instance
        SystemRegistry registry = new SystemRegistry(); //Creates system registry instance
        EntityID inventoryId = InventoryFactory.createInventory(world);
        EntityID shopId = ShopFactory.createShop(world);
        EntityID mapId = MapFactory.createGrowthMap(world);

        // Pathfinding upgrade entitys
        EntityID pathfindingUpgradeId = world.createEntity();
        world.addComponent(pathfindingUpgradeId, new PathfindingUpgradeComponent());

        shop = new ShopPlugin(world);
        inventory = new InventoryPlugin(world, shop);
        Pane root = new Pane();

        Image backgroundImage = new Image(Main.class.getResource("/Map.png").toExternalForm());
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitHeight(960);
        backgroundView.setFitWidth(960);
        backgroundView.setPreserveRatio(false);
        backgroundView.setSmooth(false);


        Canvas canvas = new Canvas(960,960);
        root.getChildren().addAll(backgroundView, canvas);

        // Monitoring overlays (from MonitoringModule)
        FPSCounter          fpsCounter          = new FPSCounter();
        CPUCounter          cpuCounter          = new CPUCounter();
        MemoryCounter       memoryCounter       = new MemoryCounter();
        PathfindingHUDLabel pathfindingLabel    = new PathfindingHUDLabel(world);
        root.getChildren().addAll(fpsCounter, cpuCounter, memoryCounter, pathfindingLabel);

        GraphicsContext gc = canvas.getGraphicsContext2D();

        registerSystems(registry, gc);
        root.getChildren().add(shop);
        root.getChildren().add(inventory);

        // Opret RenderSystem med gc
        //Adds graphic content to mappingsystem

        Scene scene = new Scene(root, 960, 960);
        window.setTitle("Farming Game");
        window.setScene(scene);
        window.show();

        // One robot: top-left (0,0) → bottom-right (9,9)
        // Change these four numbers to set any A→B route.
        RobotFactory robotFactory = new RobotFactory();

       // spawnStressTestRobots(world, robotFactory);

        EntityID firstrobotid = robotFactory.HarvestingRobot(world,1,1,2,2);

        EntityID firstrobotid2 = robotFactory.PlantingRobot(world,2,2,9,9);

        EntityID firstrobotid3 = robotFactory.RemoveWeedRobot(world,3,3,2,9);

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
                pathfindingLabel.onFrame();
            }
        };
        timer.start();
    }

    /*private void spawnStressTestRobots(World world, RobotFactory robotFactory) {
        Random random = new Random();
        int mapWidth  = MapSize.MAP_WIDTH;
        int mapHeight = MapSize.MAP_HEIGHT;

        for (int i = 0; i < 6000; i++) {
            int startX = random.nextInt(mapWidth);
            int startY = random.nextInt(mapHeight);
            int goalX  = random.nextInt(mapWidth);
            int goalY  = random.nextInt(mapHeight);
            robotFactory.HarvestingRobot(world, startX, startY, goalX, goalY);
        }

        System.out.println("Spawned 1000 stress test robots");
    }*/
    private void registerSystems(SystemRegistry registry, GraphicsContext gc) {
        registry.register(new PathfindingSystem()); // algorithm read live from PathfindingUpgradeComponent — upgrade in shop
        registry.register(new MovementSystem()); // steps robots along their PathComponent waypoints
        registry.register(new HarvestingSystem());   // harvests crops adjacent to HarvestingRobots
        registry.register(new PlantingSystem());     // plants seeds near PlantingRobots
        registry.register(new RemoveWeedSystem());   // removes weeds near RemoveWeedRobots
        registry.register(new WeedSystem());
        registry.register(new RobotTaskSystem());
        registry.register(new MappingSystem(gc));
        registry.register(new cropSystem());
        registry.register(new IntercroppingSystem());
        registry.register(shop);
        registry.register(inventory);
    }
}
