package dk.sdu.se4.group1.CommonEcs;

public interface EcsSystem{
    void update(World world, double deltaTime);

    /**
     * Lower number = runs first. Override in systems that must execute in a specific order.
     * PathfindingSystem uses -10 so it always runs before MovementSystem (0).
     */
    default int priority() { return 0; }
}

