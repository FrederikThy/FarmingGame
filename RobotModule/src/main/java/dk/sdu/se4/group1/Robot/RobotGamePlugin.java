package dk.sdu.se4.group1.Robot;

import dk.sdu.se4.group1.CommonEcs.IGamePlugin;
import dk.sdu.se4.group1.CommonEcs.World;

/**
 * Game initialization plugin for Robot module.
 * Creates initial robots in the game world.
 */
public class RobotGamePlugin implements IGamePlugin {

    @Override
    public void start(World world) {
        // Create initial robots
        RobotFactory factory = new RobotFactory();
        // Create a harvesting robot at starting position
        factory.HarvestingRobot(world, 1, 1, 5, 5);
        // Create a planting robot
        factory.PlantingRobot(world, 2, 2, 6, 6);
    }

    @Override
    public void stop(World world) {
        // Cleanup if needed
    }
}

