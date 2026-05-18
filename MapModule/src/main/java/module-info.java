module MapModule {
    requires Common.ecs;
    requires javafx.graphics;
    requires java.desktop;

    exports dk.sdu.se4.group1.Map;

    provides dk.sdu.se4.group1.CommonEcs.IGamePlugin with dk.sdu.se4.group1.Map.MapPlugin;
}

