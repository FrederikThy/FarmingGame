package dk.sdu.se4.group1.Robot;

import java.util.Random;
import dk.sdu.se4.group1.CommonEcs.EcsSystem;
import dk.sdu.se4.group1.CommonEcs.EntityID;
import dk.sdu.se4.group1.CommonEcs.PositionComponent;
import dk.sdu.se4.group1.CommonEcs.World;

public class RobotSystem implements EcsSystem {

    private final Random random = new Random();
    private double timeSinceLastMove = 0.0; // in seconds
    private static final double MOVE_INTERVAL = 0.3; // move every 1 second

    @Override
    public void update(World world, double deltaTime) {
        timeSinceLastMove += deltaTime;

        // Only move robots if 1 second has passed
        if (timeSinceLastMove >= MOVE_INTERVAL) {
            for (EntityID entity : world.getEntitiesWith(RobotComponent.class)) {

                // Get the position component
                PositionComponent robotPos = (PositionComponent) world.GetComponent(entity, PositionComponent.class);

                // Choose a random direction: 0 = up, 1 = down, 2 = left, 3 = right
                int direction = random.nextInt(4);

                switch (direction) {
                    case 0: // up
                        robotPos.y = Math.max(0, robotPos.y - 1);
                        break;
                    case 1: // down
                        robotPos.y = Math.min(10 - 1, robotPos.y + 1);
                        break;
                    case 2: // left
                        robotPos.x = Math.max(0, robotPos.x - 1);
                        break;
                    case 3: // right
                        robotPos.x = Math.min(10 - 1, robotPos.x + 1);
                        break;
                }
            }

            // Reset timer
            timeSinceLastMove = 0.0;
        }
    }
}