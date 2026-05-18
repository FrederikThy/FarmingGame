package dk.sdu.se4.group1.Inventory;

import dk.sdu.se4.group1.CommonEcs.IGamePlugin;
import dk.sdu.se4.group1.CommonEcs.World;

// Class for loading inventory plugin
public class InventoryGamePlugin implements IGamePlugin {

    InventoryFactory inventoryFactory = new InventoryFactory();
    @Override
    public void start(World world){
        inventoryFactory.createInventory(world);
    }

    @Override
    public void stop(World world) {

    }
}
