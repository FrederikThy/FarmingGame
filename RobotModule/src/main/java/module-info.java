module RobotModule
{
    // Har tilføjet Common-Ecs som dependency i pom, så vi kan bruge den
    requires Common.ecs;
    requires java.desktop;
    requires javafx.graphics;

    exports dk.sdu.se4.group1.Robot;
}