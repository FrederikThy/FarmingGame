package dk.sdu.se4.group1.Crops;

import dk.sdu.se4.group1.CommonEcs.IGamePlugin;
import dk.sdu.se4.group1.CommonEcs.World;

/**
 * Game initialization plugin for Crops module.
 * Initializes any required crop entities or setup at game start.
 */
public class CropsGamePlugin implements IGamePlugin {

    @Override
    public void start(World world) {
        // Crops module is initialized through systems, not plugins
        // This plugin exists to enable ServiceLoader discovery
    }

    @Override
    public void stop(World world) {
        // Cleanup if needed
    }
}

