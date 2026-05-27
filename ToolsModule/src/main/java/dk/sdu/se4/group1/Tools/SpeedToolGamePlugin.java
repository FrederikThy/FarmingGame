package dk.sdu.se4.group1.Tools;

import dk.sdu.se4.group1.CommonEcs.IGamePlugin;
import dk.sdu.se4.group1.CommonEcs.World;

public class SpeedToolGamePlugin implements IGamePlugin {

    @Override
    public void start(World world) {
        SpeedToolFactory.registerInShop(world);
    }

    @Override
    public void stop(World world) {
    }
}
