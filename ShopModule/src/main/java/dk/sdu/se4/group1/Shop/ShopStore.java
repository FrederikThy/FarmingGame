package dk.sdu.se4.group1.Shop;

import dk.sdu.se4.group1.CommonApi.SeedType;
import dk.sdu.se4.group1.CommonEcs.*;
import dk.sdu.se4.group1.CommonEcs.Components.CropComponent;
import dk.sdu.se4.group1.CommonEcs.Components.RobotComponent;
import dk.sdu.se4.group1.CommonEcs.Components.SpeedToolComponent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.List;

public class ShopStore implements IShopService,EcsSystem {

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
        Stage shopStage = new Stage();
        VBox layout = new VBox(10);

        layout.getChildren().add(new Label("Shop"));

        for (SeedType seedType : SeedType.values()) {
            //Seeds buy button
            Button buyBtn = new Button("Køb " + seedType + "pris: 100");
            buyBtn.setOnAction(e -> {
                EntityID robotId = findAvailableRobot(world);

                if (robotId == null) {
                    System.out.println("Ingen ledig plante-robot");
                    return;
                }

                RobotComponent robot =
                        (RobotComponent) world.GetComponent(robotId, RobotComponent.class);

                robot.seedType = seedType;
                System.out.println("Gav " + seedType + " til robot " +robotId.id());
            });
            layout.getChildren().add(buyBtn);
            layout.setPadding(new Insets(20));
            layout.setPadding(new Insets(20));
        }
        //Speed tool buy button
        Button speedToolBtn = new Button("Køb Speed Tool - pris: 200");
        speedToolBtn.setOnAction(e -> {
            for (EntityID entity : world.getEntitiesWith(RobotComponent.class)) {
                if (!world.hasComponent(entity, SpeedToolComponent.class)) {
                    world.addComponent(entity, new SpeedToolComponent(2.0));
                    System.out.println("Speed tool equipped on robot " + entity.id());
                }
            }
        });
        layout.getChildren().add(speedToolBtn);

        shopStage.setScene(new Scene(layout, 300, 400));
        shopStage.setTitle("Shop");
        shopStage.show();
    }

    @Override
    public List<EntityID> getShopItems(World world) {
        return List.of();
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

    @Override
    public void update(World world, double deltaTime) {

    }
}
