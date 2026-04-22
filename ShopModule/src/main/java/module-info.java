module ShopModule {
    exports dk.sdu.se4.group1.Shop;
    requires Common.ecs;
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.base;
    requires java.naming;
    requires jdk.compiler;

    provides dk.sdu.se4.group1.CommonEcs.IShopService
            with dk.sdu.se4.group1.Shop.ShopPlugin;
}