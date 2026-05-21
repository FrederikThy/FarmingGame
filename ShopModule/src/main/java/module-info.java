module ShopModule {
    exports dk.sdu.se4.group1.Shop;
    requires Common.ecs;
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.base;
    requires java.naming;
    requires jdk.compiler;
    requires RobotModule;

    provides dk.sdu.se4.group1.CommonEcs.IUiPlugin with dk.sdu.se4.group1.Shop.ShopUIPlugin;
    provides dk.sdu.se4.group1.CommonEcs.IGamePlugin with dk.sdu.se4.group1.Shop.ShopGamePlugin;
}


