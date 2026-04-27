package dk.sdu.se4.group1.Robot;

import dk.sdu.se4.group1.CommonEcs.Components.MovementComponent;
import dk.sdu.se4.group1.CommonEcs.Components.PathComponent;
import dk.sdu.se4.group1.CommonEcs.Components.PositionComponent;
import dk.sdu.se4.group1.CommonEcs.EcsSystem;
import dk.sdu.se4.group1.CommonEcs.EntityID;
import dk.sdu.se4.group1.CommonEcs.Node;
import dk.sdu.se4.group1.CommonEcs.World;

// Advances each robot one tile along its planned path every MOVE_INTERVAL seconds.
// PathfindingSystem fills PathComponent.remainingPath each time the queue runs out;
// this system just pops the next waypoint and updates PositionComponent to match.
public class MovementSystem implements EcsSystem {

    private double timeSinceLastMove = 0.0;

    // How often (in seconds) each robot takes one step — tune this to control speed
    private static final double MOVE_INTERVAL = 0.3;

    @Override
    public void update(World world, double deltaTime) {
        timeSinceLastMove += deltaTime;

        if (timeSinceLastMove < MOVE_INTERVAL) return;
        timeSinceLastMove = 0.0;

        // Process every entity that has both a path and a position
        for (EntityID entity : world.getEntitiesWith(MovementComponent.class)) {
            if (!world.hasComponent(entity, PathComponent.class))     continue;
            if (!world.hasComponent(entity, PositionComponent.class)) continue;

            PathComponent     path = (PathComponent)     world.GetComponent(entity, PathComponent.class);
            PositionComponent pos  = (PositionComponent) world.GetComponent(entity, PositionComponent.class);

            // Robot has arrived at its fixed goal — nothing left to do
            if (path.arrived) continue;

            // Path is empty or still being computed — wait for PathfindingSystem
            if (path.isDone() || path.pathPending) continue;

            // Pop the next waypoint and move the robot there
            Node next = path.pollNext();
            if (next != null) {
                pos.x = next.getX();
                pos.y = next.getY();
            }
        }
    }
}