package dk.sdu.se4.group1.Inventory;

import dk.sdu.se4.group1.CommonApi.SeedType;
import dk.sdu.se4.group1.CommonEcs.Components.InventoryComponent;
import dk.sdu.se4.group1.CommonEcs.Components.ShopComponent;
import dk.sdu.se4.group1.CommonEcs.EntityID;
import dk.sdu.se4.group1.CommonEcs.World;

public class InventoryFactory {
    public static EntityID createInventory(World world)
    {
        EntityID InventoryId = world.createEntity();
        InventoryComponent invi = new InventoryComponent();
        invi.addHarvest(
                SeedType.CARROT,4
        );
        world.addComponent(InventoryId,invi);

        return InventoryId;
    }
}
