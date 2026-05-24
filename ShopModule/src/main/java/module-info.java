import dk.sdu.se4.group1.CommonEcs.IGamePlugin;
import dk.sdu.se4.group1.CommonEcs.IUiPlugin;
import dk.sdu.se4.group1.Shop.ShopPlugin;

module ShopModule {
    exports dk.sdu.se4.group1.Shop;
    requires Common.ecs;
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.base;
    requires java.naming;
    requires jdk.compiler;

    uses dk.sdu.se4.group1.CommonEcs.ICreateRobot;

    provides IGamePlugin with dk.sdu.se4.group1.Shop.ShopGamePlugin;
    provides IUiPlugin with dk.sdu.se4.group1.Shop.ShopUiPlugin;
}