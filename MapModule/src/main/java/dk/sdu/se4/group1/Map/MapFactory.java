package dk.sdu.se4.group1.Map;

import dk.sdu.se4.group1.CommonEcs.Components.GrowthMapComponent;
import dk.sdu.se4.group1.CommonEcs.EntityID;
import dk.sdu.se4.group1.CommonEcs.World;

public class MapFactory {

    public static EntityID createGrowthMap(World world) {
        EntityID progressionId = world.createEntity();
        world.addComponent(progressionId, new GrowthMapComponent());
        return progressionId;
    }
}
