package dk.sdu.se4.group1.Robot;

import java.util.Random;
import dk.sdu.se4.group1.CommonEcs.EcsSystem;
import dk.sdu.se4.group1.CommonEcs.EntityID;
import dk.sdu.se4.group1.CommonEcs.PositionComponent;
import dk.sdu.se4.group1.CommonEcs.World;

public class RobotSystem implements EcsSystem {

    private final Random random = new Random();
    private double timeSinceLastMove = 0.0;
    private static final double MOVE_INTERVAL = 0.3;

    @Override
    public void update(World world, double deltaTime) {
        timeSinceLastMove += deltaTime;

        if (timeSinceLastMove >= MOVE_INTERVAL) {
            for (EntityID entity : world.getEntitiesWith(RobotComponent.class)) {

                PositionComponent robotPos =
                        (PositionComponent) world.GetComponent(entity, PositionComponent.class);

                int targetX = robotPos.x;
                int targetY = robotPos.y;

                int direction = random.nextInt(4);

                switch (direction) {
                    case 0: // up
                        targetY = Math.max(0, robotPos.y - 1);
                        break;
                    case 1: // down
                        targetY = Math.min(10 - 1, robotPos.y + 1);
                        break;
                    case 2: // left
                        targetX = Math.max(0, robotPos.x - 1);
                        break;
                    case 3: // right
                        targetX = Math.min(10 - 1, robotPos.x + 1);
                        break;
                }

                // Only move if the target tile is free
                if (world.isTileFree(targetX, targetY)) {
                    robotPos.x = targetX;
                    robotPos.y = targetY;
                }
            }

            timeSinceLastMove = 0.0;
        }
    }
}

