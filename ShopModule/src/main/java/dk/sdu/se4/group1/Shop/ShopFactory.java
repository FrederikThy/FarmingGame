package dk.sdu.se4.group1.Shop;

import dk.sdu.se4.group1.CommonEcs.Components.ShopComponent;
import dk.sdu.se4.group1.CommonEcs.EntityID;
import dk.sdu.se4.group1.CommonEcs.World;

public class ShopFactory {
    public static EntityID createShop(World world)
    {
        EntityID shopId = world.createEntity();
        world.addComponent(shopId,new ShopComponent());
        return shopId;
    }
}
