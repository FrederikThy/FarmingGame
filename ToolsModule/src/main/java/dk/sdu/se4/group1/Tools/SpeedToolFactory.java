package dk.sdu.se4.group1.Tools;

import dk.sdu.se4.group1.CommonEcs.Components.SpeedToolIComponentService;
import dk.sdu.se4.group1.CommonEcs.Components.ShopIComponentService;
import dk.sdu.se4.group1.CommonEcs.EntityID;
import dk.sdu.se4.group1.CommonEcs.World;

public class SpeedToolFactory {

    private static final int speedToolPrice = 250;

    public static void registerInShop(World world) {
        for (EntityID entity : world.getEntitiesWith(ShopIComponentService.class)) {
            ShopIComponentService shop =
                    (ShopIComponentService) world.GetComponent(entity, ShopIComponentService.class);
            shop.addShopItem(new SpeedToolIComponentService(0.15), speedToolPrice);
            return;
        }
    }

    public static void applyUpgrade(EntityID robotEntity, World world) {
        if (world.hasComponent(robotEntity, SpeedToolIComponentService.class)) {
            SpeedToolIComponentService existing =
                    (SpeedToolIComponentService) world.GetComponent(robotEntity, SpeedToolIComponentService.class);
            world.addComponent(robotEntity, new SpeedToolIComponentService(existing.getSpeedMultiplier() + 1.0));
        } else {
            world.addComponent(robotEntity, new SpeedToolIComponentService(2.0));
        }
    }
}
