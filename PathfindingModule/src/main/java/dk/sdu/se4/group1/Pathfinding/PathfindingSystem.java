package dk.sdu.se4.group1.Pathfinding;

import java.util.List;

import dk.sdu.se4.group1.CommonEcs.EcsSystem;
import dk.sdu.se4.group1.CommonEcs.EntityID;
import dk.sdu.se4.group1.CommonEcs.PositionComponent;
import dk.sdu.se4.group1.CommonEcs.World;

public class PathfindingSystem implements EcsSystem {

    private final AStar aStar = new AStar();
    private double timeSinceLastMove = 0.0;
    private static final double MOVE_INTERVAL = 0.4; // seconds per step

    @Override
    public void update(World world, double deltaTime) {
        timeSinceLastMove += deltaTime;
        if (timeSinceLastMove < MOVE_INTERVAL) return;
        timeSinceLastMove = 0.0;

        // Process every entity that has both a position and an active pathfinding goal
        for (EntityID entity : world.getEntitiesWith(PathfindingComponent.class)) {

            PositionComponent pos =
                (PositionComponent) world.GetComponent(entity, PositionComponent.class);

            PathfindingComponent pfc =
                (PathfindingComponent) world.GetComponent(entity, PathfindingComponent.class);

            if (!pfc.hasTarget()) continue;

            // If no path yet (or target changed), calculate one
            if (!pfc.hasPath()) {
                List<PositionComponent> newPath =
                    aStar.findPath(world, pos, pfc.getTarget());

                if (newPath == null || newPath.isEmpty()) continue; // No path exists

                pfc.setPath(newPath);
            }

            // Move one step along the path
            PositionComponent nextStep = pfc.pollNextStep();
            if (nextStep != null) {
                pos.x = nextStep.getX();
                pos.y = nextStep.getY();
            }

            // Clear target once reached
            if (!pfc.hasPath()) {
                pfc.setTarget(null);
            }
        }
    }
}