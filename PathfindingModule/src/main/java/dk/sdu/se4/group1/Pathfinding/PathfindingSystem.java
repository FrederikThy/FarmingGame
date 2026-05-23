package dk.sdu.se4.group1.Pathfinding;

import dk.sdu.se4.group1.CommonEcs.Components.PathComponent;
import dk.sdu.se4.group1.CommonEcs.Components.PathfindingUpgradeComponent;
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

// ECS system that decides where each robot should go and fills its PathComponent with a route.
// Algorithm is read live from PathfindingUpgradeComponent — upgrade it in the shop.
// Runs every tick before MovementSystem so paths are ready when the robot tries to move.
public class PathfindingSystem implements EcsSystem {

    private IPathfinding pathfinding = PathfindingAlgorithm.createDefault(); // starts as BFS
    private PathfindingUpgradeComponent.AlgorithmTier cachedTier = PathfindingUpgradeComponent.AlgorithmTier.BFS;
    private final Random random = new Random();

    private static final int MAP_WIDTH  = MapSize.MAP_WIDTH;
    private static final int MAP_HEIGHT = MapSize.MAP_HEIGHT;
    private static final int GOAL_SEARCH_ATTEMPTS = 50;

    @Override
    public void update(World world, double deltaTime) {
        refreshAlgorithmIfNeeded(world);

        for (EntityID entity : world.getEntitiesWith(PathComponent.class)) {
            if (!world.hasComponent(entity, RobotComponent.class))    continue;
            if (!world.hasComponent(entity, PositionComponent.class)) continue;

            PathComponent     path = (PathComponent)     world.GetComponent(entity, PathComponent.class);
            PositionComponent pos  = (PositionComponent) world.GetComponent(entity, PositionComponent.class);

            if (path.arrived)     continue;
            if (path.pathPending) continue;
            if (!path.isDone())   continue;

            if (path.goalFixed) {
                if (pos.x == path.goalX && pos.y == path.goalY) {
                    path.arrived = true;
                    System.out.println("Robot arrived at goal (" + path.goalX + ", " + path.goalY + ")!");
                    continue;
                }
                computePath(world, path, pos);
                continue;
            }

            int[] goal = selectRandomGoal(world, pos.x, pos.y);
            if (goal == null) continue;

            path.goalX = goal[0];
            path.goalY = goal[1];
            computePath(world, path, pos);
        }
    }

    private void refreshAlgorithmIfNeeded(World world) {
        var upgradeEntities = world.getEntitiesWith(PathfindingUpgradeComponent.class);
        if (upgradeEntities == null || !upgradeEntities.iterator().hasNext()) return;

        PathfindingUpgradeComponent upgrade = (PathfindingUpgradeComponent)
                world.GetComponent(upgradeEntities.iterator().next(), PathfindingUpgradeComponent.class);

        if (upgrade.activeTier != cachedTier) {
            cachedTier  = upgrade.activeTier;
            pathfinding = PathfindingAlgorithm.create(cachedTier);
            System.out.println("[PathfindingSystem] Algorithm switched to: " + cachedTier.displayName);
        }
    }

    private void computePath(World world, PathComponent path, PositionComponent pos) {
        path.pathPending = true;
        List<Node> computed = pathfinding.findPath(
                pos.x, pos.y, path.goalX, path.goalY, MAP_WIDTH, MAP_HEIGHT, world);
        path.setPath(computed);
    }

    private int[] selectRandomGoal(World world, int cx, int cy) {
        for (int i = 0; i < GOAL_SEARCH_ATTEMPTS; i++) {
            int gx = random.nextInt(MAP_WIDTH);
            int gy = random.nextInt(MAP_HEIGHT);
            if (gx == cx && gy == cy) continue;
            if (world.isTileFreeIgnoringRobots(gx, gy)) return new int[]{gx, gy};
        }
        return null;
    }
}
