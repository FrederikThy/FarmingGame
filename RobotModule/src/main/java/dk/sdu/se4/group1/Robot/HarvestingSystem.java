package dk.sdu.se4.group1.Robot;

import dk.sdu.se4.group1.CommonEcs.*;
import dk.sdu.se4.group1.CommonEcs.Components.*;

public class HarvestingSystem implements IEntityProcessingService {

    @Override
    public void update(World world, double deltaTime) {
        // the variable expects an EntityID as a return. It gets a Set<> because of getentitiesWith.
        // iterator.next gets the first element in the list, and returns it as EntityID
        EntityID inventoryEntity = world.getEntitiesWith(InventoryIComponentService.class).iterator().next();
        InventoryIComponentService inventory = (InventoryIComponentService) world.GetComponent(inventoryEntity, InventoryIComponentService.class);


        for (EntityID entity : world.getEntitiesWith(HarvestingIComponentService.class)) {
            PositionIComponentService pos = (PositionIComponentService) world.GetComponent(entity, PositionIComponentService.class);
            PathIComponentService path = (PathIComponentService) world.GetComponent(entity, PathIComponentService.class);
            HarvestingIComponentService harvesting = (HarvestingIComponentService)  world.GetComponent(entity, HarvestingIComponentService.class);
            EntityID crop = FindCropAt(world, pos.x, pos.y);

            if (!path.arrived || !harvesting.waitingToHarvest) {
                continue;
            }

            harvesting.harvestingTime += deltaTime;

            if (harvesting.harvestingTime < 2.0){
                continue;
            }

            if (crop != null) {

                //inventory.addToWallet(100);
                //world.RemoveEntity(crop);
                CropIComponentService cropComponent = (CropIComponentService) world.GetComponent(crop,CropIComponentService.class);

                if (cropComponent==null){
                    continue;
                }

                inventory.addHarvest(cropComponent.seedType,1);
                world.RemoveEntity(crop);

                harvesting.harvestingTime = 0.0;
                harvesting.waitingToHarvest = false;

            }
        }
    }


    private EntityID FindCropAt(World world, double x, double y) {
        for (EntityID entity : world.getEntitiesWith(CropIComponentService.class)) {
            PositionIComponentService posCrop = (PositionIComponentService) world.GetComponent(entity, PositionIComponentService.class);
            CropIComponentService cropComponent = (CropIComponentService) world.GetComponent(entity, CropIComponentService.class);
            if (posCrop == null){
                continue;
            }
            if (posCrop.x == x && posCrop.y == y && cropComponent.isHarvestable == true){
                return entity;
            }

        }
        return null;
    }

}
