package dk.sdu.se4.group1.Robot;

import java.util.Random;

import dk.sdu.se4.group1.CommonApi.SeedType;
import dk.sdu.se4.group1.CommonEcs.Components.RobotComponent;
import dk.sdu.se4.group1.CommonEcs.EcsSystem;
import dk.sdu.se4.group1.CommonEcs.EntityID;
import dk.sdu.se4.group1.CommonEcs.Components.PositionComponent;
import dk.sdu.se4.group1.CommonEcs.World;

public class RobotSystem implements EcsSystem {

    private final Random random = new Random();

    private double timeSinceLastMove = 0.0;
    private static final double MOVE_INTERVAL = 0.3;

    private double timeSinceLastPlantCheck = 0.0;
    private static final double PLANT_INTERVAL = 2.0;

    private static final int MAP_WIDTH = 10;
    private static final int MAP_HEIGHT = 10;


    /**
     * HELE DENNE SIDE ER TEMPORARY AI SLOP FORDI VI IKKE HAR NOGLE ALGORITMER ENDNU :)
     * Temporary robot behavior system.
     * The system makes each robot:
     * 1. move randomly every 0.3 seconds
     * 2. check every 10 seconds whether it should plant a seed
     * 3. choose a random seed type
     * 4. plant only on a free tile directly next to the robot
     *
     * Planting is done by adding a seed request to the world's seed queue.
     * The actual crop entity is then created later by the crop system.
     *
     * This logic is currently simple and mainly intended for testing.
     * It can later be replaced by proper pathfinding and decision-making algorithms.
     */



    @Override
    public void update(World world, double deltaTime) {
        timeSinceLastMove += deltaTime;
        timeSinceLastPlantCheck += deltaTime;

        boolean shouldMove = timeSinceLastMove >= MOVE_INTERVAL;
        boolean shouldCheckPlanting = timeSinceLastPlantCheck >= PLANT_INTERVAL;

        for (EntityID entity : world.getEntitiesWith(RobotComponent.class)) {
            PositionComponent robotPos =
                    (PositionComponent) world.GetComponent(entity, PositionComponent.class);

            RobotComponent robot =
                    (RobotComponent) world.GetComponent(entity, RobotComponent.class);

            if (shouldMove) {
                moveRobotRandomly(world, robotPos);
            }

            if (shouldCheckPlanting) {
                tryPlantSeed(world, robot, robotPos);
            }
        }

        if (shouldMove) {
            timeSinceLastMove = 0.0;
        }

        if (shouldCheckPlanting) {
            timeSinceLastPlantCheck = 0.0;
        }
    }

    private void moveRobotRandomly(World world, PositionComponent robotPos) {
        int targetX = robotPos.x;
        int targetY = robotPos.y;

        int direction = random.nextInt(4);

        switch (direction) {
            case 0 -> targetY = Math.max(0, robotPos.y - 1);              // up
            case 1 -> targetY = Math.min(MAP_HEIGHT - 1, robotPos.y + 1); // down
            case 2 -> targetX = Math.max(0, robotPos.x - 1);              // left
            case 3 -> targetX = Math.min(MAP_WIDTH - 1, robotPos.x + 1);  // right
        }

        if (world.isTileFree(targetX, targetY)) {
            robotPos.x = targetX;
            robotPos.y = targetY;
        }
    }

    private void tryPlantSeed(World world, RobotComponent robot, PositionComponent robotPos) {
        int chance = random.nextInt(10) + 1; // 1 to 10

        if (chance >= 5) {
            return;
        }

        robot.seedType = getRandomSeedType();

        int[] plantTile = findFreeAdjacentTile(world, robotPos.x, robotPos.y);

        if (plantTile != null) {
            world.addSeedToQueue(plantTile[0], plantTile[1], robot.seedType);
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