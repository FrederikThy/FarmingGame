module Core.engine {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.base;
    requires spring.context;
    requires spring.core;
    requires spring.beans;
    requires spring.aop;
    requires spring.expression;
    requires Common.ecs;
    requires MapModule;
    requires InventoryModule;
    requires ShopModule;
    requires RobotModule;
    uses dk.sdu.se4.group1.CommonEcs.IInventoryService;
    uses dk.sdu.se4.group1.CommonEcs.IShopService;
    exports dk.sdu.se4.group1.CoreEngine;
}

