package dk.sdu.se4.group1.CoreEngine;

import dk.sdu.se4.group1.CommonApi.SeedType;
import dk.sdu.se4.group1.CommonEcs.Components.InventoryComponent;
import dk.sdu.se4.group1.CommonEcs.EntityID;
import dk.sdu.se4.group1.Inventory.InventoryPlugin;
import dk.sdu.se4.group1.Inventory.InventoryFactory;
import dk.sdu.se4.group1.CommonEcs.World;
import dk.sdu.se4.group1.Monitoring.CPUCounter;
import dk.sdu.se4.group1.Monitoring.FPSCounter;
import dk.sdu.se4.group1.Monitoring.MemoryCounter;
import dk.sdu.se4.group1.Pathfinding.AStarPathfinding;
import dk.sdu.se4.group1.Pathfinding.PathfindingSystem;
import dk.sdu.se4.group1.Robot.*;
import dk.sdu.se4.group1.Shop.ShopStore;
import dk.sdu.se4.group1.Weed.WeedSystem;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import dk.sdu.se4.group1.Map.MappingSystem;
import dk.sdu.se4.group1.Crops.cropSystem;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class Main extends Application {
    private ShopStore shop;
    private InventoryPlugin inventory;
    private EntityID inventoryId;
    private long lastTime = 0;

    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage window) throws Exception {
        World world = new World(); //creates world instance
        SystemRegistry registry = new SystemRegistry(); //Creates system registry instance
        shop = new ShopStore();
        inventory = new InventoryPlugin();
        inventoryId = InventoryFactory.createInventory(world);

        InventoryComponent inventoryComponent =
                (InventoryComponent) world.GetComponent(inventoryId, InventoryComponent.class);
        inventoryComponent.addHarvest(SeedType.CARROT);

        inventoryComponent.addHarvest(SeedType.CARROT);
        inventoryComponent.addHarvest(SeedType.CHILI);
        Pane root = new Pane();

        Image backgroundImage = new Image(Main.class.getResource("/Map.png").toExternalForm());
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitHeight(960);
        backgroundView.setFitWidth(960);
        backgroundView.setPreserveRatio(false);
        backgroundView.setSmooth(false);

        Button shopButton = new Button();
        shopButton.setLayoutX(710);
        shopButton.setLayoutY(180);
        shopButton.setPrefWidth(230);
        shopButton.setPrefHeight(150);
        shopButton.setStyle("-fx-background-color: rgba(255, 255, 255, 0.3);");
        shopButton.setOnAction(e -> {
            shop.openShop(world);
        });

        Button invitoryButton = new Button();
        invitoryButton.setLayoutX(710);
        invitoryButton.setLayoutY(521);
        invitoryButton.setPrefWidth(230);
        invitoryButton.setPrefHeight(150);
        invitoryButton.setStyle("-fx-background-color: rgba(255, 255, 255, 0.3);");
        invitoryButton.setOnAction(e -> {
            inventory.showInventory(world, inventoryId);
        });

        Canvas canvas = new Canvas(960,960);
        root.getChildren().addAll(backgroundView, canvas);

        // Monitoring overlays (from MonitoringModule)
        FPSCounter    fpsCounter    = new FPSCounter();
        CPUCounter    cpuCounter    = new CPUCounter();
        MemoryCounter memoryCounter = new MemoryCounter();
        root.getChildren().addAll(fpsCounter, cpuCounter, memoryCounter);

        GraphicsContext gc = canvas.getGraphicsContext2D();

        registerSystems(registry, gc);
        root.getChildren().add(shopButton);
        root.getChildren().add(invitoryButton);

        // Opret RenderSystem med gc
        //Adds graphic content to mappingsystem

        Scene scene = new Scene(root, 960, 960);
        window.setTitle("Farming Game");
        window.setScene(scene);
        window.show();

        // One robot: top-left (0,0) → bottom-right (9,9)
        // Change these four numbers to set any A→B route.
        new RobotFactory().BaseRobot(world, 0, 0, 9, 9);
        RobotFactory robotFactory = new RobotFactory();


        EntityID firstrobotid = robotFactory.HarvestingRobot(world,1,1,8,8);

        EntityID firstrobotid2 = robotFactory.PlantingRobot(world,2,2,7,7);

        EntityID firstrobotid3 = robotFactory.RemoveWeedRobot(world,3,3,6,6);

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
        registry.register(new PathfindingSystem(new AStarPathfinding()));
        registry.register(new MovementSystem());// must be before RobotSystem
        registry.register(new HarvestingSystem());
        registry.register(new PlantingSystem());
        registry.register(new RemoveWeedSystem());
        registry.register(new WeedSystem());
        registry.register(new MappingSystem(gc));
        registry.register(new cropSystem());
        registry.register(shop);
        registry.register(inventory);
    }
}
