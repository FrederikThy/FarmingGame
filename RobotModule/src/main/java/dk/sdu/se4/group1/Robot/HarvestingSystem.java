package dk.sdu.se4.group1.Robot;

import dk.sdu.se4.group1.CommonEcs.Components.CropComponent;
import dk.sdu.se4.group1.CommonEcs.Components.HarvestingComponent;
import dk.sdu.se4.group1.CommonEcs.Components.PositionComponent;
import dk.sdu.se4.group1.CommonEcs.EcsSystem;
import dk.sdu.se4.group1.CommonEcs.Entity;
import dk.sdu.se4.group1.CommonEcs.EntityID;
import dk.sdu.se4.group1.CommonEcs.World;
import javafx.geometry.Pos;

public class HarvestingSystem implements EcsSystem {

    @Override
    public void update(World world, double deltaTime) {
        for (EntityID entity : world.getEntitiesWith(HarvestingComponent.class)) {
            PositionComponent pos = (PositionComponent) world.GetComponent(entity, PositionComponent.class);

            EntityID crop = FindCropAtPosition(world, pos);

            if (crop != null) {
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
