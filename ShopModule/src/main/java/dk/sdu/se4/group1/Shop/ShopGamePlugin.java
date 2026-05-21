package dk.sdu.se4.group1.Shop;

import dk.sdu.se4.group1.CommonEcs.IGamePlugin;
import dk.sdu.se4.group1.CommonEcs.World;

/**
 * Game initialization plugin for Shop module.
 */
public class ShopGamePlugin implements IGamePlugin {

    @Override
    public void start(World world) {
        // Initialize shop
        ShopFactory.createShop(world);
    }

    @Override
    public void stop(World world) {
    }
}

