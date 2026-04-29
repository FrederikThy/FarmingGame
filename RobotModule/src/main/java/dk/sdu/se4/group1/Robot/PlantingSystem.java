package dk.sdu.se4.group1.Robot;

import dk.sdu.se4.group1.CommonApi.SeedType;
import dk.sdu.se4.group1.CommonEcs.Components.HarvestingComponent;
import dk.sdu.se4.group1.CommonEcs.Components.PlantingComponent;
import dk.sdu.se4.group1.CommonEcs.Components.PositionComponent;
import dk.sdu.se4.group1.CommonEcs.Components.RobotComponent;
import dk.sdu.se4.group1.CommonEcs.EcsSystem;
import dk.sdu.se4.group1.CommonEcs.EntityID;
import dk.sdu.se4.group1.CommonEcs.World;

import java.util.Random;

public class PlantingSystem implements EcsSystem {

    private final Random random = new Random();
    private double timeSinceLastPlantCheck = 0.0;
    private static final double PLANT_INTERVAL = 2.0;

    private static final int MAP_WIDTH = 10;
    private static final int MAP_HEIGHT = 10;
    @Override
    public void update(World world, double deltaTime) {
        timeSinceLastPlantCheck += deltaTime;

        boolean shouldCheckPlanting = timeSinceLastPlantCheck >= PLANT_INTERVAL;

        for (EntityID entity : world.getEntitiesWith(PlantingComponent.class)) {
            PositionComponent robotPos =
                    (PositionComponent) world.GetComponent(entity, PositionComponent.class);


            if (shouldCheckPlanting) {
                tryPlantSeed(world, robotPos);
            }
        }


        if (shouldCheckPlanting) {
            timeSinceLastPlantCheck = 0.0;
        }
    }

    private void tryPlantSeed(World world, PositionComponent robotPos) {
        int chance = random.nextInt(10) + 1; // 1 to 10

        if (chance >= 5) {
            return;
        }

        SeedType seedType = getRandomSeedType();

        int[] plantTile = findFreeAdjacentTile(world, robotPos.x, robotPos.y);

        if (plantTile != null) {
            world.addSeedToQueue(plantTile[0], plantTile[1], seedType);
        }
    }

    private SeedType getRandomSeedType() {
        SeedType[] seedTypes = SeedType.values();
        return seedTypes[random.nextInt(seedTypes.length)];
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
