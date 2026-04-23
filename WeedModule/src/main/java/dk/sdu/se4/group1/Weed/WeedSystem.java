package dk.sdu.se4.group1.Weed;

import dk.sdu.se4.group1.CommonEcs.EcsSystem;
import dk.sdu.se4.group1.CommonEcs.MapSize;
import dk.sdu.se4.group1.CommonEcs.World;

import java.util.Random;

public class WeedSystem implements EcsSystem {
    private double timePassed = 0;
    private double spawnChance = 0.40;

    private final int mapHeight = MapSize.MAP_HEIGHT;
    private final int mapWidth = MapSize.MAP_WIDTH;

    private final Random random = new Random();

    public void update(World world, double deltaTime){

            timePassed = timePassed + deltaTime;

            if(timePassed >= 5){
                double n = random.nextDouble();

                if(n > spawnChance){
                    timePassed = 0;
                }

                if (n <= spawnChance){
                    int x = random.nextInt(mapWidth);
                    int y = random.nextInt(mapHeight);

                    if(world.isTileFree(x, y)){
                        WeedFactory.CreateWeed(world, x, y);
                        timePassed = 0;
                    }
                }
            }
    }
}
