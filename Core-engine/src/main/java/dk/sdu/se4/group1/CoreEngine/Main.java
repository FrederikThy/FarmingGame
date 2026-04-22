package dk.sdu.se4.group1.CoreEngine;

import dk.sdu.se4.group1.CommonEcs.EntityID;
import dk.sdu.se4.group1.CommonEcs.IInventoryService;
import dk.sdu.se4.group1.CommonEcs.IShopService;
import dk.sdu.se4.group1.CommonEcs.World;
import dk.sdu.se4.group1.Robot.RobotFactory;
import dk.sdu.se4.group1.Robot.RobotSystem;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import dk.sdu.se4.group1.Map.MappingSystem;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ServiceLoader;


public class Main extends Application {

    private long lastTime = 0;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage window) throws Exception {
        ServiceLoader<IInventoryService> invLoader = ServiceLoader.load(IInventoryService.class);
        invLoader.findFirst().ifPresent(inv -> {
            IInventoryService.setInstance(inv); // Gemmer instansen
        });

        ServiceLoader<IShopService> shpLoader = ServiceLoader.load(IShopService.class);
        shpLoader.findFirst().ifPresent(shp -> {
            IShopService.setInstance(shp);
        });
        World world = new World(); //creates world instance
        SystemRegistry registry = new SystemRegistry(); //Creates system registry instance

        Pane root = new Pane();
        Canvas canvas = new Canvas(800,600);



        Button shopBtn = new Button("Åbn Shop");
        shopBtn.setLayoutX(2);  // placering X
        shopBtn.setLayoutY(550);  // placering Y
        shopBtn.setPrefWidth(395);
        shopBtn.setPrefHeight(50);
        shopBtn.setOnAction(e -> {
            IShopService Shp = IShopService.getInstance();
            Shp.openShop();
        });


        Label coinLabel = new Label();
        coinLabel.setTextFill(Color.YELLOW);
        coinLabel.setLayoutX(560); // Skubber den 600 pixels mod højre
        coinLabel.setLayoutY(5);  // Skubber den 50 pixels ned fra toppen
        // Styling af "kassen"
        coinLabel.setStyle(
                "-fx-background-color: #0000FF; " +    // Blå baggrund
                        "-fx-border-color: white; " +         // Hvid ramme
                        "-fx-border-width: 2; " +             // Tykkelse på rammen
                        "-fx-padding: 10 20 10 20; " +        // Luft: Top, Højre, Bund, Venstre
                        "-fx-text-fill: yellow; " +           // Gul tekst
                        "-fx-font-size: 18px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 10; " +       // Runde hjørner på baggrunden
                        "-fx-border-radius: 10;"              // Runde hjørner på rammen
        );
        IInventoryService inventorye = IInventoryService.getInstance();
        coinLabel.setText("Coins : "+inventorye.getWallet());

        Button invBtn = new Button("Åbn Inventory");
        invBtn.setPrefWidth(395);
        invBtn.setPrefHeight(50);
        invBtn.setLayoutX(402.5); // lige til højre for shop knappen
        invBtn.setLayoutY(550);
        invBtn.setOnAction(e -> {

            IInventoryService inventory = IInventoryService.getInstance();
            inventory.showInvi();
            coinLabel.setText("Coins : "+inventory.getWallet());

        });
        root.getChildren().add(coinLabel);
        root.getChildren().add(canvas);
        root.getChildren().add(shopBtn);
        root.getChildren().add(invBtn);
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

        RobotFactory robotFactory = new RobotFactory();

        EntityID firstrobotid = robotFactory.createRobot(world);

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
        registry.register(new RobotSystem());
    }
}
