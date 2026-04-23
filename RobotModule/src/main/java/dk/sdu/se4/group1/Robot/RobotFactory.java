package dk.sdu.se4.group1.Robot;

import dk.sdu.se4.group1.CommonEcs.Components.PathComponent;
import dk.sdu.se4.group1.CommonEcs.Components.RobotComponent;
import dk.sdu.se4.group1.CommonEcs.Components.PositionComponent;
import dk.sdu.se4.group1.CommonEcs.Components.RenderComponent;
import dk.sdu.se4.group1.CommonEcs.World;
import dk.sdu.se4.group1.CommonEcs.EntityID;
import javafx.scene.image.Image;

import java.io.IOException;
import java.io.InputStream;

public class RobotFactory {

    /**
     * Create a robot that travels from point A to point B using A*.
     *
     * @param world  ECS world
     * @param startX column of point A  (0-based tile index)
     * @param startY row    of point A
     * @param goalX  column of point B
     * @param goalY  row    of point B
     */
    public EntityID createRobot(World world, int startX, int startY, int goalX, int goalY) {
        EntityID robotId = world.createEntity();

        world.addComponent(robotId, new RobotComponent(0, 0));
        world.addComponent(robotId, new PositionComponent(startX, startY));

        // Fixed A→B path: PathfindingSystem computes the route once and stops.
        PathComponent path = new PathComponent();
        path.goalX     = goalX;
        path.goalY     = goalY;
        path.goalFixed = true;
        world.addComponent(robotId, path);

        try (InputStream s = RobotFactory.class.getResourceAsStream("/HrFlink.png")) {
            if (s == null) throw new IllegalArgumentException("Sprite not found: /HrFlink.png");
            world.addComponent(robotId, new RenderComponent(new Image(s)));
        } catch (IOException e) {
            throw new RuntimeException("Failed to close sprite stream", e);
        }
        return robotId;
    }
}
