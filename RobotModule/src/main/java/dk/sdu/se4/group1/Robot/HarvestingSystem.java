package dk.sdu.se4.group1.Robot;

import dk.sdu.se4.group1.CommonEcs.*;
import dk.sdu.se4.group1.CommonEcs.Components.*;
import javafx.geometry.Pos;

import java.util.Set;

public class HarvestingSystem implements EcsSystem {

    @Override
    public void update(World world, double deltaTime) {
        // the variable expects an EntityID as a return. It gets a Set<> because of getentitiesWith.
        // iterator.next gets the first element in the list, and returns it as EntityID
        EntityID inventoryEntity = world.getEntitiesWith(InventoryComponent.class).iterator().next();

        InventoryComponent inventory = (InventoryComponent) world.GetComponent(inventoryEntity, InventoryComponent.class);

        for (EntityID entity : world.getEntitiesWith(HarvestingComponent.class)) {
            PositionComponent pos = (PositionComponent) world.GetComponent(entity, PositionComponent.class);
            EntityID crop = FindCropAtPosition(world, pos);

            if (crop != null) {

                inventory.addToWallet(100);
                world.RemoveEntity(crop);
            }
        }
    }

    private EntityID FindCropAtPosition(World world, PositionComponent pos) {
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
        for (EntityID entity : world.getEntitiesWith(CropComponent.class)) {
            PositionComponent posCrop = (PositionComponent) world.GetComponent(entity, PositionComponent.class);
            CropComponent cropComponent = (CropComponent) world.GetComponent(entity, CropComponent.class);
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
