package dk.sdu.se4.group1.CommonEcs;

import dk.sdu.se4.group1.CommonEcs.Components.PositionIComponentService;
import dk.sdu.se4.group1.CommonEcs.Components.WeedIComponentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;

class WorldTest {

    private World world;

    @BeforeEach
    void setUp() {
        world = new World();
    }

    /** Unique entity test */
    @Test
    void UniqueIdTest() {
        EntityID a = world.createEntity();
        EntityID b = world.createEntity();
        EntityID c = world.createEntity();

        assertNotEquals(a.id(), b.id(), "Each entity must have a Distinct ID");
        assertNotEquals(b.id(), c.id(), "Each entity must have a Distinct ID");
        assertNotEquals(a.id(), c.id(), "Each entity must have a Distinct ID");
        assertEquals(3, world.getEntities().size(), "World should have exactly 3 entities");
    }

    /** Test to see if returned entity is the same as instantiated one */
    @Test
    void AddAndGetComponentTest() {
        EntityID entity = world.createEntity();
        PositionIComponentService position = new PositionIComponentService(5, 10);

        world.addComponent(entity, position);
        PositionIComponentService retrieved = (PositionIComponentService) world.GetComponent(entity, PositionIComponentService.class);

        assertSame(position, retrieved, "should return the same instance that was added");
        assertEquals(5,  retrieved.x);
        assertEquals(10, retrieved.y);
    }

    /** Test to see if removed entity still exists */
    @Test
    void RemoveEntityTest() {
        EntityID entity = world.createEntity();
        world.addComponent(entity, new PositionIComponentService(1, 1));
        world.addComponent(entity, new WeedIComponentService());
        world.RemoveEntity(entity);
        assertFalse(world.getEntities().contains(entity), "Removed entity should no longer exist");
    }
}
