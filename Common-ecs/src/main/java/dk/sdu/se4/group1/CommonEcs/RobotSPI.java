package dk.sdu.se4.group1.CommonEcs;

import dk.sdu.se4.group1.CommonEcs.Components.RobotType;

public interface RobotSPI {

    // Implemented by RobotFactory. Used in ShopPlugin for buying a robot in shop
    EntityID createRobot(World world, RobotType robotType, int startX, int startY, int goalX, int goalY);
}
