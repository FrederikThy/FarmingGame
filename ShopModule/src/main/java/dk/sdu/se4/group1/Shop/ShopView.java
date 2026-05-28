package dk.sdu.se4.group1.Shop;

import dk.sdu.se4.group1.CommonEcs.Components.GrowthMapIComponentService;
import dk.sdu.se4.group1.CommonEcs.Components.InventoryIComponentService;
import dk.sdu.se4.group1.CommonEcs.Components.ShopIComponentService;
import dk.sdu.se4.group1.CommonEcs.Components.ShopOfferIComponentService;
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
                createTab("Speed", speedList),
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

        button.setOnAction(e -> controller.buyOffer(world, offer));

        return button;
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
