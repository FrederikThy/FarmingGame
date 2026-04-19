package dk.sdu.se4.group1.Robot;

import dk.sdu.se4.group1.CommonEcs.EntityID;
import dk.sdu.se4.group1.CommonEcs.PositionComponent;
import dk.sdu.se4.group1.CommonEcs.RenderComponent;
import dk.sdu.se4.group1.CommonEcs.World;
import dk.sdu.se4.group1.Pathfinding.PathfindingComponent;

public class RobotFactory {

    public static EntityID createRobot(World world, int startX, int startY, int mapLength, int mapHeight, PositionComponent target) {
        EntityID robotId = world.createEntity();

        world.addComponent(robotId, new RobotComponent(mapLength, mapHeight));
        world.addComponent(robotId, new PositionComponent(startX, startY));
        world.addComponent(robotId, new PathfindingComponent(target));
        world.addComponent(robotId, new RenderComponent("GRAY")); // MappingSystem reads this

        return robotId;
    }
}
