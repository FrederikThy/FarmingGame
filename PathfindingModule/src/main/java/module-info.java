module PathfindingModule {
    requires Common.ecs;

    exports dk.sdu.se4.group1.Pathfinding;

    provides dk.sdu.se4.group1.CommonEcs.EcsSystem with dk.sdu.se4.group1.Pathfinding.PathfindingSystem;
}
