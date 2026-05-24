import dk.sdu.se4.group1.CommonEcs.ICreateRobot;
import dk.sdu.se4.group1.CommonEcs.IGamePlugin;

module RobotModule
{
    // Har tilføjet Common-Ecs som dependency i pom, så vi kan bruge den
    requires Common.ecs;
    requires java.desktop;
    requires javafx.graphics;

    exports dk.sdu.se4.group1.Robot;

    provides dk.sdu.se4.group1.CommonEcs.EcsSystem
        with dk.sdu.se4.group1.Robot.MovementSystem,
             dk.sdu.se4.group1.Robot.HarvestingSystem,
             dk.sdu.se4.group1.Robot.PlantingSystem,
             dk.sdu.se4.group1.Robot.RemoveWeedSystem,
             dk.sdu.se4.group1.Robot.RobotTaskSystem;
    provides IGamePlugin with dk.sdu.se4.group1.Robot.RobotPlugin;
    provides ICreateRobot with dk.sdu.se4.group1.Robot.RobotFactory;
}