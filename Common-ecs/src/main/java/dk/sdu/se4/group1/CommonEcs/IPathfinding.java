package dk.sdu.se4.group1.CommonEcs;

import java.util.List;

/**
 * Contract for A* pathfinding.
 * Lives in Common-ecs so both RobotModule and PathfindingModule share the type
 * without a circular dependency.
 */
public interface IPathfinding {
    /**
     * Compute the shortest walkable path on a 4-directional grid.
     * Returns an ordered list of Nodes from start (inclusive) to goal (inclusive),
     * or an empty list when no path exists.
     */
    List<Node> findPath(int startX, int startY,
                        int goalX,  int goalY,
                        int mapWidth, int mapHeight,
                        World world);
}
