package dk.sdu.se4.group1.Robot;

import dk.sdu.se4.group1.CommonEcs.World;
import dk.sdu.se4.group1.CommonEcs.EntityID;

public class RobotFactory {
    public EntityID createRobot(World world, int mapHeight, int mapLength) {
        EntityID robotId = world.createEntity();

        world.addComponent(robotId, new RobotComponent(mapLength, mapHeight));

        return robotId;
    }
}
