package dk.sdu.se4.group1.Map;

import dk.sdu.se4.group1.CommonEcs.IGamePlugin;
import dk.sdu.se4.group1.CommonEcs.World;

public class MapPlugin implements IGamePlugin {
    MapFactory mapFactory =  new MapFactory();

    @Override
    public void start(World world){
        mapFactory.createGrowthMap(world);
    }
    @Override
    public void stop(World world){

    }
}
