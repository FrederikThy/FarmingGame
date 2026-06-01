package dk.sdu.se4.group1.Pathfinding;

import dk.sdu.se4.group1.CommonEcs.Components.PathfindingUpgradeIComponentService;
import dk.sdu.se4.group1.CommonEcs.EntityID;
import dk.sdu.se4.group1.CommonEcs.IGamePlugin;
import dk.sdu.se4.group1.CommonEcs.World;

public class PathfindingGamePlugin implements IGamePlugin {

    @Override
    public void start(World world) {
        EntityID entity = world.createEntity();
        world.addComponent(entity, new PathfindingUpgradeIComponentService());
    }

    @Override
    public void stop(World world) {
    }
}
