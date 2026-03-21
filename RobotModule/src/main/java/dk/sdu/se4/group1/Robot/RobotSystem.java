package dk.sdu.se4.group1.Robot;

import dk.sdu.se4.group1.CommonEcs.EcsSystem;
import dk.sdu.se4.group1.CommonEcs.World;

// Vi gennemgår alle entities i World.
// Tjekker om det er en robot ved hjælp af if (entity insanceof RobotPlugin robot)
// Kalder robot.moveRandomly, for at opdatere robottens position
public class RobotSystem implements EcsSystem {
    @Override
    public void update(World world, double deltaTime) {
        for (Object entity : world.getEntities()) {
            if (entity instanceof RobotPlugin robot) {
                robot.moveRandomly();
            }
        }
    }
}