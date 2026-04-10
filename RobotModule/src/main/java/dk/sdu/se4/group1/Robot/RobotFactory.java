package dk.sdu.se4.group1.Robot;

import dk.sdu.se4.group1.CommonEcs.PositionComponent;
import dk.sdu.se4.group1.CommonEcs.RenderComponent;
import dk.sdu.se4.group1.CommonEcs.World;
import dk.sdu.se4.group1.CommonEcs.EntityID;
import javafx.scene.image.Image;

import java.io.IOException;
import java.io.InputStream;


public class RobotFactory {
    public EntityID createRobot(World world) {
        EntityID robotId = world.createEntity();

        world.addComponent(robotId, new RobotComponent(0,0));
        world.addComponent(robotId, new PositionComponent(5,5));

       //We try to use ur sprite to find our picture of HrFlink
        // if it fails we throw an exception
        try (InputStream spriteStream = RobotFactory.class.getResourceAsStream("/HrFlink.png")) {
            if (spriteStream == null) {
                throw new IllegalArgumentException("Sprite not found: /HrFlink.png");
            }
            //Here we add the render the image from the code above
            //Throws an exception if it fails to load HrFlink
            world.addComponent(robotId, new RenderComponent(new Image(spriteStream)));
        } catch (IOException e) {
            throw new RuntimeException("Failed to close sprite stream", e);
        }
        return robotId;
    }
}
