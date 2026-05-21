package dk.sdu.se4.group1.Shop;

import dk.sdu.se4.group1.CommonApi.SeedType;
import dk.sdu.se4.group1.CommonEcs.*;
import dk.sdu.se4.group1.CommonEcs.Components.*;
import dk.sdu.se4.group1.Robot.RobotFactory;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Hello world!
 */
public class ShopPlugin extends Button implements IShopService, EcsSystem {

    private VBox activeAllList;
    private VBox activeCropList;
    private VBox activeSpeedList;
    private VBox activeRobotList;

    private ShopComponent activeShop;
    private InventoryComponent activeInventory;
    private Label activeWallet;
    private double shopUpdateTimer = 0.0;

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
    private VBox createTabList() {
        VBox list = new VBox(12);
        list.setStyle("-fx-padding: 8; -fx-background-color: #7a5c2e;");
        return list;
    }


    private ScrollPane createScrollPane(VBox content) {
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background: #7a5c2e;");
        return scrollPane;
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
        VBox allList = createTabList();
        VBox cropList = createTabList();
        VBox speedList = createTabList();
        VBox robotList = createTabList();

        Tab allTab = new Tab("All", createScrollPane(allList));
        allTab.setClosable(false);
        Tab cropTab = new Tab("Crop", createScrollPane(cropList));
        cropTab.setClosable(false);

        Tab speedTab = new Tab("Speed", createScrollPane(speedList));
        speedTab.setClosable(false);

        Tab robotTab = new Tab("Robot", createScrollPane(robotList));
        robotTab.setClosable(false);
        TabPane tabPane = new TabPane();
        tabPane.getTabs().addAll(allTab,cropTab,speedTab,robotTab);
        updateShopContent(allList,cropList,speedList,robotList, shop, invitory, walletLabel, world);
        activeAllList = allList;
        activeCropList = cropList;
        activeSpeedList = speedList;
        activeRobotList = robotList;
        activeShop = shop;
        activeInventory = invitory;
        activeWallet = walletLabel;
        layout.getChildren().addAll(title, walletLabel, tabPane);
        Scene scene = new Scene(layout, 360, 420);


        Stage shopStage = new Stage();
        shopStage.setScene(scene);
        shopStage.setTitle("Shop");

        shopStage.show();
        shopStage.setOnHidden(e->{
            activeAllList = null;
            activeCropList = null;
            activeSpeedList = null;
            activeRobotList = null;
            activeShop = null;
            activeInventory = null;
            activeWallet = null;
            shopUpdateTimer = 0.0;
        });
    }
    private void updateShopContent(
            VBox allList,
            VBox cropList,
            VBox speedList,
            VBox robotList,
            ShopComponent shop,
            InventoryComponent inventory,
            Label walletLabel,
            World world
    ) {

        allList.getChildren().clear();
        cropList.getChildren().clear();
        speedList.getChildren().clear();
        robotList.getChildren().clear();

        for (ShopOfferComponent item : shop.getShopItems()) {
            Component component = item.getComponent();

            allList.getChildren().add(createShopCard(item, inventory, walletLabel, world,allList,cropList, speedList, robotList, shop));
            if (component instanceof CropComponent) {
                cropList.getChildren().add(createShopCard(item, inventory, walletLabel, world,allList,cropList, speedList, robotList, shop));
            } else if (component instanceof SpeedToolComponent) {
                speedList.getChildren().add(createSpeedToolCard(item, inventory, walletLabel, world,allList,cropList, speedList, robotList, shop));
            } else if (component instanceof RobotComponent) {
                robotList.getChildren().add(createShopCard(item, inventory, walletLabel, world,allList,cropList, speedList, robotList, shop));
            } else if (component instanceof PathfindingAlgorithmComponent) {
                robotList.getChildren().add(createShopCard(item, inventory, walletLabel, world,allList,cropList, speedList, robotList, shop));
            }
        }
    }


    @Override
    public List<EntityID> getShopItems(World world) {
        return List.of();
    }

    private Button createShopCard(ShopOfferComponent item,InventoryComponent inventory,Label walletLabel,World world,VBox allList ,VBox cropList,VBox speedList,VBox robotList,ShopComponent shop)
    {
        int price = item.getBuyPrice();
        Component component = item.getComponent();

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

        if (!isAvailable(price, inventory.getWallet())) {
            button.setDisable(true);

            ColorAdjust darken = new ColorAdjust();
            darken.setBrightness(-0.5);
            button.setEffect(darken);
        }

        button.setOnAction(e -> {
        if(component instanceof RobotComponent){

            handleRobotSelect(item, inventory, walletLabel, world,allList,cropList,speedList,robotList,shop);
        }
        else{
            handlePurchase(component, price, inventory, walletLabel, null, world);
        }

            updateWalletLabel(walletLabel, inventory);
            updateShopContent(allList,cropList, speedList, robotList, shop, inventory, walletLabel, world);
        });

        return button;
    }

    private Button createSpeedToolCard(ShopOfferComponent item, InventoryComponent inventory, Label walletLabel, World world,VBox allList ,VBox cropList,VBox speedList,VBox robotList,ShopComponent shop) {
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

        button.setOnAction(e -> {
            if (inventory.getWallet() < price) return;

            Stage pickStage = new Stage();
            VBox pickLayout = new VBox(10);

            pickLayout.setStyle("-fx-padding: 16; -fx-background-color: #f1e0b8;");
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
                    updateShopContent(allList , cropList, speedList, robotList, shop, inventory, walletLabel, world);
                    pickStage.close();
                });
                pickLayout.getChildren().add(robotBtn);
            }
            pickStage.setScene(new Scene(pickLayout, 250, 300));
            pickStage.setTitle("Vælg Robot");
            pickStage.show();});

        return button;
    }


    private void handleRobotSelect(ShopOfferComponent item, InventoryComponent inventory, Label walletLabel, World world,VBox allList, VBox cropList,VBox speedList,VBox robotList,ShopComponent shop){
        Stage pickStage = new Stage();
        pickStage.setTitle("Vælg Robot");

        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-background-color: #f1e0b8;");

        Label title = new Label("Vælg en robot:");
        title.setStyle("-fx-font-size: 13px;");
        layout.getChildren().add(title);

        String[] robotTypes = {"HarvestingRobot", "PlantingRobot", "RemoveWeedRobot"};

        for (String robotType : robotTypes) {
            Button robotBtn = new Button(robotType);
            robotBtn.setStyle(
                    "-fx-background-color: white;" +
                            "-fx-border-color: #5b9bd5;" +
                            "-fx-border-width: 2;" +
                            "-fx-padding: 5 15;" +
                            "-fx-font-size: 13px;" +
                            "-fx-cursor: hand;"
            );
            robotBtn.setPrefWidth(180);

            robotBtn.setOnMouseEntered(e -> robotBtn.setStyle(
                    "-fx-background-color: #cce4f7;" +
                            "-fx-border-color: #5b9bd5;" +
                            "-fx-border-width: 2;" +
                            "-fx-padding: 5 15;" +
                            "-fx-font-size: 13px;" +
                            "-fx-cursor: hand;"
            ));
            robotBtn.setOnMouseExited(e -> robotBtn.setStyle(
                    "-fx-background-color: white;" +
                            "-fx-border-color: #5b9bd5;" +
                            "-fx-border-width: 2;" +
                            "-fx-padding: 5 15;" +
                            "-fx-font-size: 13px;" +
                            "-fx-cursor: hand;"
            ));

            robotBtn.setOnAction(e -> {
                handlePurchase(item.getComponent(), item.getBuyPrice(), inventory, walletLabel, robotType, world);
                updateWalletLabel(walletLabel,inventory);
                updateShopContent(allList, cropList, speedList, robotList, shop, inventory, walletLabel, world);
                pickStage.close();
            });

            layout.getChildren().add(robotBtn);
        }

        pickStage.setScene(new Scene(layout, 250, 300));
        pickStage.show();
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
        if (component instanceof PathfindingAlgorithmComponent) {
            return "/gear.png";
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
        if (component instanceof PathfindingAlgorithmComponent algoComponent) {
            return switch (algoComponent.tier) {
                case DIJKSTRA -> "Dijkstra Pathfinding";
                case A_STAR   -> "A* Pathfinding";
                default       -> algoComponent.tier.displayName + " Pathfinding";
            };
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

    private void handleHrFlinkBuy(){

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



    private void handlePurchase(Component type, int price, InventoryComponent inventory, Label walletLabel,String Robottype, World world) {
        if (inventory.getWallet() < price) {
            return;
        }


        if (type instanceof PathfindingAlgorithmComponent algoComponent) {
            var upgradeEntities = world.getEntitiesWith(PathfindingUpgradeComponent.class);
            if (upgradeEntities != null && upgradeEntities.iterator().hasNext()) {
                PathfindingUpgradeComponent upgrade = (PathfindingUpgradeComponent)
                        world.GetComponent(upgradeEntities.iterator().next(), PathfindingUpgradeComponent.class);
                if (upgrade.activeTier.tier < algoComponent.tier.tier) {
                    upgrade.activeTier = algoComponent.tier;
                    System.out.println("[Shop] Pathfinding upgraded to: " + algoComponent.tier.displayName);
                }
            }
        }

        if (type instanceof CropComponent) {
            CropComponent seedComponent = new CropComponent(((CropComponent) type).seedType);
            seedComponent.isHarvestable = false;

            EntityID itemId = world.createEntity();
            inventory.addSeeds(seedComponent.seedType, 1);
        }

        if (type instanceof RobotComponent) {
            EntityID id =null;
            switch (Robottype)
            {
                case "HarvestingRobot":

                    id = new RobotFactory().HarvestingRobot(world, 9, 9, 1, 1);
                    break;
                case "PlantingRobot":
                    id = new RobotFactory().PlantingRobot(world, 9, 9, 1, 1);
                    break;
                case "RemoveWeedRobot":
                    id = new RobotFactory().RemoveWeedRobot(world, 9, 9, 1, 1);
                    break;
            }
        }

        if (type instanceof SpeedToolComponent) {
            //;
            //world.addComponent();
            /// skal adde moment speed tool så det virker ind

        }

        if (type instanceof SoilLevel){

        }
        inventory.removeFromWallet(price);
        updateWalletLabel(walletLabel, inventory);
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
    }
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
        boolean isAvalilable = false;

        if (price <= wallet) {
            isAvalilable = true;
        }
        return isAvalilable;
    }

    @Override
    public void update(World world, double deltaTime) {
        if (activeShop == null || activeInventory == null || activeWallet == null){
            return;
        }

        shopUpdateTimer += deltaTime;
        if (shopUpdateTimer < 1.1) {
            return;
        }
        shopUpdateTimer = 0.0;
        updateWalletLabel(activeWallet,activeInventory);

        updateShopContent(
                activeAllList,
                activeCropList,
                activeSpeedList,
                activeRobotList,
                activeShop,
                activeInventory,
                activeWallet,
                world
        );
    }
}
