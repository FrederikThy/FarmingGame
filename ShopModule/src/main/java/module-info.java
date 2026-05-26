import dk.sdu.se4.group1.CommonEcs.IGamePlugin;
import dk.sdu.se4.group1.CommonEcs.RobotSPI;
import dk.sdu.se4.group1.CommonEcs.IUiPluginService;
import dk.sdu.se4.group1.Shop.ShopUiPluginService;

module ShopModule {
    exports dk.sdu.se4.group1.Shop;
    requires Common.ecs;
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.base;
    requires java.naming;
    requires jdk.compiler;

    uses RobotSPI;

    provides IGamePlugin with dk.sdu.se4.group1.Shop.ShopGamePlugin;
    provides IUiPluginService with ShopUiPluginService;
}