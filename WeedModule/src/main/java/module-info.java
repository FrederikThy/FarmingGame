module WeedModule {
    requires Common.ecs;
    requires javafx.graphics;

    exports dk.sdu.se4.group1.Weed;

    provides dk.sdu.se4.group1.CommonEcs.EcsSystem
        with dk.sdu.se4.group1.Weed.WeedSystem;
}