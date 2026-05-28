package dk.sdu.se4.group1.Shop;

import dk.sdu.se4.group1.CommonApi.SeedType;
import dk.sdu.se4.group1.CommonEcs.Components.*;
import dk.sdu.se4.group1.CommonEcs.EntityID;
import dk.sdu.se4.group1.CommonEcs.World;

public class ShopFactory {
    public static EntityID createShop(World world)
    {
        EntityID shopId = world.createEntity();
        ShopIComponentService shopComponent = new ShopIComponentService();

        new ShopCatalog().addDefaultOffersTo(shopComponent);

        world.addComponent(shopId, shopComponent);
        return shopId;
    }
}
