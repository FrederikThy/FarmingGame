package dk.sdu.se4.group1.Robot;

import dk.sdu.se4.group1.CommonEcs.Components.*;
import dk.sdu.se4.group1.CommonEcs.IEntityProcessingService;
import dk.sdu.se4.group1.CommonEcs.EntityID;
import dk.sdu.se4.group1.CommonEcs.World;

import java.util.Random;

// Udelukkende til at give robots en task. Det er MovementSystem der bevæger robotten
public class RobotTaskSystem implements IEntityProcessingService {

    Random random = new Random();
    @Override
    public void update(World world, double deltaTime){
        for (EntityID entity : world.getEntitiesWith(PathIComponentService.class)) {
            PathIComponentService path = (PathIComponentService) world.GetComponent(entity,  PathIComponentService.class);

            // Hvis robotten allerede har en opgave, skipper vi
            if (!path.isDone() || path.pathPending || !path.arrived){
                continue;
            }

            // If the PlantingRobot already has a task, we skip.
            if (world.hasComponent(entity, PlantingIComponentService.class)){
                PlantingIComponentService planting = (PlantingIComponentService) world.GetComponent(entity, PlantingIComponentService.class);

                if (planting.waitingToPlant == true){
                    continue;
                }
            }

            if (world.hasComponent(entity, HarvestingIComponentService.class)){
                HarvestingIComponentService harvesting = (HarvestingIComponentService)  world.GetComponent(entity, HarvestingIComponentService.class);

                if (harvesting.waitingToHarvest == true){
                    continue;
                }
            }


            int[] target = FindTarget(world, entity);

            // Sætter vores goalX og goalY til de koordinater vi får fra FindTarget.
            if (target != null){
                path.goalX = target[0];
                path.goalY = target[1];
                path.goalFixed = true;
                path.arrived = false;

                if (world.hasComponent(entity, PlantingIComponentService.class)){
                    PlantingIComponentService planting = (PlantingIComponentService) world.GetComponent(entity, PlantingIComponentService.class);

                    planting.waitingToPlant = true;
                    planting.plantingTimer = 0.0;
                }

                if (world.hasComponent(entity, HarvestingIComponentService.class)){
                    HarvestingIComponentService harvesting = (HarvestingIComponentService)  world.GetComponent(entity, HarvestingIComponentService.class);

                    harvesting.waitingToHarvest = true;
                    harvesting.harvestingTime = 0.0;
                }
            }


        }
    }

    private int[] FindTarget(World world, EntityID entity){
        // planting robot
        if (world.hasComponent(entity, PlantingIComponentService.class)){
            return FindPlantingTarget(world);
        }
        // Harvesting robot

        if (world.hasComponent(entity, HarvestingIComponentService.class)){
            return FindHarvestableCrop(world);
        }
        // RemoveWeed robot
        if (world.hasComponent(entity, RemoveCropIComponentService.class)){
            return FindWeed(world);
        }
        return null;
    }


    // Bruges af PlantingRobot. Får inventory. Hvis der er noget i inventory, kalder vi FindFreeTile
    // som returnerer et x og y koordinat
    private int[] FindPlantingTarget(World world){
        EntityID invEntity = world.getEntitiesWith(InventoryIComponentService.class).iterator().next();

        InventoryIComponentService inv = (InventoryIComponentService) world.GetComponent(invEntity, InventoryIComponentService.class);

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
        for (EntityID entity : world.getEntitiesWith(CropIComponentService.class)){
            CropIComponentService crop = (CropIComponentService) world.GetComponent(entity, CropIComponentService.class);
            PositionIComponentService pos = (PositionIComponentService) world.GetComponent(entity, PositionIComponentService.class);

            if (crop != null && crop.isHarvestable && pos != null){
                return new int[]{pos.x, pos.y};
            }
        }
        return null;
    }

    // Finder position for weed, og sender den videre til robotten som så rykker derhen
    private int[] FindWeed(World world){
        for (EntityID entity : world.getEntitiesWith(WeedIComponentService.class)){
            PositionIComponentService pos = (PositionIComponentService) world.GetComponent(entity, PositionIComponentService.class);

            if (pos != null){
                return new int[]{pos.x, pos.y};
            }
        }
        return null;
    }
}
