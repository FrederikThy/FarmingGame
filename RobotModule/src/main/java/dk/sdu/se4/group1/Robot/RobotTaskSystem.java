package dk.sdu.se4.group1.Robot;

import dk.sdu.se4.group1.CommonEcs.Components.*;
import dk.sdu.se4.group1.CommonEcs.EcsSystem;
import dk.sdu.se4.group1.CommonEcs.EntityID;
import dk.sdu.se4.group1.CommonEcs.World;

import java.nio.file.Path;
import java.util.Random;

// Udelukkende til at give robots en task. Det er MovementSystem der bevæger robotten
public class RobotTaskSystem implements EcsSystem {

    Random random = new Random();
    @Override
    public void update(World world, double deltaTime){
        for (EntityID entity : world.getEntitiesWith(PathComponent.class)) {
            PathComponent path = (PathComponent) world.GetComponent(entity,  PathComponent.class);

            // Hvis robotten allerede har en opgave, skipper vi
            if (!path.isDone() || path.pathPending || !path.arrived){
                continue;
            }

            int[] target = FindTarget(world, entity);

            // Sætter vores goalX og goalY til de koordinater vi får fra FindTarget.
            if (target != null){
                path.goalX = target[0];
                path.goalY = target[1];
                path.goalFixed = true;
                path.arrived = false;
            }
        }
    }

    private int[] FindTarget(World world, EntityID entity){
        // planting robot
        if (world.hasComponent(entity, PlantingComponent.class)){
            return FindPlantingTarget(world);
        }
        // Harvesting robot

        if (world.hasComponent(entity, HarvestingComponent.class)){
            return FindHarvestableCrop(world);
        }
        // RemoveWeed robot
        if (world.hasComponent(entity, RemoveCropComponent.class)){
            return FindWeed(world);
        }
        return null;
    }


    // Bruges af PlantingRobot. Får inventory. Hvis der er noget i inventory, kalder vi FindFreeTile
    // som returnerer et x og y koordinat
    private int[] FindPlantingTarget(World world){
        EntityID invEntity = world.getEntitiesWith(InventoryComponent.class).iterator().next();

        InventoryComponent inv = (InventoryComponent) world.GetComponent(invEntity, InventoryComponent.class);

        for (var entry : inv.getSeedStorage().entrySet()) {
            if (entry.getValue() > 0){
                return FindFreeTile(world);
            }
        }

        return null;
    }


    // returnerer et random x og y koordinat. Bruges som mål for plantingRobot.
    private int[] FindFreeTile(World world){

        int x = random.nextInt(10);
        int y = random.nextInt(10);

        if (world.isTileFree(x, y)){
            return new int[]{x, y};
        }

        return null;
    }

    // Bruges af HarvestingRobot. Returnerer den position for crop
    private int[] FindHarvestableCrop(World world){
        for (EntityID entity : world.getEntitiesWith(CropComponent.class)){
            CropComponent crop = (CropComponent) world.GetComponent(entity, CropComponent.class);
            PositionComponent pos = (PositionComponent) world.GetComponent(entity, PositionComponent.class);

            if (crop != null && crop.isHarvestable && pos != null){
                return new int[]{pos.x, pos.y};
            }
        }
        return null;
    }

    // Finder position for weed, og sender den videre til robotten som så rykker derhen
    private int[] FindWeed(World world){
        for (EntityID entity : world.getEntitiesWith(WeedComponent.class)){
            PositionComponent pos = (PositionComponent) world.GetComponent(entity, PositionComponent.class);

            if (pos != null){
                return new int[]{pos.x, pos.y};
            }
        }
        return null;
    }
}
