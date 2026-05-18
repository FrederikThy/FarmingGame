package dk.sdu.se4.group1.Robot;

import dk.sdu.se4.group1.CommonEcs.IGamePlugin;
import dk.sdu.se4.group1.CommonEcs.World;

public class RobotPlugin implements IGamePlugin {
    RobotFactory robotFactory = new RobotFactory();
    @Override
    public void start(World world) {
        robotFactory.HarvestingRobot(world, 1, 1, 2, 2);
        robotFactory.PlantingRobot(world, 2, 2, 9, 9);
        robotFactory.RemoveWeedRobot(world, 3, 3, 2, 9);
    }

    @Override
    public void stop(World world) {

    }
}
