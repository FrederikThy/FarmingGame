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
