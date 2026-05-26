import dk.sdu.se4.group1.CommonEcs.IGamePlugin;
import dk.sdu.se4.group1.CommonEcs.IUiPluginService;
import dk.sdu.se4.group1.Inventory.InventoryUiPluginService;

module InventoryModule {
    exports dk.sdu.se4.group1.Inventory;
    requires Common.ecs;
    requires javafx.controls;
    requires javafx.graphics;

    provides IGamePlugin with dk.sdu.se4.group1.Inventory.InventoryGamePlugin;
    provides IUiPluginService with InventoryUiPluginService;
}