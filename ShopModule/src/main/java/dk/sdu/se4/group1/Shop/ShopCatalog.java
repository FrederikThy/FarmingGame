package dk.sdu.se4.group1.Shop;

import dk.sdu.se4.group1.CommonApi.SeedType;
import dk.sdu.se4.group1.CommonEcs.Components.*;

public class ShopCatalog
{
    public void addDefaultOffersTo(ShopIComponentService shop) {
        addSeeds(shop);
        addTools(shop);
        addRobots(shop);
        addPathfindingUpgrades(shop);
    }
    private void addSeeds(ShopIComponentService shop) {
        shop.addShopItem(new CropIComponentService(SeedType.CARROT), 100);
        shop.addShopItem(new CropIComponentService(SeedType.TOMATO), 70);
        shop.addShopItem(new CropIComponentService(SeedType.CHILI), 50);
        shop.addShopItem(new CropIComponentService(SeedType.BEANSPROUT), 100);
    }

    private void addTools(ShopIComponentService shop) {
        shop.addShopItem(new PlantingIComponentService(), 150);
        shop.addShopItem(new HarvestingIComponentService(), 200);
        shop.addShopItem(new SpeedToolIComponentService(0.15), 250);
    }

    private void addRobots(ShopIComponentService shop) {
        shop.addShopItem(new RobotIComponentService(0, 0, RobotType.WEED_REMOVER), 1000);
        shop.addShopItem(new RobotIComponentService(0, 0, RobotType.HARVEST), 1000);
        shop.addShopItem(new RobotIComponentService(0, 0, RobotType.PLANT), 1000);
    }

    private void addPathfindingUpgrades(ShopIComponentService shop) {
        shop.addShopItem(
                new PathfindingAlgorithmIComponentService(
                        PathfindingUpgradeIComponentService.AlgorithmTier.DIJKSTRA
                ),
                500
        );

        shop.addShopItem(
                new PathfindingAlgorithmIComponentService(
                        PathfindingUpgradeIComponentService.AlgorithmTier.A_STAR
                ),
                1500
        );
    }
}
