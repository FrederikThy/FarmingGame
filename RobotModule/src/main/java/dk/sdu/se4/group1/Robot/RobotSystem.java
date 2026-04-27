package dk.sdu.se4.group1.Robot;

import dk.sdu.se4.group1.CommonEcs.EcsSystem;
import dk.sdu.se4.group1.CommonEcs.World;

// Coordinator system for robot behaviour.
// Movement is handled by MovementSystem (path-following via PathComponent).
// Specialised actions are handled by HarvestingSystem, PlantingSystem, and RemoveWeedSystem.
// This class exists so Main can register a RobotSystem in the expected order;
public class RobotSystem implements EcsSystem {

    @Override
    public void update(World world, double deltaTime) {
        // Intentionally empty — robot behaviour is split across dedicated systems:
        //   PathfindingSystem  → computes routes and fills PathComponent
        //   MovementSystem     → steps robots along their PathComponent waypoints
        //   HarvestingSystem   → harvests crops adjacent to HarvestingRobots
        //   PlantingSystem     → plants seeds near PlantingRobots
        //   RemoveWeedSystem   → removes weeds near RemoveWeedRobots
    }
}
