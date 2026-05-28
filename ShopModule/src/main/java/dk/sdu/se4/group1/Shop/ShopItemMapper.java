package dk.sdu.se4.group1.Shop;

import dk.sdu.se4.group1.CommonEcs.Components.*;
import dk.sdu.se4.group1.CommonEcs.IComponentService;

public class ShopItemMapper {
    public String getName(IComponentService IComponentService) {
        if (IComponentService instanceof CropIComponentService cropComponent) {
            return formatSeedName(cropComponent.seedType.toString()) + " Seed";
        }

        if (IComponentService instanceof SpeedToolIComponentService) {
            return "Speed Tool";
        }

        if (IComponentService instanceof PlantingIComponentService) {
            return "Planting Tool";
        }

        if (IComponentService instanceof HarvestingIComponentService) {
            return "Harvesting Tool";
        }
        // Instead of only one robot to pick, we have three
        if (IComponentService instanceof RobotIComponentService robotComponent) {
            return switch (robotComponent.robotType){
                case WEED_REMOVER ->  "Weed Remover";
                case HARVEST ->  "Harvest";
                case PLANT ->  "Planting";
            };
        }
        if (IComponentService instanceof PathfindingAlgorithmIComponentService algoComponent) {
            return switch (algoComponent.tier) {
                case DIJKSTRA -> "Dijkstra Pathfinding";
                case A_STAR   -> "A* Pathfinding";
                default       -> algoComponent.tier.displayName + " Pathfinding";
            };
        }

        return IComponentService.getClass().getSimpleName();
    }

    public String getImagePath(IComponentService IComponentService) {
        if (IComponentService instanceof CropIComponentService cropComponent) {
            return "/" + cropComponent.seedType.name().toLowerCase() + ".png";
        }

        if (IComponentService instanceof SpeedToolIComponentService) {
            return "/Speed_Tool.png";
        }

        if (IComponentService instanceof PlantingIComponentService) {
            return "/Planting_Tool.png";
        }

        if (IComponentService instanceof HarvestingIComponentService) {
            return "/Harvesting_Tool.png";
        }
        // Picture for each of the robots
        if(IComponentService instanceof RobotIComponentService robotComponent) {
            return switch (robotComponent.robotType){
                case HARVEST -> "/HrFlink_1.png";
                case PLANT -> "/HrFlink_2.png";
                case WEED_REMOVER ->  "/HrFlink_3.png";
            };
        }
        if (IComponentService instanceof PathfindingAlgorithmIComponentService) {
            return "/gear.png";
        }
        return "/item_slot.png";
    }

    public ShopCategory getCategory(IComponentService component) {
        if (component instanceof CropIComponentService) {
            return ShopCategory.CROP;
        }

        if (component instanceof RobotIComponentService ||
                component instanceof PathfindingAlgorithmIComponentService) {
            return ShopCategory.ROBOT;
        }

        if (component instanceof SpeedToolIComponentService ||
                component instanceof PlantingIComponentService ||
                component instanceof HarvestingIComponentService) {
            return ShopCategory.Tool;
        }

        return ShopCategory.OTHER;
    }

    private String formatSeedName(String seedType) {
        String lower = seedType.toLowerCase();
        return lower.substring(0, 1).toUpperCase() + lower.substring(1);
    }
}
