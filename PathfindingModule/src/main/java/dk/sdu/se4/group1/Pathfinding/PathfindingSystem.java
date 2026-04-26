package dk.sdu.se4.group1.Pathfinding;

import dk.sdu.se4.group1.CommonEcs.Components.PathComponent;
import dk.sdu.se4.group1.CommonEcs.Components.PositionComponent;
import dk.sdu.se4.group1.CommonEcs.Components.RobotComponent;
import dk.sdu.se4.group1.CommonEcs.EcsSystem;
import dk.sdu.se4.group1.CommonEcs.EntityID;
import dk.sdu.se4.group1.CommonEcs.IPathfinding;
import dk.sdu.se4.group1.CommonEcs.MapSize;
import dk.sdu.se4.group1.CommonEcs.Node;
import dk.sdu.se4.group1.CommonEcs.World;

import java.util.List;
import java.util.Random;

// ECS system that decides where each robot should go and fills its PathComponent with an A* route
// Runs every tick before RobotSystem so paths are ready when the robot tries to move
public class PathfindingSystem implements EcsSystem {

    // The algorithm to use — injected so it can be swapped or mocked in tests
    private final IPathfinding pathfinding;
    private final Random random = new Random();

    private static final int MAP_WIDTH  = MapSize.MAP_WIDTH;
    private static final int MAP_HEIGHT = MapSize.MAP_HEIGHT;

    // How many random tiles to try before giving up on finding a free goal this tick
    private static final int GOAL_SEARCH_ATTEMPTS = 50;

    public PathfindingSystem(IPathfinding pathfinding) {
        this.pathfinding = pathfinding;
    }

    @Override
    public void update(World world, double deltaTime) {

        // Only process entities that have a path component and are robots with a position
        for (EntityID entity : world.getEntitiesWith(PathComponent.class)) {
            if (!world.hasComponent(entity, RobotComponent.class))    continue;
            if (!world.hasComponent(entity, PositionComponent.class)) continue;

            PathComponent     path = (PathComponent)     world.GetComponent(entity, PathComponent.class);
            PositionComponent pos  = (PositionComponent) world.GetComponent(entity, PositionComponent.class);

            // Robot already reached its fixed destination — nothing more to do
            if (path.arrived) continue;

            // A path computation is already in flight — wait for it to finish before requesting another
            if (path.pathPending) continue;

            // Still has waypoints to follow — no need to recompute yet
            if (!path.isDone()) continue;

            // Fixed-goal mode: robot was given a specific A→B destination
            if (path.goalFixed) {

                // Check if the robot is already standing on the goal
                if (pos.x == path.goalX && pos.y == path.goalY) {
                    path.arrived = true; // stop processing this robot permanently
                    System.out.println("Robot arrived at goal (" + path.goalX + ", " + path.goalY + ")!");
                    continue;
                }

                // Not there yet — compute the route to the fixed goal
                computePath(world, path, pos);
                continue;
            }

            // Free-roaming mode: pick a new random destination now that the previous path is done
            int[] goal = selectRandomGoal(world, pos.x, pos.y);
            if (goal == null) continue; // no free tile found this tick — try again next tick

            path.goalX = goal[0];
            path.goalY = goal[1];
            computePath(world, path, pos);
        }
    }

    // Run A* from the robot's current position to the goal stored in path
    private void computePath(World world, PathComponent path, PositionComponent pos) {
        path.pathPending = true; // block further requests until this one is stored
        List<Node> computed = pathfinding.findPath(
                pos.x, pos.y, path.goalX, path.goalY, MAP_WIDTH, MAP_HEIGHT, world);
        path.setPath(computed); // stores the waypoints and clears pathPending
    }

    // Sample random tiles until we find one that is free of static obstacles (crops/weeds)
    // Other robots are ignored — they move and shouldn't permanently block goal selection
    private int[] selectRandomGoal(World world, int cx, int cy) {
        for (int i = 0; i < GOAL_SEARCH_ATTEMPTS; i++) {
            int gx = random.nextInt(MAP_WIDTH);
            int gy = random.nextInt(MAP_HEIGHT);

            // Don't pick the tile the robot is already standing on
            if (gx == cx && gy == cy) continue;

            if (world.isTileFreeIgnoringRobots(gx, gy)) return new int[]{gx, gy};
        }
        return null; // couldn't find a free tile — caller will retry next tick
    }
}
