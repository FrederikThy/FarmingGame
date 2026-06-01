import dk.sdu.se4.group1.CommonEcs.IEntityProcessingService;
import dk.sdu.se4.group1.CommonEcs.IGamePlugin;

module PathfindingModule {
    requires Common.ecs;
    exports dk.sdu.se4.group1.Pathfinding;

    provides IEntityProcessingService
        with dk.sdu.se4.group1.Pathfinding.PathfindingSystem;
    provides IGamePlugin
        with dk.sdu.se4.group1.Pathfinding.PathfindingGamePlugin;
}
