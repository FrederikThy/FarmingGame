package dk.sdu.se4.group1.Robot;

import dk.sdu.se4.group1.CommonEcs.Components.MovementComponent;
import dk.sdu.se4.group1.CommonEcs.Components.PositionComponent;
import dk.sdu.se4.group1.CommonEcs.Components.RobotComponent;
import dk.sdu.se4.group1.CommonEcs.EcsSystem;
import dk.sdu.se4.group1.CommonEcs.EntityID;
import dk.sdu.se4.group1.CommonEcs.World;

import java.util.Random;

public class MovementSystem implements EcsSystem {

    private final Random random = new Random();

    private double timeSinceLastMove = 0.0;
    private static final double MOVE_INTERVAL = 0.3;

    private static final int MAP_WIDTH = 10;
    private static final int MAP_HEIGHT = 10;

    @Override
    public void update(World world, double deltaTime) {

        timeSinceLastMove += deltaTime;

        boolean shouldMove = timeSinceLastMove >= MOVE_INTERVAL;

        if (!shouldMove) return;

        for (EntityID entity : world.getEntitiesWith(RobotComponent.class)) {

            PositionComponent robotPos =
                    (PositionComponent) world.GetComponent(entity, PositionComponent.class);

            moveRobotRandomly(world, robotPos);
        }

        if (shouldMove) {
            timeSinceLastMove = 0.0;
        }
    }

    private void moveRobotRandomly(World world, PositionComponent robotPos) {
        int targetX = robotPos.x;
        int targetY = robotPos.y;

        int direction = random.nextInt(4);

        switch (direction) {
            case 0 -> targetY = Math.max(0, robotPos.y - 1);
            case 1 -> targetY = Math.min(MAP_HEIGHT - 1, robotPos.y + 1);
            case 2 -> targetX = Math.max(0, robotPos.x - 1);
            case 3 -> targetX = Math.min(MAP_WIDTH - 1, robotPos.x + 1);
        }

        if (world.isTileFree(targetX, targetY)) {
            robotPos.x = targetX;
            robotPos.y = targetY;
        }
    }
}