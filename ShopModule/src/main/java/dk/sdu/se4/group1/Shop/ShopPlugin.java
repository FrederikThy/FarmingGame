package dk.sdu.se4.group1.Shop;

import dk.sdu.se4.group1.CommonApi.SeedType;
import dk.sdu.se4.group1.CommonEcs.*;
import dk.sdu.se4.group1.CommonEcs.Components.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.InputStream;
import java.util.List;
import java.util.ServiceLoader;

/**
 * Hello world!
 */
public class ShopPlugin extends Button implements IShopService, IEntityProcessingService {

    private VBox activeAllList;
    private VBox activeCropList;
    private VBox activeSpeedList;
    private VBox activeRobotList;

    private ShopIComponentService activeShop;
    private InventoryIComponentService activeInventory;
    private Label activeWallet;
    private Label activeSoilLevelLabel;
    private Button activeSoilUpgradeButton;
    private GrowthMapComponent activeGrowthMap;
    private double shopUpdateTimer = 0.0;
    private static final int SOIL_UPGRADE_PRICE = 300;

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
        for (EntityID entity : world.getEntitiesWith(RobotIComponentService.class)) {
            RobotIComponentService robot =
                    (RobotIComponentService) world.GetComponent(entity, RobotIComponentService.class);


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
        var entityInvitory = world.getEntitiesWith(InventoryIComponentService.class);
        InventoryIComponentService invitory = (InventoryIComponentService)world.GetComponent(entityInvitory.iterator().next(), InventoryIComponentService.class);
        var entityShop = world.getEntitiesWith(ShopIComponentService.class);
        ShopIComponentService shop = (ShopIComponentService)world.GetComponent(entityShop.iterator().next(), ShopIComponentService.class);
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

        Label soilLabel = null;
        Button soilUpgradeBtn = null;
        GrowthMapComponent growthMap = findGrowthMapComponent(world);
        if (growthMap != null) {
            soilLabel = new Label();
            soilLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #3f2d17;");

            soilUpgradeBtn = new Button();
            soilUpgradeBtn.setStyle("-fx-background-color: #c8a96e; -fx-border-color: #3f2d17; -fx-border-width: 3; -fx-padding: 10;");
            soilUpgradeBtn.setMaxWidth(Double.MAX_VALUE);
            soilUpgradeBtn.setPrefWidth(330);

            refreshSoilUpgradeUI(soilLabel, soilUpgradeBtn, growthMap, invitory, SOIL_UPGRADE_PRICE);

            Label finalSoilLabel = soilLabel;
            Button finalSoilUpgradeBtn = soilUpgradeBtn;
            soilUpgradeBtn.setOnAction(e -> {
                handleSoilUpgradePurchase(SOIL_UPGRADE_PRICE, invitory, walletLabel, world);
                refreshSoilUpgradeUI(finalSoilLabel, finalSoilUpgradeBtn, growthMap, invitory, SOIL_UPGRADE_PRICE);
                updateShopContent(allList,cropList,speedList,robotList, shop, invitory, walletLabel, world);
            });
        }

        updateShopContent(allList,cropList,speedList,robotList, shop, invitory, walletLabel, world);
        activeAllList = allList;
        activeCropList = cropList;
        activeSpeedList = speedList;
        activeRobotList = robotList;
        activeShop = shop;
        activeInventory = invitory;
        activeWallet = walletLabel;
        activeSoilLevelLabel = soilLabel;
        activeSoilUpgradeButton = soilUpgradeBtn;
        activeGrowthMap = growthMap;

        layout.getChildren().addAll(title, walletLabel, tabPane);
        if (soilLabel != null) {
            layout.getChildren().addAll(soilLabel, soilUpgradeBtn);
        }
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
            activeSoilLevelLabel = null;
            activeSoilUpgradeButton = null;
            activeGrowthMap = null;
            shopUpdateTimer = 0.0;
        });
    }

    private GrowthMapComponent findGrowthMapComponent(World world) {
        var growthMapEntities = world.getEntitiesWith(GrowthMapComponent.class);
        if (growthMapEntities != null && growthMapEntities.iterator().hasNext()) {
            EntityID growthMapEntity = growthMapEntities.iterator().next();
            return (GrowthMapComponent) world.GetComponent(growthMapEntity, GrowthMapComponent.class);
        }
        return null;
    }

    private void handleSoilUpgradePurchase(int price, InventoryComponent inventory, Label walletLabel, World world) {
        GrowthMapComponent growthMap = findGrowthMapComponent(world);
        if (growthMap == null || inventory.getWallet() < price) {
            return;
        }

        int nextLevel = growthMap.getUnlockedMapLevel() + 1;
        if (nextLevel > 2) {
            return;
        }

        if (growthMap.unlockMap(nextLevel)) {
            inventory.removeFromWallet(price);
            updateWalletLabel(walletLabel, inventory);
            System.out.println("Soil upgraded to level " + growthMap.getUnlockedMapLevel());
        }
    }
    private void updateShopContent(
            VBox allList,
            VBox cropList,
            VBox speedList,
            VBox robotList,
            ShopIComponentService shop,
            InventoryIComponentService inventory,
            Label walletLabel,
            World world
    ) {

        allList.getChildren().clear();
        cropList.getChildren().clear();
        speedList.getChildren().clear();
        robotList.getChildren().clear();

        for (ShopOfferIComponentService item : shop.getShopItems()) {
            IComponentService IComponentService = item.getComponent();

            allList.getChildren().add(createShopCard(item, inventory, walletLabel, world,allList,cropList, speedList, robotList, shop));
            if (IComponentService instanceof CropIComponentService) {
                cropList.getChildren().add(createShopCard(item, inventory, walletLabel, world,allList,cropList, speedList, robotList, shop));
            } else if (IComponentService instanceof SpeedToolIComponentService) {
                speedList.getChildren().add(createSpeedToolCard(item, inventory, walletLabel, world,allList,cropList, speedList, robotList, shop));
            } else if (IComponentService instanceof RobotIComponentService) {
                robotList.getChildren().add(createShopCard(item, inventory, walletLabel, world,allList,cropList, speedList, robotList, shop));
            } else if (IComponentService instanceof PathfindingAlgorithmIComponentService) {
                robotList.getChildren().add(createShopCard(item, inventory, walletLabel, world,allList,cropList, speedList, robotList, shop));
            }
        }
    }


    @Override
    public List<EntityID> getShopItems() {
        return List.of();
    }

    private Button createShopCard(ShopOfferIComponentService item, InventoryIComponentService inventory, Label walletLabel, World world, VBox allList , VBox cropList, VBox speedList, VBox robotList, ShopIComponentService shop)
    {
        int price = item.getBuyPrice();
        IComponentService IComponentService = item.getComponent();

        String name = getName(IComponentService);
        String imagePath = getImagePath(IComponentService);

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

        if (IComponentService instanceof PathfindingAlgorithmIComponentService algoComponent) {
            var upgradeEntities = world.getEntitiesWith(PathfindingUpgradeIComponentService.class);
            if (upgradeEntities != null && upgradeEntities.iterator().hasNext()) {
                PathfindingUpgradeIComponentService upgrade = (PathfindingUpgradeIComponentService)
                        world.GetComponent(upgradeEntities.iterator().next(), PathfindingUpgradeIComponentService.class);
                if (upgrade.activeTier.tier >= algoComponent.tier.tier) {
                    button.setDisable(true);
                    priceLabel.setText("Purchased");
                    ColorAdjust darken = new ColorAdjust();
                    darken.setBrightness(-0.5);
                    button.setEffect(darken);
                }
            }
        }

        // We dont have to check if its a robotComponent, because we do that in handlePurchase
        button.setOnAction(e -> {

            handlePurchase(IComponentService, price, inventory, walletLabel, null, world);

            updateWalletLabel(walletLabel, inventory);
            updateShopContent(allList,cropList, speedList, robotList, shop, inventory, walletLabel, world);
        });

        return button;
    }

    private Button createSpeedToolCard(ShopOfferIComponentService item, InventoryIComponentService inventory, Label walletLabel, World world, VBox allList , VBox cropList, VBox speedList, VBox robotList, ShopIComponentService shop) {
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

            for (EntityID entity : world.getEntitiesWith(RobotIComponentService.class)) {
                boolean alreadyEquipped = world.hasComponent(entity, SpeedToolIComponentService.class);

                String label;
                if (alreadyEquipped){
                   label = "Robot " + entity.id() + " (level " + (int)((SpeedToolIComponentService) world.GetComponent(entity, SpeedToolIComponentService.class)).getSpeedMultiplier() + ")";
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


    private void handleRobotSelect(ShopOfferIComponentService item, InventoryIComponentService inventory, Label walletLabel, World world, VBox allList, VBox cropList, VBox speedList, VBox robotList, ShopIComponentService shop){
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

    private String getImagePath(IComponentService IComponentService) {
        if (IComponentService instanceof CropIComponentService cropComponent) {
            return "/" + cropComponent.seedType.name().toLowerCase() + ".png";
        }

        if (IComponentService instanceof SpeedToolIComponentService) {
            return "/Speed_Tool.png";
        }

        if (IComponentService instanceof PlantingIComponentService) {
            return "/Planting_Tool.png";
        }

        if (IComponentService instanceof HarvestingIComponentService) {
            return "/Harvesting_Tool.png";
        }
        // Picture for each of the robots
        if(IComponentService instanceof RobotIComponentService robotComponent) {
            return switch (robotComponent.robotType){
                case HARVEST -> "/HrFlink_1.png";
                case PLANT -> "/HrFlink_2.png";
                case WEED_REMOVER ->  "/HrFlink_3.png";
            };
        }
        if (IComponentService instanceof PathfindingAlgorithmIComponentService) {
            return "/gear.png";
        }
        return "/item_slot.png";
    }



    private String getName(IComponentService IComponentService) {
        if (IComponentService instanceof CropIComponentService cropComponent) {
            return formatSeedName(cropComponent.seedType.toString()) + " Seed";
        }

        if (IComponentService instanceof SpeedToolIComponentService) {
            return "Speed Tool";
        }

        if (IComponentService instanceof PlantingIComponentService) {
            return "Planting Tool";
        }

        if (IComponentService instanceof HarvestingIComponentService) {
            return "Harvesting Tool";
        }
        // Instead of only one robot to pick, we have three
        if (IComponentService instanceof RobotIComponentService robotComponent) {
            return switch (robotComponent.robotType){
                case WEED_REMOVER ->  "Weed Remover";
                case HARVEST ->  "Harvest";
                case PLANT ->  "Planting";
            };
        }
        if (IComponentService instanceof PathfindingAlgorithmIComponentService algoComponent) {
            return switch (algoComponent.tier) {
                case DIJKSTRA -> "Dijkstra Pathfinding";
                case A_STAR   -> "A* Pathfinding";
                default       -> algoComponent.tier.displayName + " Pathfinding";
            };
        }

        return IComponentService.getClass().getSimpleName();
    }
    private int getPrice(ShopOfferIComponentService component) {
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



    private void handlePurchase(IComponentService type, int price, InventoryIComponentService inventory, Label walletLabel, String Robottype, World world) {
        if (inventory.getWallet() < price) {
            return;
        }


        if (type instanceof PathfindingAlgorithmIComponentService algoComponent) {
            var upgradeEntities = world.getEntitiesWith(PathfindingUpgradeIComponentService.class);
            if (upgradeEntities != null && upgradeEntities.iterator().hasNext()) {
                PathfindingUpgradeIComponentService upgrade = (PathfindingUpgradeIComponentService)
                        world.GetComponent(upgradeEntities.iterator().next(), PathfindingUpgradeIComponentService.class);
                if (upgrade.activeTier.tier >= algoComponent.tier.tier) {
                    return;
                }
                upgrade.activeTier = algoComponent.tier;
                System.out.println("[Shop] Pathfinding upgraded to: " + algoComponent.tier.displayName);
            }
            inventory.removeFromWallet(price);
            updateWalletLabel(walletLabel, inventory);
            return;
        }

        if (type instanceof CropIComponentService) {
            CropIComponentService seedComponent = new CropIComponentService(((CropIComponentService) type).seedType);
            seedComponent.isHarvestable = false;

            EntityID itemId = world.createEntity();
            inventory.addSeeds(seedComponent.seedType, 1);
        }

        if (type instanceof RobotIComponentService robotComponent) {
            RobotSPI robotCreator = ServiceLoader.load(RobotSPI.class).findFirst().orElseThrow( () -> new RuntimeException("Can't find createRobot"));

            robotCreator.createRobot(world, robotComponent.robotType, 9, 9, 1, 1);
        }

        if (type instanceof SpeedToolIComponentService) {
            //;
            //world.addComponent();
            /// skal adde moment speed tool så det virker ind

        }

        if (type instanceof SoilLevel){
            handleSoilUpgradePurchase(price, inventory, walletLabel, world);
            if (activeSoilLevelLabel != null && activeSoilUpgradeButton != null) {
                GrowthMapComponent growthMap = findGrowthMapComponent(world);
                if (growthMap != null) {
                    refreshSoilUpgradeUI(activeSoilLevelLabel, activeSoilUpgradeButton, growthMap, inventory, price);
                }
            }
            return;
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
     private void handleSpeedToolPurchase(EntityID entity, int price, InventoryIComponentService inventory, Label walletLabel, World world) {
        if (world.hasComponent(entity, SpeedToolIComponentService.class)) {
            SpeedToolIComponentService existing = (SpeedToolIComponentService) world.GetComponent(entity, SpeedToolIComponentService.class);
            world.addComponent(entity, new SpeedToolIComponentService(existing.getSpeedMultiplier() + 1.0));
        } else {
            world.addComponent(entity, new SpeedToolIComponentService(2.0));
        }
        inventory.removeFromWallet(price);
        updateWalletLabel(walletLabel, inventory);
        System.out.println("Speed tool upgraded on robot " + entity.id());
    }

    /*@Override
    public List<EntityID> getShopItems(World world) {
        return List.of();
    }*/

    private void updateWalletLabel(Label walletLabel, InventoryIComponentService inventory) {
        walletLabel.setText("Coins: " + inventory.getWallet());
    }

    private void refreshSoilUpgradeUI(Label soilLabel, Button soilUpgradeBtn, GrowthMapComponent growthMap,
                                      InventoryComponent inventory, int soilUpgradePrice) {
        int currentLevel = growthMap.getUnlockedMapLevel();
        boolean isMaxLevel = currentLevel >= 2;
        boolean canAfford = isAvailable(soilUpgradePrice, inventory.getWallet());

        soilLabel.setText("Soil Level: " + currentLevel);
        soilUpgradeBtn.setText(isMaxLevel
                ? "Soil Level Maxed"
                : "Upgrade Soil Level - " + soilUpgradePrice + " coins");

        boolean disableButton = isMaxLevel || !canAfford;
        soilUpgradeBtn.setDisable(disableButton);
        if (disableButton) {
            ColorAdjust darken = new ColorAdjust();
            darken.setBrightness(-0.5);
            soilUpgradeBtn.setEffect(darken);
        } else {
            soilUpgradeBtn.setEffect(null);
        }
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

        if (activeSoilLevelLabel != null && activeSoilUpgradeButton != null && activeGrowthMap != null) {
            refreshSoilUpgradeUI(activeSoilLevelLabel, activeSoilUpgradeButton, activeGrowthMap, activeInventory, SOIL_UPGRADE_PRICE);
        }
    }
}
