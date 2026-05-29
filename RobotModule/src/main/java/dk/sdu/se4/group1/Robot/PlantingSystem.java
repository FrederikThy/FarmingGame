package dk.sdu.se4.group1.Robot;

import dk.sdu.se4.group1.CommonApi.SeedType;
import dk.sdu.se4.group1.CommonEcs.Components.*;
import dk.sdu.se4.group1.CommonEcs.IEntityProcessingService;
import dk.sdu.se4.group1.CommonEcs.EntityID;
import dk.sdu.se4.group1.CommonEcs.World;

import java.util.Random;

public class PlantingSystem implements IEntityProcessingService {

    private final Random random = new Random();


    private static final int MAP_WIDTH = 10;
    private static final int MAP_HEIGHT = 10;
    @Override
    public void update(World world, double deltaTime) {

        for (EntityID entity : world.getEntitiesWith(PlantingIComponentService.class)) {
            PositionIComponentService robotPos = (PositionIComponentService) world.GetComponent(entity, PositionIComponentService.class);
            PathIComponentService path = (PathIComponentService) world.GetComponent(entity, PathIComponentService.class);
            PlantingIComponentService planting = (PlantingIComponentService) world.GetComponent(entity, PlantingIComponentService.class);


            if (!path.arrived || !planting.waitingToPlant) {
                continue;
            }
            planting.plantingTimer += deltaTime;

            if (planting.plantingTimer >= 2.0){
                tryPlantSeed(world, entity, robotPos);
                planting.plantingTimer = 0.0;
                planting.waitingToPlant = false;
            }

        }

    }

    private void tryPlantSeed(World world,EntityID entity, PositionIComponentService robotPos) {


        // Henter inventory. kommentar om iterator.next i harvestingSystem.
        EntityID inventoryEntity = world.getEntitiesWith(InventoryIComponentService.class).iterator().next();

        InventoryIComponentService inventory = (InventoryIComponentService) world.GetComponent(inventoryEntity, InventoryIComponentService.class);

        int[] plantTile = findFreeAdjacentTile(world, robotPos.x,  robotPos.y);

        // For at det ikke crasher hvis det er null
        if (plantTile == null) {
            return;
        }

        SeedType seedType = null;


        // Bruger entrySet til at få par af key, value par fordi getSeedStorage er Map

        for (var entry : inventory.getSeedStorage().entrySet()) {
            if (entry.getValue() > 0) {
                // Får key fra vores key/value par
                seedType = entry.getKey();
                break;
            }
        }

        // For at det ikke crasher hvis det er null
        if (seedType == null) {
            return;
        }

        // Fjerner den seed vi har brugt fra storage.
        boolean success = inventory.removeSeedsFromStorage(seedType, 1);

        // For at det ikke crasher hvis det er null
        if (!success) {
            return;
        }

        // Tilføjer til seedQueue
        world.addSeedToQueue(plantTile[0], plantTile[1], seedType);

    }

    private int[] findFreeAdjacentTile(World world, int robotX, int robotY) {
        int[][] directions = {
                {0, -1}, // up
                {0, 1},  // down
                {-1, 0}, // left
                {1, 0}   // right
        };

        // optional: randomize start direction
        int startIndex = random.nextInt(directions.length);

        for (int i = 0; i < directions.length; i++) {
            int[] dir = directions[(startIndex + i) % directions.length];

            int x = robotX + dir[0];
            int y = robotY + dir[1];

            if (!isInsideMap(x, y)) {
                continue;
            }

            if (world.isTileFree(x, y)) {
                return new int[]{x, y};
            }
        }

        return null;
    }

    private boolean isInsideMap(int x, int y) {
        return x >= 0 && x < MAP_WIDTH && y >= 0 && y < MAP_HEIGHT;
    }

}
