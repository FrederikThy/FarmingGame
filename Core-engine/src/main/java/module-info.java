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
    exports dk.sdu.se4.group1.CoreEngine;
    uses dk.sdu.se4.group1.CommonEcs.EcsSystem;
    uses dk.sdu.se4.group1.CommonEcs.IGamePlugin;
    uses dk.sdu.se4.group1.CommonEcs.IUiPlugin;
    uses dk.sdu.se4.group1.CommonEcs.IRenderSystem;
}
