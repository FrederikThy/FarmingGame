package dk.sdu.se4.group1.Weed;

import dk.sdu.se4.group1.CommonEcs.IGamePlugin;
import dk.sdu.se4.group1.CommonEcs.World;

/**
 * Game initialization plugin for Weed module.
 */
public class WeedGamePlugin implements IGamePlugin {

    @Override
    public void start(World world) {
        // Weed module is initialized through systems
    }

    @Override
    public void stop(World world) {
        // Cleanup if needed
    }
}

