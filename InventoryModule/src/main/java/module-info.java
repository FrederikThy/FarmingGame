import dk.sdu.se4.group1.CommonEcs.IGamePlugin;
import dk.sdu.se4.group1.CommonEcs.IUiPlugin;
import dk.sdu.se4.group1.Inventory.InventoryPlugin;

module InventoryModule {
    exports dk.sdu.se4.group1.Inventory;
    requires Common.ecs;
    requires javafx.controls;
    requires javafx.graphics;

    provides IGamePlugin with dk.sdu.se4.group1.Inventory.InventoryGamePlugin;
    provides IUiPlugin with dk.sdu.se4.group1.Inventory.InventoryUiPlugin;
}