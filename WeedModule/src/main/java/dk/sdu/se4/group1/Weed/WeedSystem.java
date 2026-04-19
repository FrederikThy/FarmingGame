package dk.sdu.se4.group1.Weed;

import dk.sdu.se4.group1.CommonEcs.EcsSystem;
import dk.sdu.se4.group1.CommonEcs.World;

public class WeedSystem implements EcsSystem {

    private boolean hasSpawned = false;
    public void update(World world, double deltaTime){

        if(!hasSpawned){
            hasSpawned = true;

            int x = 0;
            int y = 0;

            WeedFactory.CreateWeed(world, x, y);
        }
    }
}
