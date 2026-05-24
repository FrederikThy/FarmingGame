module CropsModule {
    requires Common.ecs;
    requires javafx.graphics;

    exports dk.sdu.se4.group1.Crops;

    provides dk.sdu.se4.group1.CommonEcs.EcsSystem
            with dk.sdu.se4.group1.Crops.cropSystem,
                    dk.sdu.se4.group1.Crops.IntercroppingSystem,
                    dk.sdu.se4.group1.Crops.RainOverlaySystem;
}