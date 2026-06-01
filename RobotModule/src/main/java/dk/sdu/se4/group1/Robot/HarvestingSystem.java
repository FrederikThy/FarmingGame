package dk.sdu.se4.group1.Robot;

import dk.sdu.se4.group1.CommonEcs.*;
import dk.sdu.se4.group1.CommonEcs.Components.*;

public class HarvestingSystem implements IEntityProcessingService {

    @Override
    public void update(World world, double deltaTime) {
        EntityID inventoryEntity = world.getEntitiesWith(InventoryIComponentService.class).iterator().next();
        InventoryIComponentService inventory = (InventoryIComponentService) world.GetComponent(inventoryEntity, InventoryIComponentService.class);

        for (EntityID entity : world.getEntitiesWith(HarvestingIComponentService.class)) {
            PositionIComponentService pos = (PositionIComponentService) world.GetComponent(entity, PositionIComponentService.class);
            HarvestingIComponentService tool = (HarvestingIComponentService) world.GetComponent(entity, HarvestingIComponentService.class);
            EntityID crop = FindCropAtPosition(world, pos);

            if (crop != null) {
                tool.harvestWaitTimer += deltaTime;
                // Each upgrade level shaves 1 second off the base 5-second wait
                double waitTime = Math.max(0.0, 5.0 - (tool.getGrowthMultiplier() - 1.0));
                if (tool.harvestWaitTimer >= waitTime) {
                    tool.harvestWaitTimer = 0.0;
                    CropIComponentService cropComponent = (CropIComponentService) world.GetComponent(crop, CropIComponentService.class);
                    if (cropComponent == null) continue;
                    inventory.addHarvest(cropComponent.seedType, 1);
                    world.RemoveEntity(crop);
                }
            } else {
                tool.harvestWaitTimer = 0.0;
            }
        }
    }

    private EntityID FindCropAtPosition(World world, PositionIComponentService pos) {
        EntityID crop = FindCropAt(world, pos.x, pos.y - 1);
        if (crop != null) return crop;{
            crop = FindCropAt(world, pos.x, pos.y + 1);}
        if (crop != null) return crop;{
        crop = FindCropAt(world, pos.x - 1, pos.y);}
        if (crop != null) return crop;{
        crop = FindCropAt(world, pos.x + 1, pos.y);}
        if (crop != null) return crop;{
        return FindCropAt(world, pos.x, pos.y);}
    }

    private EntityID FindCropAt(World world, double x, double y) {
        for (EntityID entity : world.getEntitiesWith(CropIComponentService.class)) {
            PositionIComponentService posCrop = (PositionIComponentService) world.GetComponent(entity, PositionIComponentService.class);
            CropIComponentService cropComponent = (CropIComponentService) world.GetComponent(entity, CropIComponentService.class);
            if (posCrop == null) continue;
            if (posCrop.x == x && posCrop.y == y && cropComponent.isHarvestable) return entity;
        }
        return null;
    }
}