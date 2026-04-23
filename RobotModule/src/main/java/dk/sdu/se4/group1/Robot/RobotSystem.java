package dk.sdu.se4.group1.Robot;

import java.util.Random;

import dk.sdu.se4.group1.CommonApi.SeedType;
import dk.sdu.se4.group1.CommonEcs.Components.PathComponent;
import dk.sdu.se4.group1.CommonEcs.Components.RobotComponent;
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

        boolean shouldMove          = timeSinceLastMove       >= MOVE_INTERVAL;
        boolean shouldCheckPlanting = timeSinceLastPlantCheck >= PLANT_INTERVAL;

        for (EntityID entity : world.getEntitiesWith(RobotComponent.class)) {
            PositionComponent robotPos = (PositionComponent) world.GetComponent(entity, PositionComponent.class);
            RobotComponent    robot    = (RobotComponent)    world.GetComponent(entity, RobotComponent.class);

            if (shouldMove) {
                if (world.hasComponent(entity, PathComponent.class)) {
                    // Robot has a computed A* path — follow it one step at a time
                    PathComponent path = (PathComponent) world.GetComponent(entity, PathComponent.class);
                    followPath(world, robotPos, path);
                } else {
                    // No path component — use the old random walk as a fallback
                    moveRobotRandomly(world, robotPos);
                }
            }

            if (shouldCheckPlanting) {
                tryPlantSeed(world, robot, robotPos);
            }
        }

        // Reset timers after processing all robots
        if (shouldMove)          timeSinceLastMove       = 0.0;
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

    // Legacy fallback movement — picks a random direction and moves there if the tile is free
    private void moveRobotRandomly(World world, PositionComponent robotPos) {
        int tx = robotPos.x, ty = robotPos.y;
        switch (random.nextInt(4)) {
            case 0 -> ty = Math.max(0, robotPos.y - 1);              // up
            case 1 -> ty = Math.min(MAP_HEIGHT - 1, robotPos.y + 1); // down
            case 2 -> tx = Math.max(0, robotPos.x - 1);              // left
            case 3 -> tx = Math.min(MAP_WIDTH - 1, robotPos.x + 1);  // right
        }
        if (world.isTileFree(tx, ty)) { robotPos.x = tx; robotPos.y = ty; }
    }

    // 50% chance to plant a seed on a free adjacent tile
    private void tryPlantSeed(World world, RobotComponent robot, PositionComponent robotPos) {
        if (random.nextInt(10) + 1 >= 5) return; // roll fails — skip planting this interval

        // Pick a random seed type and find a free neighbour to plant on
        robot.seedType = SeedType.values()[random.nextInt(SeedType.values().length)];
        int[] tile = findFreeAdjacentTile(world, robotPos.x, robotPos.y);

        // Add the request to the world queue — cropSystem will create the entity next tick
        if (tile != null) world.addSeedToQueue(tile[0], tile[1], robot.seedType);
    }

    // Check all four neighbours in a random order and return the first free tile found
    private int[] findFreeAdjacentTile(World world, int rx, int ry) {
        int[][] dirs = {{0,-1},{0,1},{-1,0},{1,0}};
        int start = random.nextInt(dirs.length); // randomise which direction we check first
        for (int i = 0; i < dirs.length; i++) {
            int[] d = dirs[(start + i) % dirs.length];
            int x = rx + d[0], y = ry + d[1];

            // Skip tiles outside the map or already occupied
            if (x >= 0 && x < MAP_WIDTH && y >= 0 && y < MAP_HEIGHT && world.isTileFree(x, y))
                return new int[]{x, y};
        }
        return null; // all neighbours are occupied or out of bounds
    }
}
