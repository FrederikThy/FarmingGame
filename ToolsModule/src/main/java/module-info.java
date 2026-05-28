import dk.sdu.se4.group1.CommonEcs.IUiPluginService;

module ToolsModule {
    exports dk.sdu.se4.group1.Tools;
    requires Common.ecs;
    requires javafx.controls;
    requires javafx.graphics;
    provides IUiPluginService with dk.sdu.se4.group1.Tools.SpeedToolUiPluginService;
}
