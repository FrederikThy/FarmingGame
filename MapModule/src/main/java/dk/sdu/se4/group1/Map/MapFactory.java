package dk.sdu.se4.group1.Map;

import dk.sdu.se4.group1.CommonEcs.EntityID;
import dk.sdu.se4.group1.CommonEcs.PositionComponent;
import dk.sdu.se4.group1.CommonEcs.TileComponent;
import dk.sdu.se4.group1.CommonEcs.World;

public class MapFactory {
    
    // makes the map avalaible to the MappingSystem by creating an entity 
    // for each tile and giving it a PositionComponent and TileComponent.
    public static void registerTiles(World world, int rows, int cols) {
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                EntityID tileEntity = world.createEntity();
                world.addComponent(tileEntity, new PositionComponent(x, y));
                world.addComponent(tileEntity, new TileComponent());  
            }
        }
    }
}