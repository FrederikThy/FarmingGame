package dk.sdu.se4.group1.Robot;

import dk.sdu.se4.group1.CommonEcs.Components.*;
import dk.sdu.se4.group1.CommonEcs.IEntityProcessingService;
import dk.sdu.se4.group1.CommonEcs.EntityID;
import dk.sdu.se4.group1.CommonEcs.Node;
import dk.sdu.se4.group1.CommonEcs.World;

// Advances each robot one tile along its planned path every MOVE_INTERVAL seconds.
// PathfindingSystem fills PathComponent.remainingPath each time the queue runs out;
// this system just pops the next waypoint and updates PositionComponent to match.
public class MovementSystem implements IEntityProcessingService {

    // How often (in seconds) each robot takes one step — tune this to control speed
    private static final double MOVE_INTERVAL = 0.3;

    @Override
    public void update(World world, double deltaTime) {

        // Process every entity that has both a path and a position
        for (EntityID entity : world.getEntitiesWith(MovementIComponentService.class)) {
            if (!world.hasComponent(entity, PathIComponentService.class))     continue;
            if (!world.hasComponent(entity, PositionIComponentService.class)) continue;
            MovementIComponentService movement = (MovementIComponentService) world.GetComponent(entity, MovementIComponentService.class);
            PathIComponentService path = (PathIComponentService)     world.GetComponent(entity, PathIComponentService.class);
            PositionIComponentService pos  = (PositionIComponentService) world.GetComponent(entity, PositionIComponentService.class);

            // Check if robot has the speed tool and adjust MOVE_INTERVAL
            double effectiveMoveInterval = MOVE_INTERVAL;
            if (world.hasComponent(entity, SpeedToolIComponentService.class)) {
                SpeedToolIComponentService speedTool = (SpeedToolIComponentService) world.GetComponent(entity, SpeedToolIComponentService.class);
                effectiveMoveInterval = MOVE_INTERVAL / speedTool.getSpeedMultiplier();
            }

            // Timer for each robot
            movement.timeSinceLastMove += deltaTime;
            if (movement.timeSinceLastMove < effectiveMoveInterval) continue;
            movement.timeSinceLastMove = 0.0;

            // Pop the next waypoint and move the robot there
            Node next = path.pollNext();
            if (next != null) {
                pos.x = next.getX();
                pos.y = next.getY();
            }
        }
    }


}