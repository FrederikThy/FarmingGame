package dk.sdu.se4.group1.Inventory;

import dk.sdu.se4.group1.CommonApi.SeedType;
import dk.sdu.se4.group1.CommonEcs.Components.InventoryIComponentService;
import dk.sdu.se4.group1.CommonEcs.EntityID;
import dk.sdu.se4.group1.CommonEcs.World;

public class InventoryFactory {
    public static EntityID createInventory(World world)
    {
        EntityID InventoryId = world.createEntity();
        InventoryIComponentService invi = new InventoryIComponentService();
        invi.addHarvest(
                SeedType.CARROT,4
        );
        world.addComponent(InventoryId,invi);

        return InventoryId;
    }
}
