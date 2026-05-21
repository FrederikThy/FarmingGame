package dk.sdu.se4.group1.Map;

import dk.sdu.se4.group1.CommonEcs.IGamePlugin;
import dk.sdu.se4.group1.CommonEcs.World;

/**
 * Game initialization plugin for Map module.
 */
public class MapGamePlugin implements IGamePlugin {

    @Override
    public void start(World world) {
        // Initialize map
        MapFactory.createGrowthMap(world);
    }

    @Override
    public void stop(World world) {
        // Cleanup if needed
    }
}

