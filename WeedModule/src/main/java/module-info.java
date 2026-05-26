import dk.sdu.se4.group1.CommonEcs.IEntityProcessingService;

module WeedModule {
    requires Common.ecs;
    requires javafx.graphics;

    exports dk.sdu.se4.group1.Weed;

    provides IEntityProcessingService
        with dk.sdu.se4.group1.Weed.WeedSystem;
}