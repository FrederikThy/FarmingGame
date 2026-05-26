import dk.sdu.se4.group1.CommonEcs.IEntityProcessingService;

module PathfindingModule {
    requires Common.ecs;
    exports dk.sdu.se4.group1.Pathfinding;

    provides IEntityProcessingService
        with dk.sdu.se4.group1.Pathfinding.PathfindingSystem;
}
