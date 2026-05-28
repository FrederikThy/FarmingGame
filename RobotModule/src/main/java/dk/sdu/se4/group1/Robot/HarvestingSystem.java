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
            EntityID crop = FindCropAtPosition(world, pos);

            if (crop != null) {

                //inventory.addToWallet(100);
                //world.RemoveEntity(crop);
                CropIComponentService cropComponent = (CropIComponentService) world.GetComponent(crop,CropIComponentService.class);

                if (cropComponent==null){
                    continue;
                }

                inventory.addHarvest(cropComponent.seedType,1);
                world.RemoveEntity(crop);


            }
        }
    }

    private EntityID FindCropAtPosition(World world, PositionIComponentService pos) {
       // Oppe
        EntityID crop = FindCropAt(world, pos.x, pos.y - 1);
        if(crop != null) {
            return crop;
        }
        // Ned
        crop = FindCropAt(world, pos.x, pos.y + 1);
        if(crop != null) {
            return crop;
        }

        // Venstre
        crop = FindCropAt(world, pos.x - 1, pos.y);
        if(crop != null) {
            return crop;
        }

        crop = FindCropAt(world, pos.x + 1, pos.y);
        if(crop != null) {
            return crop;
        }
        return null;
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
