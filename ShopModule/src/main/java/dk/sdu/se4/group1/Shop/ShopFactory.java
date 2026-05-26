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

        shopComponent.addShopItem(new CropIComponentService(SeedType.CARROT),100);
        shopComponent.addShopItem(new CropIComponentService(SeedType.TOMATO),70);
        shopComponent.addShopItem(new CropIComponentService(SeedType.CHILI),50);
        shopComponent.addShopItem(new CropIComponentService(SeedType.BEANSPROUT),100);

        shopComponent.addShopItem(new PlantingIComponentService(),150);
        shopComponent.addShopItem(new HarvestingIComponentService(),200);
        shopComponent.addShopItem(new SpeedToolIComponentService(0.15),250);
        // Instead of a pop-up where you choose which robot to purchase, there's just a button for each robot.
        shopComponent.addShopItem(new RobotIComponentService(0,0,RobotType.WEED_REMOVER),1000);
        shopComponent.addShopItem(new RobotIComponentService(0,0,RobotType.HARVEST),1000);
        shopComponent.addShopItem(new RobotIComponentService(0,0,RobotType.PLANT),1000);

        // Pathfinding algorithm upgrades — BFS is free/default
        shopComponent.addShopItem(
                new PathfindingAlgorithmIComponentService(PathfindingUpgradeIComponentService.AlgorithmTier.DIJKSTRA), 500);
        shopComponent.addShopItem(
                new PathfindingAlgorithmIComponentService(PathfindingUpgradeIComponentService.AlgorithmTier.A_STAR), 1500);

        world.addComponent(shopId, shopComponent);
        return shopId;
    }
}
