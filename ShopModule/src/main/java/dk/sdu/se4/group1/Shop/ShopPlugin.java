package dk.sdu.se4.group1.Shop;

import dk.sdu.se4.group1.CommonEcs.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 */
public class ShopPlugin implements IShopService {

    List<Item> items = new ArrayList<Item>();
    public ShopPlugin(){
        items.add(new Item(new Entity(),"Dirt", 100, 1, Item.FarmingType.Dirt));
        items.add(new Item(new Entity(),"Seeder", 250, 1, Item.FarmingType.Planer));
        items.add(new Item(new Entity(),"Picker", 750, 1, Item.FarmingType.shovel));
    }
    @Override
    public void openShop() {
        Stage shopStage = new Stage();
        VBox layout = new VBox(10);
        Label title = new Label("Shop");

        for (Item item : getShopItems(1)) {
            HBox itemRow = new HBox(10);

            Label itemLabel = new Label(item.GetAllnfo());

            Button buyBtn = new Button("Køb");
            buyBtn.setOnAction(e -> {
                IInventoryService inventory = IInventoryService.getInstance();
                if (inventory != null) {
                    if (inventory.getWallet() >= item.getPrice()) {
                        inventory.RemoveCoins(item.getPrice());
                        inventory.additem(item);
                        System.out.println("Købt: " + item.GetAllnfo());
                    } else {
                        System.out.println("Ikke nok penge!");
                    }
                }
            });

            itemRow.getChildren().addAll(itemLabel, buyBtn);
            layout.getChildren().add(itemRow);
        }
        layout.setPadding(new Insets(20));
        shopStage.setScene(new Scene(layout,300,400));
        shopStage.setTitle("Shop");


        shopStage.show();

    }

    @Override
    public List<Item> getShopItems(int entityID) {
        return items;
    }

    @Override
    public boolean buyItem(int entityID, Item item) {
        return false;
    }

    @Override
    public boolean buyItem(int entityID, Item item, int quantity) {
        return false;
    }

    @Override
    public int SellItem(int entityID, Item item, int quantity) {
        int coins =0;
        switch (item.getType()){

            case "Dirt":
                coins = item.getLevel()*4;
                break;
            case "shovel":
                coins = item.getLevel()*3;
                break;
            case "Planer":
                coins = item.getLevel()*2;
                break;
        }
        System.out.println("Item Sold");
        return coins;
    }

    @Override
    public int getBuyPrice(Item item) {
        return item.getPrice();
    }

    @Override
    public int getSellPrice(int entityID, Item item) {
        return SellItem(entityID,item,1);
    }
}
