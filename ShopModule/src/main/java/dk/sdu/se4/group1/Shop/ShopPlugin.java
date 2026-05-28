package dk.sdu.se4.group1.Shop;
import dk.sdu.se4.group1.CommonEcs.*;
import dk.sdu.se4.group1.CommonEcs.Components.*;
import javafx.scene.control.*;

public class ShopPlugin extends Button implements IEntityProcessingService {

    private final ShopController controller;

    public ShopPlugin(World world) {

        this.controller = new ShopController();
        this.setLayoutX(710);
        this.setLayoutY(180);
        this.setPrefWidth(230);
        this.setPrefHeight(150);
        this.setStyle("-fx-background-color: rgba(255, 255, 255, 0);");
        this.setOnAction(e -> {
            controller.openShop(world);
        });
    }

    @Override
    public void update(World world, double deltaTime) {
        controller.update(world,deltaTime);
    }
}
