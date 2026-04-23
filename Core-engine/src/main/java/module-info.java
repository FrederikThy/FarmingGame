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
    requires WeedModule;
    requires CropsModule;

    requires RobotModule;
    requires MonitoringModule;
    requires ShopModule;
    requires InventoryModule;
    exports dk.sdu.se4.group1.CoreEngine;
}

