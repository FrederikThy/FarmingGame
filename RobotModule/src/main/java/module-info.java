import dk.sdu.se4.group1.CommonEcs.RobotSPI;
import dk.sdu.se4.group1.CommonEcs.IEntityProcessingService;
import dk.sdu.se4.group1.CommonEcs.IGamePlugin;
import dk.sdu.se4.group1.Robot.RobotSPIFactory;

module RobotModule
{
    // Har tilføjet Common-Ecs som dependency i pom, så vi kan bruge den
    requires Common.ecs;
    requires java.desktop;
    requires javafx.graphics;

    exports dk.sdu.se4.group1.Robot;

    provides IEntityProcessingService
        with dk.sdu.se4.group1.Robot.MovementSystem,
             dk.sdu.se4.group1.Robot.HarvestingSystem,
             dk.sdu.se4.group1.Robot.PlantingSystem,
             dk.sdu.se4.group1.Robot.RemoveWeedSystem,
             dk.sdu.se4.group1.Robot.RobotTaskSystem;
    provides IGamePlugin with dk.sdu.se4.group1.Robot.RobotPlugin;
    provides RobotSPI with RobotSPIFactory;
}