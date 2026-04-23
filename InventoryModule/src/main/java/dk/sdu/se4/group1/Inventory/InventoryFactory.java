package dk.sdu.se4.group1.Inventory;

import dk.sdu.se4.group1.CommonEcs.Components.InventoryComponent;
import dk.sdu.se4.group1.CommonEcs.Components.ShopComponent;
import dk.sdu.se4.group1.CommonEcs.EntityID;
import dk.sdu.se4.group1.CommonEcs.World;

public class InventoryFactory {
    public static EntityID createInventory(World world)
    {
        EntityID InventoryId = world.createEntity();
        world.addComponent(InventoryId,new InventoryComponent());
        return InventoryId;
    }
}
