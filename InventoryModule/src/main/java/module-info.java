module InventoryModule {
    exports dk.sdu.se4.group1.Inventory;
    requires Common.ecs;
    requires javafx.controls;
    requires javafx.graphics;
     provides dk.sdu.se4.group1.CommonEcs.IInventoryService
             with dk.sdu.se4.group1.Inventory.InventoryPlugin;

     uses dk.sdu.se4.group1.CommonEcs.IInventoryService;
     uses dk.sdu.se4.group1.CommonEcs.IShopService;
}