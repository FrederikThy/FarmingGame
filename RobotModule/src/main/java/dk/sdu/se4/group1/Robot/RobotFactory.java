package dk.sdu.se4.group1.Robot;

import dk.sdu.se4.group1.CommonEcs.PositionComponent;
import dk.sdu.se4.group1.CommonEcs.RenderComponent;
import dk.sdu.se4.group1.CommonEcs.World;
import dk.sdu.se4.group1.CommonEcs.EntityID;
import javafx.scene.paint.Color;


public class RobotFactory {
    public EntityID createRobot(World world) {
        EntityID robotId = world.createEntity();

        world.addComponent(robotId, new RobotComponent(0,0));
        world.addComponent(robotId, new PositionComponent(5,5));
        world.addComponent(robotId, new RenderComponent(Color.GRAY));
        return robotId;
    }
}
