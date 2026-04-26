package dk.sdu.se4.group1.Robot;

import java.util.Random;

import dk.sdu.se4.group1.CommonApi.SeedType;
import dk.sdu.se4.group1.CommonEcs.Components.PathComponent;
import dk.sdu.se4.group1.CommonEcs.Components.RobotComponent;
import dk.sdu.se4.group1.CommonEcs.Components.SpeedToolComponent;
import dk.sdu.se4.group1.CommonEcs.EcsSystem;
import dk.sdu.se4.group1.CommonEcs.EntityID;
import dk.sdu.se4.group1.CommonEcs.Components.PositionComponent;
import dk.sdu.se4.group1.CommonEcs.MapSize;
import dk.sdu.se4.group1.CommonEcs.Node;
import dk.sdu.se4.group1.CommonEcs.World;

// ECS system that moves robots and handles planting each tick
// Movement uses A* waypoints from PathComponent — if no PathComponent exists it falls back to random movement
public class RobotSystem implements EcsSystem {

    private final Random random = new Random();

    // How much time has passed since the robot last moved
    private double timeSinceLastMove = 0.0;
    // Robot moves one tile every 0.3 seconds
    private static final double MOVE_INTERVAL = 0.3;

    // How much time has passed since the robot last tried to plant
    private double timeSinceLastPlantCheck = 0.0;
    // Robot attempts to plant a seed every 2 seconds
    private static final double PLANT_INTERVAL = 2.0;

    private static final int MAP_WIDTH  = MapSize.MAP_WIDTH;
    private static final int MAP_HEIGHT = MapSize.MAP_HEIGHT;

    @Override
    public void update(World world, double deltaTime) {

        // Accumulate time since last move and last plant attempt
        timeSinceLastMove       += deltaTime;
        timeSinceLastPlantCheck += deltaTime;

        boolean anyMoved            = false;
        boolean shouldCheckPlanting = timeSinceLastPlantCheck >= PLANT_INTERVAL;

        for (EntityID entity : world.getEntitiesWith(RobotComponent.class)) {
            PositionComponent robotPos = (PositionComponent) world.GetComponent(entity, PositionComponent.class);
            RobotComponent robot = (RobotComponent) world.GetComponent(entity, RobotComponent.class);

            double effectiveMoveInterval = MOVE_INTERVAL;
            if (world.hasComponent(entity, SpeedToolComponent.class)) {
                SpeedToolComponent speedTool = (SpeedToolComponent) world.GetComponent(entity, SpeedToolComponent.class);
                effectiveMoveInterval = MOVE_INTERVAL / speedTool.getSpeedMultiplier();
            }

            if (timeSinceLastMove >= effectiveMoveInterval) {
                if (world.hasComponent(entity, PathComponent.class)) {
                    PathComponent path = (PathComponent) world.GetComponent(entity, PathComponent.class);
                    followPath(world, robotPos, path);
                }
                anyMoved = true;
            }
            if (shouldCheckPlanting) {
                tryPlantSeed(world, robot, robotPos);
            }
        }
        if (anyMoved)            timeSinceLastMove       = 0.0;
        if (shouldCheckPlanting) timeSinceLastPlantCheck = 0.0;
    }

    // Move the robot one step along its A* path
    private void followPath(World world, PositionComponent robotPos, PathComponent path) {

        // Path queue is empty — PathfindingSystem will refill it next tick
        if (path.isDone()) return;

        Node next = path.peekNext();

        // A* includes the start tile in the path — skip it if the robot is already standing on it
        if (next != null && next.getX() == robotPos.x && next.getY() == robotPos.y) {
            path.pollNext();
            next = path.peekNext();
        }

        // Nothing left to step to
        if (next == null) return;

        if (world.isTileFreeIgnoringRobots(next.getX(), next.getY())) {
            // Step onto the next waypoint
            path.pollNext();
            robotPos.x = next.getX();
            robotPos.y = next.getY();
        } else {
            // Next tile got blocked since the path was computed (e.g. a weed spawned on it)
            // Clear the path so PathfindingSystem recomputes a detour next tick
            path.setPath(java.util.Collections.emptyList());
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
