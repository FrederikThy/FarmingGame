package dk.sdu.se4.group1.Shop;

import dk.sdu.se4.group1.CommonApi.SeedType;
import dk.sdu.se4.group1.CommonEcs.*;
import dk.sdu.se4.group1.CommonEcs.Components.InventoryComponent;
import dk.sdu.se4.group1.CommonEcs.Components.RobotComponent;
import dk.sdu.se4.group1.CommonEcs.Components.SpeedToolComponent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.InputStream;
import java.util.List;

/**
 * Hello world!
 */
public class ShopPlugin extends Button implements IShopService,EcsSystem {



    public ShopPlugin(World world){
        this.setLayoutX(710);
        this.setLayoutY(180);
        this.setPrefWidth(230);
        this.setPrefHeight(150);
        this.setStyle("-fx-background-color: rgba(255, 255, 255, 0);");
        this.setOnAction(e -> {
            openShop(world);
        });
    }

    private EntityID findAvailableRobot(World world) {
        for (EntityID entity : world.getEntitiesWith(RobotComponent.class)) {
            RobotComponent robot =
                    (RobotComponent) world.GetComponent(entity, RobotComponent.class);


            if (robot.seedType == null) {
                return entity;
            }
        }
        return null;
    }
    @Override
    public void openShop(World world) {
        var entityInvitory = world.getEntitiesWith(InventoryComponent.class);
        InventoryComponent invitory = (InventoryComponent)world.GetComponent(entityInvitory.iterator().next(),InventoryComponent.class);

        VBox layout = new VBox(12);
        layout.getStyleClass().add("shop-root");

        Label title = new Label("Shop");
        title.getStyleClass().add("shop-title");

        Label walletLabel = new Label();
        updateWalletLabel(walletLabel, invitory);

        layout.getChildren().addAll(title, walletLabel);

        for (SeedType seedType : SeedType.values()) {
            Button card = createSeedCard(seedType, invitory, walletLabel, world);
            card.setLayoutX(100);
            layout.getChildren().add(card);
        }

        layout.getChildren().add(createSpeedToolCard(invitory, walletLabel, world));
        Scene scene = new Scene(layout, 360, 520);


        Stage shopStage = new Stage();
        shopStage.setScene(scene);
        shopStage.setTitle("Shop");
        shopStage.show();
    }

    private Button createSeedCard(SeedType seedType, InventoryComponent inventory, Label walletLabel, World world) {
        int price = getBuyPrice(seedType);

        ImageView seedImage = loadImage("/" + seedType.name().toLowerCase() + ".png", 52, 52);
        ImageView coinImage = loadImage("/coin.png", 24, 24);

        Label nameLabel = new Label(formatSeedName(seedType)+" Seed");
        Label priceLabel = new Label("pris: " + price);

        HBox priceRow = new HBox(12, priceLabel, coinImage);
        VBox textBox = new VBox(12, nameLabel, priceRow);
        HBox content = new HBox(12, seedImage, textBox);

        Button button = new Button();
        button.setStyle("-fx-background-color: #c8a96e; -fx-border-color: #7a5c2e; -fx-border-width: 3; -fx-padding: 10;");
        button.setGraphic(content);
        button.setMaxWidth(Double.MAX_VALUE);
        button.getStyleClass().add("shop-item");
        button.setOnAction(e -> handlePurchase(seedType, price, inventory, walletLabel, world));

        return button;
    }

    private Button createSpeedToolCard(InventoryComponent inventory, Label walletLabel, World world) {
        int price = 200;
        ImageView coinImage = loadImage("/coin.png", 24, 24);
        Label nameLabel = new Label("Speed Tool");
        Label priceLabel = new Label("pris: " + price);
        HBox priceRow = new HBox(12, priceLabel, coinImage);
        VBox textBox = new VBox(12, nameLabel, priceRow);

        Button button = new Button();
        button.setStyle("-fx-background-color: #c8a96e; -fx-border-color: #7a5c2e; -fx-border-width: 3; -fx-padding: 10;");
        button.setGraphic(textBox);
        button.setMaxWidth(Double.MAX_VALUE);
        button.getStyleClass().add("shop-item");

        button.setOnAction(e -> {
            if (inventory.getWallet() < price) return;

            Stage pickStage = new Stage();
            VBox pickLayout = new VBox(10);
            pickLayout.setPadding(new javafx.geometry.Insets(20));
            pickLayout.getChildren().add(new Label("Vælg en robot:"));

            for (EntityID entity : world.getEntitiesWith(RobotComponent.class)) {
                Button robotBtn = new Button("Robot " + entity.id());
                robotBtn.setOnAction(ev -> {
                    handleSpeedToolPurchase(entity, price, inventory, walletLabel, world);
                    pickStage.close();
                });
                pickLayout.getChildren().add(robotBtn);}
            pickStage.setScene(new Scene(pickLayout, 250, 300));
            pickStage.setTitle("Vælg Robot");
            pickStage.show();});
        return button;
    }

    private String formatSeedName(SeedType seedType) {
        String lower = seedType.name().toLowerCase();
        return lower.substring(0, 1).toUpperCase() + lower.substring(1);
    }

    //image path
    private ImageView loadImage(String path, double width, double height) {
        InputStream stream = getClass().getResourceAsStream(path);

        if (stream == null) {
            throw new IllegalArgumentException("Image not found: " + path);
        }

        ImageView imageView = new ImageView(new Image(stream));
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        imageView.setPreserveRatio(true);

        return imageView;
    }

    private void handlePurchase(SeedType seedType, int price, InventoryComponent inventory, Label walletLabel, World world) {
        if (inventory.getWallet() < price) {
            return;
        }

        EntityID robotId = findAvailableRobot(world);

        if (robotId == null) {
            inventory.addSeeds(seedType);
            System.out.println(seedType+" seed Kunne tilføjet til en robot. Så er blevet lagt i inventory");
        } else {
            RobotComponent robot = (RobotComponent) world.GetComponent(robotId, RobotComponent.class);
            robot.seedType = seedType;
        }

        inventory.removeFromWallet(price);
        updateWalletLabel(walletLabel, inventory);
    }

    private void handleSpeedToolPurchase(EntityID entity, int price, InventoryComponent inventory, Label walletLabel, World world) {
        world.addComponent(entity, new SpeedToolComponent(2.0));
        inventory.removeFromWallet(price);
        updateWalletLabel(walletLabel, inventory);
        System.out.println("Speed tool equipped on robot " + entity.id());
    }

    @Override
    public List<EntityID> getShopItems(World world) {
        return List.of();
    }

    private void updateWalletLabel(Label walletLabel, InventoryComponent inventory) {
        walletLabel.setText("Coins: " + inventory.getWallet());
    }


    @Override
    public boolean buyItem(int entityID, Item item, int quantity) {
        boolean resualt =true;

        return resualt;
    }

    @Override
    public int SellItem(int entityID, int quantity) {
        int coins =0;
        SeedType type = SeedType.BEANSPROUT;
        switch (type){

            case BEANSPROUT:
                coins = 110*quantity;
                break;
            case CHILI:
                coins = 120*quantity;
                break;
            case CARROT:
                coins = 150*quantity;
                break;
            case TOMATO:
                coins = 90*quantity;
                break;
        }
        System.out.println("Item Sold");
        return coins;
    }

    @Override
    public int getBuyPrice(SeedType seed) {
        int amount =0;
        switch (seed){

            case BEANSPROUT:
                amount = 100;
                break;
            case CHILI:
                amount = 50;
                break;
            case CARROT:
                amount = 70;
                break;
            case TOMATO:
                amount = 100;
                break;
        }
        return amount;
    }

    @Override
    public int getSellPrice(int entityID,int amount) {
        return SellItem(entityID,amount);
    }


    @Override
    public boolean isAvailable() {
        return false;
    }

    @Override
    public void update(World world, double deltaTime) {

    }
}
