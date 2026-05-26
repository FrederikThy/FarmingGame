package dk.sdu.se4.group1.Robot;

import dk.sdu.se4.group1.CommonEcs.Components.*;
import dk.sdu.se4.group1.CommonEcs.RobotSPI;
import dk.sdu.se4.group1.CommonEcs.World;
import dk.sdu.se4.group1.CommonEcs.EntityID;
import javafx.scene.image.Image;

import java.io.IOException;
import java.io.InputStream;


public class RobotSPIFactory implements RobotSPI {

    public EntityID BaseRobot(World world, int startX, int startY, int goalX, int goalY, String spritePath, RobotType robotType) {
        EntityID robotId = world.createEntity();
        world.addComponent(robotId, new RobotIComponentService(0,0, robotType));
        world.addComponent(robotId, new PositionIComponentService(startX,startY));
        world.addComponent(robotId, new MovementIComponentService());

        PathIComponentService path = new PathIComponentService();
        path.goalX     = goalX;
        path.goalY     = goalY;
        path.goalFixed = true; // travel to this exact goal, don't pick a random one
        world.addComponent(robotId, path);

        try (InputStream spriteStream = RobotSPIFactory.class.getResourceAsStream(spritePath)) {
            if (spriteStream == null) {
                throw new IllegalArgumentException("Sprite not found: " + spritePath);
            }
            //Here we add the render the image from the code above
            //Throws an exception if it fails to load HrFlink
            world.addComponent(robotId, new RenderIComponentService(new Image(spriteStream)));
        } catch (IOException e) {
            throw new RuntimeException("Failed to close sprite stream", e);
        }

        return robotId;
    }

    public EntityID HarvestingRobot(World world, int startX, int startY, int goalX, int goalY) {
        EntityID robotId = BaseRobot(world, startX, startY, goalX, goalY, "/HrFlink_1.png", RobotType.HARVEST);
        world.addComponent(robotId, new HarvestingIComponentService());
        return robotId;
    }

    public EntityID PlantingRobot(World world, int startX, int startY, int goalX, int goalY) {
        EntityID robotId = BaseRobot(world, startX, startY, goalX, goalY, "/HrFlink_2.png",  RobotType.PLANT);
        world.addComponent(robotId, new PlantingIComponentService());
        return robotId;
    }

    public EntityID RemoveWeedRobot(World world, int startX, int startY, int goalX, int goalY) {
        EntityID robotId = BaseRobot(world, startX, startY, goalX, goalY, "/HrFlink_3.png", RobotType.WEED_REMOVER);
        world.addComponent(robotId, new RemoveCropIComponentService());
        return robotId;
    }

    // Checks the robotType. Calls the method to that robotType.
    @Override
    public EntityID createRobot(World world, RobotType robotType, int startX, int startY, int goalX, int goalY) {
        return switch (robotType){
            case PLANT ->  PlantingRobot(world, startX, startY, goalX, goalY);
            case HARVEST ->   HarvestingRobot(world, startX, startY, goalX, goalY);
            case WEED_REMOVER ->   RemoveWeedRobot(world, startX, startY, goalX, goalY);
        };
    }
}
