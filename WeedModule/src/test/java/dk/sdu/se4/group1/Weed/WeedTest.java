package dk.sdu.se4.group1.Weed;

import dk.sdu.se4.group1.CommonEcs.Components.PositionComponent;
import dk.sdu.se4.group1.CommonEcs.Components.WeedComponent;
import dk.sdu.se4.group1.CommonEcs.EntityID;
import dk.sdu.se4.group1.CommonEcs.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

class WeedTest {

    private World world;

    @BeforeEach
    void setUp() {
        world = new World();
    }

    /** Test to see if weed occupies 1 tile */
    @Test
    void weedOccupies1Tile() {
        EntityID weed = world.createEntity();
        world.addComponent(weed, new WeedComponent());
        world.addComponent(weed, new PositionComponent(3, 7));

        assertFalse(world.isTileFree(3, 7), "The tile is occupied with weed");
        assertTrue(world.isTileFree(3, 8),  "Tiles around the weed should remain free");
        assertTrue(world.isTileFree(3, 6),  "Tiles around the weed should remain free");
        assertTrue(world.isTileFree(2, 7),  "Tiles around the weed should remain free");
        assertTrue(world.isTileFree(4, 7),  "Tiles around the weed should remain free");
        assertTrue(world.isTileFree(2, 6),  "Tiles around the weed should remain free");
        assertTrue(world.isTileFree(4, 6),  "Tiles around the weed should remain free");
        assertTrue(world.isTileFree(2, 8),  "Tiles around the weed should remain free");
        assertTrue(world.isTileFree(4, 8),  "Tiles around the weed should remain free");
    }

    /** Tile must be free when weed is removed */
    @Test
    void removingWeedTest() {
        EntityID weed = world.createEntity();
        world.addComponent(weed, new WeedComponent());
        world.addComponent(weed, new PositionComponent(2, 2));
        world.RemoveEntity(weed);
        assertTrue(world.isTileFree(2, 2), "Tile should be free");
    }

    /** Test that getEntitiesWith(WeedComponent.class) only returns weed entities */
    @Test
    void getEntitiesWith_returnsOnlyWeedEntities() {
        EntityID weed1 = world.createEntity();
        world.addComponent(weed1, new WeedComponent());
        world.addComponent(weed1, new PositionComponent(1, 1));

        EntityID weed2 = world.createEntity();
        world.addComponent(weed2, new WeedComponent());
        world.addComponent(weed2, new PositionComponent(9, 9));

        EntityID robot = world.createEntity();
        world.addComponent(robot, new PositionComponent(0, 0));
        Set<EntityID> weeds = world.getEntitiesWith(WeedComponent.class);

        assertEquals(2, weeds.size(), "Only 2 weeds exist");
        assertTrue(weeds.contains(weed1));
        assertTrue(weeds.contains(weed2));
    }
}