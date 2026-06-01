package dk.sdu.se4.group1.Shop;

import dk.sdu.se4.group1.CommonEcs.*;
import dk.sdu.se4.group1.CommonEcs.Components.*;

import java.util.ServiceLoader;

public class ShopPurchaseService {

    public boolean purchase(World world, InventoryIComponentService inventory, ShopOfferIComponentService offer)
    {
        int price = offer.getBuyPrice();

        if (inventory.getWallet() < price) {
            return false;
        }

        IComponentService item = offer.getComponent();

        if (item instanceof CropIComponentService crop) {
            buySeed(inventory, crop);
            inventory.removeFromWallet(price);
            return true;
        }

        if (item instanceof RobotIComponentService robot) {
            buyRobot(world, robot);
            inventory.removeFromWallet(price);
            return true;
        }

        if (item instanceof PathfindingAlgorithmIComponentService upgrade) {
            boolean upgraded = buyPathfindingUpgrade(world, upgrade);
            if (upgraded) {
                inventory.removeFromWallet(price);
            }
            return upgraded;
        }

        return false;
    }
    public boolean buyToolForRobot(World world, InventoryIComponentService inventory,
                                   ShopOfferIComponentService offer, EntityID robotEntity) {
        for (ToolSPI tool : ServiceLoader.load(ToolSPI.class)) {
            if (tool.getToolType().isInstance(offer.getComponent())) {
                return tool.applyTool(world, inventory, offer, robotEntity);
            }
        }
        return false;
    }

    public boolean buySoilUpgrade(World world,InventoryIComponentService inventory,int price)
    {
        if (inventory.getWallet() < price) {
            return false;
        }

        GrowthMapIComponentService growthMap = findGrowthMapComponent(world);

        if (growthMap == null) {
            return false;
        }

        int nextLevel = growthMap.getUnlockedMapLevel() + 1;

        if (nextLevel > 2) {
            return false;
        }

        boolean upgraded = growthMap.unlockMap(nextLevel);

        if (upgraded) {
            inventory.removeFromWallet(price);
        }

        return upgraded;
    }

    private void buySeed(InventoryIComponentService inventory, CropIComponentService crop)
    {
        inventory.addSeeds(crop.seedType, 1);
    }

    private void buyRobot(World world, RobotIComponentService robot)
    {
        RobotSPI robotCreator = ServiceLoader.load(RobotSPI.class)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Can't find RobotSPI"));

        robotCreator.createRobot(world, robot.robotType, 9, 9, 1, 1);
    }

    private boolean buyPathfindingUpgrade(World world, PathfindingAlgorithmIComponentService upgradeItem)
    {
        var upgradeEntities = world.getEntitiesWith(PathfindingUpgradeIComponentService.class);

        if (upgradeEntities == null || !upgradeEntities.iterator().hasNext()) {
            return false;
        }

        PathfindingUpgradeIComponentService upgrade =
                (PathfindingUpgradeIComponentService) world.GetComponent(
                        upgradeEntities.iterator().next(),
                        PathfindingUpgradeIComponentService.class
                );

        if (upgrade.activeTier.tier >= upgradeItem.tier.tier) {
            return false;
        }

        upgrade.activeTier = upgradeItem.tier;
        return true;
    }

    private GrowthMapIComponentService findGrowthMapComponent(World world)
    {
        var entities = world.getEntitiesWith(GrowthMapIComponentService.class);

        if (entities != null && entities.iterator().hasNext()) {
            return (GrowthMapIComponentService) world.GetComponent(
                    entities.iterator().next(),
                    GrowthMapIComponentService.class
            );
        }

        return null;
    }

}
