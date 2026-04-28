package dk.sdu.se4.group1.Inventory;

import dk.sdu.se4.group1.CommonEcs.*;
import dk.sdu.se4.group1.CommonEcs.Components.CropComponent;
import dk.sdu.se4.group1.CommonEcs.Components.InventoryComponent;
import dk.sdu.se4.group1.CommonEcs.Components.RobotComponent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.*;

/**
 * Hello world!
 */
public class InventoryPlugin extends Button implements IInventoryService, EcsSystem {

    World world;
    public InventoryPlugin(World world){
        this.world=world;
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
        InventoryComponent invitory = (InventoryComponent)world.GetComponent(entityInvitory.iterator().next(),InventoryComponent.class);

        Stage stage = new Stage();
        VBox layout = new VBox(10);

        layout.getChildren().add(new Label("Inventory"));

        layout.getChildren().add(new Label(""+invitory.getWallet()));

        for (var entry : invitory.getHarvestedCrops().entrySet()) {
            layout.getChildren().add(new Label(entry.getKey() + " x" + entry.getValue()));
        }
        layout.getChildren().add(new Label("Let Over seed"));
        for (var entry : invitory.getSeedStorage().entrySet()) {
            layout.getChildren().add(new Label(entry.getKey() + " x" + entry.getValue()));
        }

        layout.setPadding(new Insets(20));
        stage.setScene(new Scene(layout, 300, 400));
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


    }
    @Override
    public void RemoveCoins(int coins){

    }

    @Override
    public int getWallet() {
        return 0;
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



    @Override
    public void update(World world, double deltaTime) {

    }
}
