package dk.sdu.se4.group1.Shop;

import dk.sdu.se4.group1.CommonApi.SeedType;
import dk.sdu.se4.group1.CommonEcs.Components.*;
import dk.sdu.se4.group1.CommonEcs.EntityID;
import dk.sdu.se4.group1.CommonEcs.World;

public class ShopFactory {
    public static EntityID createShop(World world)
    {
        EntityID shopId = world.createEntity();
        ShopComponent shopComponent = new ShopComponent();

        shopComponent.addShopItem(new CropComponent(SeedType.CARROT),100);
        shopComponent.addShopItem(new CropComponent(SeedType.TOMATO),70);
        shopComponent.addShopItem(new CropComponent(SeedType.CHILI),50);
        shopComponent.addShopItem(new CropComponent(SeedType.BEANSPROUT),100);

        shopComponent.addShopItem(new PlantingComponent(),150);
        shopComponent.addShopItem(new HarvestingComponent(),200);
        shopComponent.addShopItem(new SpeedToolComponent(0.15),250);
        // Instead of a pop-up where you choose which robot to purchase, there's just a button for each robot.
        shopComponent.addShopItem(new RobotComponent(0,0,RobotType.WEED_REMOVER),1000);
        shopComponent.addShopItem(new RobotComponent(0,0,RobotType.HARVEST),1000);
        shopComponent.addShopItem(new RobotComponent(0,0,RobotType.PLANT),1000);

        // Pathfinding algorithm upgrades — BFS is free/default
        shopComponent.addShopItem(
                new PathfindingAlgorithmComponent(PathfindingUpgradeComponent.AlgorithmTier.DIJKSTRA), 500);
        shopComponent.addShopItem(
                new PathfindingAlgorithmComponent(PathfindingUpgradeComponent.AlgorithmTier.A_STAR), 1500);

        world.addComponent(shopId, shopComponent);
        return shopId;
    }
}
