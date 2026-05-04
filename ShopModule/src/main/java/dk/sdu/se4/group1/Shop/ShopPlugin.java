package dk.sdu.se4.group1.Shop;

import dk.sdu.se4.group1.CommonApi.SeedType;
import dk.sdu.se4.group1.CommonEcs.*;
import dk.sdu.se4.group1.CommonEcs.Components.*;
import dk.sdu.se4.group1.Robot.RobotFactory;
import dk.sdu.se4.group1.CommonEcs.Components.InventoryComponent;
import dk.sdu.se4.group1.CommonEcs.Components.RobotComponent;
import dk.sdu.se4.group1.CommonEcs.Components.SpeedToolComponent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.InputStream;
import java.util.ArrayList;
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
        var entityShop = world.getEntitiesWith(ShopComponent.class);
        ShopComponent shop = (ShopComponent)world.GetComponent(entityShop.iterator().next(),ShopComponent.class);
        VBox layout = new VBox(12);
        layout.setStyle("-fx-padding: 16; -fx-background-color: #f1e0b8;");
        layout.getStyleClass().add("shop-root");

        Label title = new Label("Shop");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");
        title.getStyleClass().add("shop-title");

        Label walletLabel = new Label();
        walletLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        updateWalletLabel(walletLabel, invitory);

        VBox itemList = new VBox(12);
        itemList.setStyle("-fx-padding: 8; -fx-background-color: #7a5c2e;");

        for (ShopOfferComponent component : shop.getShopItems()) {
            Button card = createShopCard(component, invitory, walletLabel, world);
            itemList.getChildren().add(card);
        }

        Scene scene = new Scene(layout, 360, 420);


        Stage shopStage = new Stage();
        shopStage.setScene(scene);
        shopStage.setTitle("Shop");
        shopStage.show();
    }

    private Button createShopCard(ShopOfferComponent item, InventoryComponent inventory, Label walletLabel, World world) {
        int price = item.getBuyPrice();
        var component = item.getComponent();
        String name = getName(component);
        String imagePath = getImagePath(component);

        ImageView itemImage = loadImage(imagePath, 52, 52);
        ImageView coinImage = loadImage("/coin.png", 24, 24);

        Label nameLabel = new Label(name);
        Label priceLabel = new Label("pris: " + price);

        HBox priceRow = new HBox(12, priceLabel, coinImage);
        VBox textBox = new VBox(12, nameLabel, priceRow);
        HBox content = new HBox(12, itemImage, textBox);
        content.setStyle("-fx-alignment: center-left;");

        Button button = new Button();
        button.setStyle("-fx-background-color: #c8a96e; -fx-border-color: #3f2d17; -fx-border-width: 3; -fx-padding: 10;");
        button.setGraphic(content);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setPrefWidth(330);
        if (!isAvailable(price,inventory.getWallet())){
            button.setCancelButton(false);
            ColorAdjust darken = new ColorAdjust();
            darken.setBrightness(-0.5); // -1.0 = helt sort, 0.0 = normal

            button.setEffect(darken);
        }
        button.getStyleClass().add("shop-item");
        button.setOnAction(e -> handlePurchase(component, price, inventory, walletLabel, world));

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
                boolean alreadyEquipped = world.hasComponent(entity, SpeedToolComponent.class);

                String label;
                if (alreadyEquipped){
                   label = "Robot " + entity.id() + " (level " + (int)((SpeedToolComponent) world.GetComponent(entity, SpeedToolComponent.class)).getSpeedMultiplier() + ")";
                } else {
                    label = "Robot " + entity.id();
                }

                Button robotBtn = new Button(label);
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

    private String getImagePath(Component component) {
        if (component instanceof CropComponent cropComponent) {
            return "/" + cropComponent.seedType.name().toLowerCase() + ".png";
        }

        if (component instanceof SpeedToolComponent) {
            return "/Speed_Tool.png";
        }

        if (component instanceof PlantingComponent) {
            return "/Planting_Tool.png";
        }

        if (component instanceof HarvestingComponent) {
            return "/Harvesting_Tool.png";
        }
        if(component instanceof RobotComponent){
            return "/HrFlink.png";
        }
        return "/item_slot.png";
    }



    private String getName(Component component) {
        if (component instanceof CropComponent cropComponent) {
            return formatSeedName(cropComponent.seedType.toString()) + " Seed";
        }

        if (component instanceof SpeedToolComponent) {
            return "Speed Tool";
        }

        if (component instanceof PlantingComponent) {
            return "Planting Tool";
        }

        if (component instanceof HarvestingComponent) {
            return "Harvesting Tool";
        }
        if (component instanceof RobotComponent){
            return "Hr Flink";
        }

        return component.getClass().getSimpleName();
    }
    private int getPrice(ShopOfferComponent component) {
       return component.getBuyPrice();
    }

    private String formatSeedName(String seedType) {
        String lower = seedType.toLowerCase();
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



    /*private EntityID findSlowestRobot(World world) {
        List<EntityID> slowestRobots = new ArrayList<>();
        double slowestInterval = -1;

        for (EntityID entity : world.getEntitiesWith(RobotComponent.class)) {
            RobotComponent robot =
                    (RobotComponent) world.GetComponent(entity, RobotComponent.class);

            /*double interval = robot.getMoveInterval();

            if (interval > slowestInterval) {
                slowestInterval = interval;
                slowestRobots.clear();
                slowestRobots.add(entity);
            } else if (interval == slowestInterval) {
                slowestRobots.add(entity);
            }
        }

        if (slowestRobots.isEmpty()) {
            return null;
        }

        return slowestRobots.get(new Random().nextInt(slowestRobots.size()));
    }*/



    private void handlePurchase(Component type, int price, InventoryComponent inventory, Label walletLabel, World world) {
        if (inventory.getWallet() < price) {
            return;
        }


        if(type instanceof CropComponent){
            CropComponent seedComponent = new CropComponent(((CropComponent) type).seedType);
            seedComponent.isHarvestable = false;

            EntityID itemId = world.createEntity();
            inventory.addSeeds(seedComponent.seedType, 1);
        }

        if (type instanceof RobotComponent){
            EntityID id = new RobotFactory().BaseRobot(world,4,4,10,10);
            world.addComponent(id,type);
        }

        if(type instanceof SpeedToolComponent)
        {
            //;
            //world.addComponent();
            /// skal adde moment speed tool så det virker ind

        }
        /*if (seedType instanceof SpeedToolComponent speedToolComponent) {
            EntityID robotId = findSlowestRobot(world);

            if (robotId == null) {
                return;
            }

            RobotComponent robot =
                    (RobotComponent) world.GetComponent(robotId, RobotComponent.class);

            robot.setMoveInterval(speedToolComponent.getSpeedMultiplier());
        }*/

        //EntityID robotId = findAvailableRobot(world);

        /*if (robotId == null) {
            inventory.addComponentItem();
            System.out.println(seedType+" seed Kunne tilføjet til en robot. Så er blevet lagt i inventory");
        } else {
            RobotComponent robot = (RobotComponent) world.GetComponent(robotId, RobotComponent.class);
            robot.seedType = seedType;
        }

        inventory.removeFromWallet(price);
        updateWalletLabel(walletLabel, inventory);
    }*/

     private void handleSpeedToolPurchase(EntityID entity, int price, InventoryComponent inventory, Label walletLabel, World world) {
        if (world.hasComponent(entity, SpeedToolComponent.class)) {
            SpeedToolComponent existing = (SpeedToolComponent) world.GetComponent(entity, SpeedToolComponent.class);
            world.addComponent(entity, new SpeedToolComponent(existing.getSpeedMultiplier() + 1.0));
        } else {
            world.addComponent(entity, new SpeedToolComponent(2.0));
        }
        inventory.removeFromWallet(price);
        updateWalletLabel(walletLabel, inventory);
        System.out.println("Speed tool upgraded on robot " + entity.id());
    }

    /*@Override
    public List<EntityID> getShopItems(World world) {
        return List.of();
    }*/

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
                coins = 100*quantity;
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
    public int getSellPrice(SeedType seedType, int amount) {
        int coins = 0;
        switch (seedType) {
            case BEANSPROUT:
                coins = 200 * amount;
                break;
            case CHILI:
                coins = 100 * amount;
                break;
            case CARROT:
                coins = 150 * amount;
                break;
            case TOMATO:
                coins = 200 * amount;
                break;
        }
        return coins;
    }




    @Override
    public boolean isAvailable(int price, int wallet ) {
        boolean isAvalilable= false;

        if (price<=wallet){
            isAvalilable=true;
        }
        return isAvalilable;
    }

    @Override
    public void update(World world, double deltaTime) {

    }
}
