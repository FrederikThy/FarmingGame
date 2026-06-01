package dk.sdu.se4.group1.Shop;

import dk.sdu.se4.group1.CommonEcs.Components.*;
import dk.sdu.se4.group1.CommonEcs.EntityID;
import dk.sdu.se4.group1.CommonEcs.IComponentService;
import dk.sdu.se4.group1.CommonEcs.World;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.InputStream;

public class ShopView {

    private final ShopController controller;
    private final ShopItemMapper itemMapper;
    private final ShopPricingService pricingService;


    private VBox allList;
    private VBox cropList;
    private VBox speedList;
    private VBox robotList;
    private Label walletLabel;
    private Label soilLabel;
    private Button soilUpgradeButton;


    public ShopView(ShopController controller, ShopItemMapper itemMapper, ShopPricingService pricingService){
        this.controller = controller;
        this.itemMapper = itemMapper;
        this.pricingService = pricingService;
    }


    public void open(World world,ShopIComponentService shop,InventoryIComponentService inventory,GrowthMapIComponentService growthMap,int soilUpgradePrice)
    {
        VBox layout = new VBox(12);
        layout.setStyle("-fx-padding: 16; -fx-background-color: #f1e0b8;");

        Label title = new Label("Shop");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        walletLabel = new Label();
        walletLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        allList = createTabList();
        cropList = createTabList();
        speedList = createTabList();
        robotList = createTabList();

        TabPane tabPane = new TabPane(
                createTab("All", allList),
                createTab("Crop", cropList),
                createTab("Tools", speedList),
                createTab("Robot", robotList)
        );

        layout.getChildren().addAll(title, walletLabel, tabPane);

        if (growthMap != null) {
            soilLabel = new Label();
            soilUpgradeButton = new Button();

            soilUpgradeButton.setMaxWidth(Double.MAX_VALUE);
            soilUpgradeButton.setPrefWidth(330);
            soilUpgradeButton.setStyle("-fx-background-color: #c8a96e; -fx-border-color: #3f2d17; -fx-border-width: 3; -fx-padding: 10;");
            soilUpgradeButton.setOnAction(e -> controller.buySoilUpdgrade(world));

            layout.getChildren().addAll(soilLabel, soilUpgradeButton);
        }

        refresh(world, shop, inventory, growthMap, soilUpgradePrice);

        Stage stage = new Stage();
        stage.setScene(new Scene(layout, 360, 420));
        stage.setTitle("Shop");
        stage.setOnHidden(e -> controller.closeShop());
        stage.show();
    }

    public void refresh(World world,ShopIComponentService shop,InventoryIComponentService inventory,GrowthMapIComponentService growthMap,int soilUpgradePrice)
    {
        walletLabel.setText("Coins: " + inventory.getWallet());

        allList.getChildren().clear();
        cropList.getChildren().clear();
        speedList.getChildren().clear();
        robotList.getChildren().clear();

        for (ShopOfferIComponentService offer : shop.getShopItems()) {
            Button card = createShopCard(world, offer, inventory);
            allList.getChildren().add(card);

            ShopCategory category = itemMapper.getCategory(offer.getComponent());

            if (category == ShopCategory.CROP) {
                cropList.getChildren().add(createShopCard(world, offer, inventory));
            } else if (category == ShopCategory.Tool) {
                speedList.getChildren().add(createShopCard(world, offer, inventory));
            } else if (category == ShopCategory.ROBOT) {
                robotList.getChildren().add(createShopCard(world, offer, inventory));
            }
        }

        if (growthMap != null && soilLabel != null && soilUpgradeButton != null) {
            refreshSoilUpgradeUI(growthMap, inventory, soilUpgradePrice);
        }
    }

    private Button createShopCard(World world,ShopOfferIComponentService offer,InventoryIComponentService inventory)
    {
        IComponentService component = offer.getComponent();
        int price = offer.getBuyPrice();

        ImageView itemImage = loadImage(itemMapper.getImagePath(component), 52, 52);
        ImageView coinImage = loadImage("/coin.png", 24, 24);

        Label nameLabel = new Label(itemMapper.getName(component));
        Label priceLabel = new Label("pris: " + price);

        HBox priceRow = new HBox(12, priceLabel, coinImage);
        VBox textBox = new VBox(12, nameLabel, priceRow);
        HBox content = new HBox(12, itemImage, textBox);

        Button button = new Button();
        button.setGraphic(content);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setPrefWidth(330);
        button.setStyle("-fx-background-color: #c8a96e; -fx-border-color: #3f2d17; -fx-border-width: 3; -fx-padding: 10;");

        if (!pricingService.canAfford(price, inventory.getWallet())) {
            disableButton(button);
        }
        if (component instanceof SpeedToolIComponentService) {
            button.setOnAction(e -> showSpeedToolRobotPicker(world, offer));
        } else if (component instanceof HarvestingIComponentService) {
            button.setOnAction(e -> showHarvestingToolRobotPicker(world, offer));
        } else if (component instanceof PlantingIComponentService) {
            button.setOnAction(e -> showPlantingToolRobotPicker(world, offer));
        } else {
            button.setOnAction(e -> controller.buyOffer(world, offer));
        }

        return button;
    }
    private void showSpeedToolRobotPicker(World world, ShopOfferIComponentService offer) {
        Stage stage = new Stage();
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 16; -fx-background-color: #f1e0b8;");
        layout.getChildren().add(new Label("Choose a robot to upgrade:"));

        var robots = world.getEntitiesWith(RobotIComponentService.class);

        if (robots == null || !robots.iterator().hasNext()) {
            layout.getChildren().add(new Label("No robots available"));
        } else {
            int harvestCount = 1;
            int plantCount = 1;
            int weedCount = 1;

            for (EntityID robotEntity : robots) {
                RobotIComponentService robot = (RobotIComponentService) world.GetComponent(robotEntity, RobotIComponentService.class);

                String robotName = switch (robot.robotType) {
                    case HARVEST -> "Harvest Robot " + harvestCount++;
                    case PLANT -> "Planting Robot " + plantCount++;
                    case WEED_REMOVER -> "Weed Remover Robot " + weedCount++;
                };

                if (world.hasComponent(robotEntity, SpeedToolIComponentService.class)) {
                    SpeedToolIComponentService speed =
                            (SpeedToolIComponentService) world.GetComponent(robotEntity, SpeedToolIComponentService.class);

                    robotName += " - speed " + speed.getSpeedMultiplier();
                }

                Button speedButton = new Button(robotName);
                speedButton.setPrefWidth(220);
                speedButton.setStyle("-fx-background-color: #c8a96e; -fx-border-color: #3f2d17; -fx-border-width: 3; -fx-padding: 10;");


                speedButton.setOnAction(e -> {
                    controller.buyToolForRobot(world, offer, robotEntity);
                    stage.close();
                });
                layout.getChildren().add(speedButton);
            }
        }

        stage.setScene(new Scene(layout, 280, 320));
        stage.setTitle("Upgrade Speed Tool");
        stage.show();
    }
    private void showHarvestingToolRobotPicker(World world, ShopOfferIComponentService offer) {
        Stage stage = new Stage();
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 16; -fx-background-color: #f1e0b8;");
        layout.getChildren().add(new Label("Choose a robot to equip Harvesting Tool:"));
           int harvestCount = 1;
        for (EntityID entity : world.getEntitiesWith(RobotIComponentService.class)) {
            RobotIComponentService robot = (RobotIComponentService) world.GetComponent(entity, RobotIComponentService.class);
            if (robot.robotType != RobotType.HARVEST) continue;

            HarvestingIComponentService existing = world.hasComponent(entity, HarvestingIComponentService.class) ? (HarvestingIComponentService) world.GetComponent(entity, HarvestingIComponentService.class) : null;
            String label = "Harvest Robot " + harvestCount++ + (existing != null ? " (level " + (int)(existing.getGrowthMultiplier()) + ")" : "");

            Button harvestButton = new Button(label);
            harvestButton.setPrefWidth(220);
            harvestButton.setStyle("-fx-background-color: #c8a96e; -fx-border-color: #3f2d17; -fx-border-width: 3; -fx-padding: 10;");
            harvestButton.setOnAction(e -> {controller.buyToolForRobot(world, offer, entity);
                stage.close();
            });
            layout.getChildren().add(harvestButton);
        }
        stage.setScene(new Scene(layout, 280, 320));
        stage.setTitle("Upgrade Harvesting Tool");
        stage.show();
    }

    private void showPlantingToolRobotPicker(World world, ShopOfferIComponentService offer) {
        Stage stage = new Stage();
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 16; -fx-background-color: #f1e0b8;");
        layout.getChildren().add(new Label("Choose a robot to equip Planting Tool:"));
            int plantCount = 1;
        for (EntityID entity : world.getEntitiesWith(RobotIComponentService.class)) {
            RobotIComponentService robot = (RobotIComponentService) world.GetComponent(entity, RobotIComponentService.class);
            if (robot.robotType != RobotType.PLANT) continue;

            PlantingIComponentService existing = world.hasComponent(entity, PlantingIComponentService.class) ? (PlantingIComponentService) world.GetComponent(entity, PlantingIComponentService.class) : null;
            String label = "Planting Robot " + plantCount++ + (existing != null ? " (level " + (int)(existing.getPlantingSpeedMultiplier()) + ")" : "");

            Button plantingButton = new Button(label);
            plantingButton.setPrefWidth(220);
            plantingButton.setPrefWidth(220);
            plantingButton.setStyle("-fx-background-color: #c8a96e; -fx-border-color: #3f2d17; -fx-border-width: 3; -fx-padding: 10;");
            plantingButton.setOnAction(e -> {
                controller.buyToolForRobot(world, offer, entity);
                stage.close();
            });
            layout.getChildren().add(plantingButton);
        }
        stage.setScene(new Scene(layout, 280, 320));
        stage.setTitle("Upgrade Planting Tool");
        stage.show();
    }

    private void refreshSoilUpgradeUI(GrowthMapIComponentService growthMap,InventoryIComponentService inventory,int price)
    {
        int currentLevel = growthMap.getUnlockedMapLevel();
        boolean maxLevel = currentLevel >= 2;
        boolean canAfford = pricingService.canAfford(price, inventory.getWallet());

        soilLabel.setText("Soil Level: " + currentLevel);
        soilUpgradeButton.setText(maxLevel
                ? "Soil Level Maxed"
                : "Upgrade Soil Level - " + price + " coins");

        if (maxLevel || !canAfford) {
            disableButton(soilUpgradeButton);
        } else {
            soilUpgradeButton.setDisable(false);
            soilUpgradeButton.setEffect(null);
        }
    }


    private Tab createTab(String title, VBox list)
    {
        Tab tab = new Tab(title, createScrollPane(list));
        tab.setClosable(false);
        return tab;
    }

    private VBox createTabList()
    {
        VBox list = new VBox(12);
        list.setStyle("-fx-padding: 8; -fx-background-color: #7a5c2e;");
        return list;
    }

    private ScrollPane createScrollPane(VBox content)
    {
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background: #7a5c2e;");
        return scrollPane;
    }

    private void disableButton(Button button)
    {
        button.setDisable(true);

        ColorAdjust darken = new ColorAdjust();
        darken.setBrightness(-0.5);
        button.setEffect(darken);
    }

    private ImageView loadImage(String path, double width, double height)
    {
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


}
