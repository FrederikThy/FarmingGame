import dk.sdu.se4.group1.CommonEcs.*;
import dk.sdu.se4.group1.Shop.ShopPricingService;
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
    uses ToolSPI;

    provides IShopPricingService with ShopPricingService;
    provides IGamePlugin with dk.sdu.se4.group1.Shop.ShopGamePlugin;
    provides IUiPluginService with ShopUiPluginService;
}