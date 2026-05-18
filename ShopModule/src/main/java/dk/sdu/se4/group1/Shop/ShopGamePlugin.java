package dk.sdu.se4.group1.Shop;

import dk.sdu.se4.group1.CommonEcs.IGamePlugin;
import dk.sdu.se4.group1.CommonEcs.World;

// Used for creating the shop from the shop factory
public class ShopGamePlugin implements IGamePlugin {

    ShopFactory shopFactory =  new ShopFactory();
    @Override
    public void start(World world){
        shopFactory.createShop(world);
    }

    @Override
    public void stop(World world){

    }
}
