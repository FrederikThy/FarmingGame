import dk.sdu.se4.group1.CommonEcs.IGamePlugin;
import dk.sdu.se4.group1.CommonEcs.ToolSPI;
import dk.sdu.se4.group1.Tools.ToolGamePlugin;

module ToolsModule {
    exports dk.sdu.se4.group1.Tools;
    requires Common.ecs;
    requires javafx.controls;
    requires javafx.graphics;

    provides IGamePlugin with ToolGamePlugin;
    provides ToolSPI
            with dk.sdu.se4.group1.Tools.SpeedToolImpl,
                    dk.sdu.se4.group1.Tools.HarvestingToolImpl,
                    dk.sdu.se4.group1.Tools.PlantingToolImpl;
}