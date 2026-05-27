package dk.sdu.se4.group1.Tools;

import dk.sdu.se4.group1.CommonEcs.Components.InventoryIComponentService;
import dk.sdu.se4.group1.CommonEcs.Components.RobotIComponentService;
import dk.sdu.se4.group1.CommonEcs.Components.SpeedToolIComponentService;
import dk.sdu.se4.group1.CommonEcs.EntityID;
import dk.sdu.se4.group1.CommonEcs.World;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SpeedToolPlugin extends Button {

    private static final int speedToolPrice = 250;

    public SpeedToolPlugin(World world) {
        this.setText("Speed Tool");
        this.setStyle("-fx-background-color: #c8a96e; -fx-border-color: #3f2d17; -fx-border-width: 2; -fx-padding: 6 14;");
        this.setOnAction(e -> openRobotPicker(world));
    }

    private void openRobotPicker(World world) {
        InventoryIComponentService inventory = findInventory(world);
        if (inventory == null || inventory.getWallet() < speedToolPrice) return;

        Stage pickStage = new Stage();
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 16; -fx-background-color: #f1e0b8;");
        layout.setPadding(new Insets(20));
        layout.getChildren().add(new Label("Choose robot to upgrade:"));

        for (EntityID entity : world.getEntitiesWith(RobotIComponentService.class)) {
            boolean equipped = world.hasComponent(entity, SpeedToolIComponentService.class);
            String label = equipped
                    ? "Robot " + entity.id() + " (level " + (int) ((SpeedToolIComponentService)
                    world.GetComponent(entity, SpeedToolIComponentService.class)).getSpeedMultiplier() + ")"
                    : "Robot " + entity.id();

            Button robotBtn = new Button(label);
            robotBtn.setOnAction(ev -> {
                SpeedToolFactory.applyUpgrade(entity, world);
                inventory.removeFromWallet(speedToolPrice);
                pickStage.close();
            });
            layout.getChildren().add(robotBtn);
        }
        pickStage.setScene(new Scene(layout, 250, 300));
        pickStage.setTitle("Upgrade Speed Tool");
        pickStage.show();
    }

    private InventoryIComponentService findInventory(World world) {
        var entities = world.getEntitiesWith(InventoryIComponentService.class);
        if (entities == null || !entities.iterator().hasNext()) return null;
        return (InventoryIComponentService) world.GetComponent(
                entities.iterator().next(), InventoryIComponentService.class);
    }
}
