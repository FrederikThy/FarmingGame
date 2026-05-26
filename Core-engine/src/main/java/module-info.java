import dk.sdu.se4.group1.CommonEcs.IEntityProcessingService;
import dk.sdu.se4.group1.CommonEcs.IMapService;
import dk.sdu.se4.group1.CommonEcs.IUiPluginService;

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
    uses IEntityProcessingService;
    uses dk.sdu.se4.group1.CommonEcs.IGamePlugin;
    uses IUiPluginService;
    uses IMapService;
}
