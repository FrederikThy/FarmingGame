package dk.sdu.se4.group1.Shop;

import dk.sdu.se4.group1.CommonApi.SeedType;
import dk.sdu.se4.group1.CommonEcs.*;
import dk.sdu.se4.group1.CommonEcs.Components.InventoryIComponentService;
import dk.sdu.se4.group1.CommonEcs.Components.RobotIComponentService;
import dk.sdu.se4.group1.CommonEcs.Components.ShopIComponentService;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;


public class ShopStore implements IShopService, IEntityProcessingService {

    private double shopUpdateTimer = 0.0;

    private VBox activeAllList;
    private VBox activeCropList;
    private VBox activeSpeedList;
    private VBox activeRobotList;

    private ShopIComponentService activeShop;
    private InventoryIComponentService activeInventory;
    private Label activeWalletLabel;
    private World activeWorld;

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
    @Override
    public void openShop(World world) {
        Stage shopStage = new Stage();
        VBox layout = new VBox(10);

        layout.getChildren().add(new Label("Shop"));

        for (SeedType seedType : SeedType.values()) {
            Button buyBtn = new Button("Køb " + seedType + "pris: 100");
            buyBtn.setOnAction(e -> {
                EntityID robotId = findAvailableRobot(world);

                if (robotId == null) {
                    System.out.println("Ingen ledig plante-robot");
                    return;
                }

                RobotIComponentService robot =
                        (RobotIComponentService) world.GetComponent(robotId, RobotIComponentService.class);

                robot.seedType = seedType;
                System.out.println("Gav " + seedType + " til robot " +robotId.id());
            });

            layout.getChildren().add(buyBtn);
            layout.setPadding(new Insets(20));
            layout.setPadding(new Insets(20));
        }
        shopStage.setScene(new Scene(layout, 300, 400));
        shopStage.setTitle("Shop");
        shopStage.show();
    }

    @Override
    public List<EntityID> getShopItems() {
        return List.of();
    }



    @Override
    public boolean buyItem(int entityID, Item item, int quantity) {
        return false;
    }

    @Override
    public int getBuyPrice(SeedType type) {
        return 0;
    }

    @Override
    public int getSellPrice(int entityID, int amount) {
        return 0;
    }

    @Override
    public int getSellPrice(SeedType seedType, int amount) {
        return switch (seedType) {
            case BEANSPROUT -> 110 * amount;
            case CHILI -> 120 * amount;
            case CARROT -> 150 * amount;
            case TOMATO -> 100 * amount;
        };
    }

    @Override
    public boolean isAvailable(int price, int wallet ) {
        return false;
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
    public void update(World world, double deltaTime) {

    }
}
