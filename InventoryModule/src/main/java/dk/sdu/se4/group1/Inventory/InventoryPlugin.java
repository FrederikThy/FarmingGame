package dk.sdu.se4.group1.Inventory;

import dk.sdu.se4.group1.CommonEcs.*;
import dk.sdu.se4.group1.CommonEcs.Components.InventoryComponent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.*;

/**
 * Hello world!
 */
public class InventoryPlugin implements IInventoryService, EcsSystem {


    public void showInventory(World world, EntityID inventoryId) {
        InventoryComponent inventory =(InventoryComponent) world.GetComponent(inventoryId, InventoryComponent.class);

        Stage stage = new Stage();
        VBox layout = new VBox(10);

        layout.getChildren().add(new Label("Inventory"));

        for (var entry : inventory.getHarvestedCrops().entrySet()) {
            layout.getChildren().add(new Label(entry.getKey() + " x" + entry.getValue()));
        }

        layout.setPadding(new Insets(20));
        stage.setScene(new Scene(layout, 300, 400));
        stage.setTitle("Inventory");
        stage.show();
    }

    @Override
    public boolean additem(Item item) {
       boolean result = true;
        /*try {
            for (InviItme invItem : invitory) {
                if (invItem.getItem().equals(item)) {
                    invItem.addCount(1); // Fast 1, ikke amount!
                    return result;
                }
            }
            invitory.add(new InviItme(item)); // Bruger den originale constructor
        } catch (Exception e) {
            System.console().printf(e.getMessage());
            result = false;
        }*/
        return result;
    }

    @Override
    public boolean additem(Item item, int quantity) {
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
    public void showInvi() {
        Stage inviStage = new Stage();
        VBox layout = new VBox(10);
        Label title = new Label("Inventory");

        layout.getChildren().add(title);
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
