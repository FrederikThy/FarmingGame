package dk.sdu.se4.group1.Inventory;

import dk.sdu.se4.group1.CommonEcs.*;
import dk.sdu.se4.group1.CommonEcs.Components.CropComponent;
import dk.sdu.se4.group1.CommonEcs.Components.InventoryComponent;
import dk.sdu.se4.group1.CommonEcs.Components.RobotComponent;
import dk.sdu.se4.group1.CommonApi.SeedType;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.*;

/**
 * Hello world!
 */
public class InventoryPlugin extends Button implements IInventoryService, EcsSystem {

    World world;
    private final IShopService shopService;

    public InventoryPlugin(World world){
        this(world, null);
    }

    public InventoryPlugin(World world, IShopService shopService){
        this.world=world;
        this.shopService = shopService;
        this.setLayoutX(710);
        this.setLayoutY(521);
        this.setPrefWidth(230);
        this.setPrefHeight(150);
        this.setStyle("-fx-background-color: rgba(255, 255, 255, 0);");
        this.setOnAction(e -> {
            showInventory(world);
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

    public void showInventory(World world) {

        var entityInvitory = world.getEntitiesWith(InventoryComponent.class);
        InventoryComponent invitory = (InventoryComponent) world.GetComponent(entityInvitory.iterator().next(), InventoryComponent.class);

        Stage stage = new Stage();

        VBox layout = new VBox(12);
        layout.setStyle("-fx-padding: 16; -fx-background-color: #f1e0b8;");

        // Titel
        Label title = new Label("Inventory");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        // Wallet
        Label walletLabel = new Label("Coins: " + invitory.getWallet());
        walletLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        // Seeds sektion
        Label seedTitle = new Label("Leftover Seeds");
        seedTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: black;");

        VBox seedList = new VBox(8);
        seedList.setStyle("-fx-padding: 8; -fx-background-color: #7a5c2e;");

        if (invitory.getSeedStorage().entrySet().isEmpty()) {
            Label empty = new Label("No Seeds Leftover");
            empty.setStyle("-fx-text-fill: black;");
            seedList.getChildren().add(empty);
        } else {
            for (var entry : invitory.getSeedStorage().entrySet()) {
                Label seedLabel = new Label(entry.getKey() + " x " + entry.getValue() + " Seeds");
                seedLabel.setStyle(
                        "-fx-background-color: #c8a96e;" +
                                "-fx-border-color: #3f2d17;" +
                                "-fx-border-width: 3;" +
                                "-fx-padding: 10;" +
                                "-fx-font-size: 13px;"
                );
                seedLabel.setMaxWidth(Double.MAX_VALUE);
                seedList.getChildren().add(seedLabel);
            }
        }

        // Harvest sektion
        Label harvestTitle = new Label("Harvested Plants");
        harvestTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: black;");

        VBox harvestList = new VBox(8);
        harvestList.setStyle("-fx-padding: 8; -fx-background-color: #7a5c2e;");

        if (invitory.getharvestedCrops().entrySet().isEmpty()) {
            Label empty = new Label("Ingen Item Harvested");
            empty.setStyle("-fx-text-fill: black;");
            harvestList.getChildren().add(empty);
        } else {
            for (var entry : invitory.getharvestedCrops().entrySet()) {
                var key = entry.getKey();
                var value = entry.getValue();

                Label cropLabel = new Label(key + " x " + value + " Harvested");
                cropLabel.setStyle("-fx-font-size: 13px;");

                Button sellButton = new Button("Sell");
                sellButton.setStyle(
                        "-fx-background-color: #5a8a3c;" +
                                "-fx-text-fill: white;" +
                                "-fx-border-color: #3f2d17;" +
                                "-fx-border-width: 2;" +
                                "-fx-padding: 5 12;" +
                                "-fx-font-weight: bold;" +
                                "-fx-cursor: hand;"
                );

                sellButton.setOnAction(e -> {
                    boolean sold = invitory.removeHarvest(key, value);
                    if (sold) {
                        invitory.addToWallet(getSellPrice(key, value));
                        stage.close();
                        showInventory(world);
                    }
                });

                HBox row = new HBox(10, cropLabel, sellButton);
                row.setStyle(
                        "-fx-background-color: #c8a96e;" +
                                "-fx-border-color: #3f2d17;" +
                                "-fx-border-width: 3;" +
                                "-fx-padding: 10;" +
                                "-fx-alignment: center-left;"
                );
                row.setMaxWidth(Double.MAX_VALUE);
                harvestList.getChildren().add(row);
            }
        }

        ScrollPane scrollPane = new ScrollPane();
        VBox content = new VBox(12, seedTitle, seedList, harvestTitle, harvestList);
        scrollPane.setContent(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #f1e0b8;");

        layout.getChildren().addAll(title, walletLabel, scrollPane);

        stage.setScene(new Scene(layout, 360, 420));
        stage.setTitle("Inventory");
        stage.show();
    }

    @Override
    public boolean additem(EntityID entityID) {
       boolean result = true;
        var entityInvitory = world.getEntitiesWith(InventoryComponent.class);
        InventoryComponent invitory = (InventoryComponent)world.GetComponent(entityInvitory.iterator().next(),InventoryComponent.class);
        world.getEntitiesWith(CropComponent.class).stream();


        //invitory.addHarvest();
        return result;
    }

    @Override
    public boolean additem(EntityID entityID, int quantity) {
        boolean result = true;
        /*try {
            for (InviItme invItem : invitory) {
                if (invItem.getItem().equals(item)) {
                    invItem.addCount(quantity);
                }
            }
            invitory.add(new InviItme(item, quantity)); // Bruger den nye constructor
        } catch (Exception e) {
            System.console().printf(e.getMessage());
            result = false;
        }*/
         return result;

    }
    private InventoryComponent getInventoryComponent() {
        var entityInvitory = world.getEntitiesWith(InventoryComponent.class);
        return (InventoryComponent) world.GetComponent(
                entityInvitory.iterator().next(),
                InventoryComponent.class
        );
    }



    @Override
    public void removeItem(Item item) {
        /*invitory.removeIf(invItem -> invItem.getItem().equals(item));*/
    }

    @Override
    public List<InviItme> getItems() {
        return null;
    }
    @Override
    public boolean hasItem(Item item) {
        return false;
    }

    @Override
    public void AddCoins(int coins){

        var entityInvitory = world.getEntitiesWith(InventoryComponent.class);
        InventoryComponent invitory = (InventoryComponent)world.GetComponent(entityInvitory.iterator().next(),InventoryComponent.class);
        invitory.addToWallet(coins);

    }

    @Override
    public void RemoveCoins(int coins){
        var entityInvitory = world.getEntitiesWith(InventoryComponent.class);
        InventoryComponent invitory = (InventoryComponent)world.GetComponent(entityInvitory.iterator().next(),InventoryComponent.class);
        invitory.removeFromWallet(coins);
    }

    @Override
    public int getWallet() {
        var entityInvitory = world.getEntitiesWith(InventoryComponent.class);
        InventoryComponent invitory = (InventoryComponent)world.GetComponent(entityInvitory.iterator().next(),InventoryComponent.class);
        return  invitory.getWallet();
    }

    @Override
    public void AddHarvest( EntityID EntityID, Item crop, int quantity) {
       /* List<InviItme> harvest = Harvest.computeIfAbsent(EntityID, k -> new ArrayList<>());

        for (InviItme invItem : harvest) {
            if (invItem.getItem().equals(crop)) {
                invItem.addCount(quantity);
                return;
            }
        }
        harvest.add(new InviItme(crop, quantity));*/
    }

    //private final Map<EntityID, List<InviItme>> Harvest = new HashMap<>();

    @Override
    public List<InviItme> getHarvest() {
        return null;
        /*return Harvest.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());*/
    }

    @Override
    public void sellAllHarvest(EntityID entityID) {
        /*List<InviItme> harvest = Harvest.getOrDefault(entityID, new ArrayList<>());

        for (InviItme invItem : harvest) {
            var shop =IShopService.getInstance();
            int coins = shop.SellItem(1,invItem.getItem(),invItem.getCount());
            //int sellPrice = invItem.getItem().getPrice() * invItem.getCount();
            AddCoins(coins);
        }

        Harvest.remove(entityID);*/
    }

    private int getSellPrice(SeedType seedType, int amount) {
        if (shopService == null) {
            return 0;
        }
        return shopService.getSellPrice(seedType, amount);
    }



    @Override
    public void update(World world, double deltaTime) {

    }
}
