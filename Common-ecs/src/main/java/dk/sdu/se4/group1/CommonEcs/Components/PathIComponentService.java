package dk.sdu.se4.group1.CommonEcs.Components;

import dk.sdu.se4.group1.CommonEcs.IComponentService;
import dk.sdu.se4.group1.CommonEcs.Node;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * ECS component that stores a planned A* path for an entity.
 *
 * Set goalX/goalY and goalFixed = true to give it a fixed destination.
 * PathfindingSystem will compute the path once and stop when the robot arrives.
 *
 * Leave goalFixed = false (default) and PathfindingSystem will keep picking
 * new random goals automatically.
 */
public class PathIComponentService implements IComponentService {

    /** Waypoints from current position to goal, polled one per move-tick. */
    public final Queue<Node> remainingPath = new LinkedList<>();

    /** Target column. */
    public int goalX;

    /** Target row. */
    public int goalY;

    /**
     * When true the goal was set externally and PathfindingSystem will NOT
     * overwrite it with a random destination. The robot travels once and stops.
     */
    public boolean goalFixed = false;

    /** Set by PathfindingSystem while a computation is in-flight. */
    public boolean pathPending = false;

    /** Set to true once the robot has reached a fixed goal. */
    public boolean arrived = false;

    public boolean isDone() { return remainingPath.isEmpty(); }

    public void setPath(List<Node> path) {
        remainingPath.clear();
        remainingPath.addAll(path);
        pathPending = false;
    }

    public Node peekNext() { return remainingPath.peek(); }
    public Node pollNext() { return remainingPath.poll(); }
}
