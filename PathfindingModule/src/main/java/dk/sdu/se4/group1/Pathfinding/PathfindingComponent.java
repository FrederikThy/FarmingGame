package dk.sdu.se4.group1.Pathfinding;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import dk.sdu.se4.group1.CommonEcs.Component;
import dk.sdu.se4.group1.CommonEcs.PositionComponent;

public class PathfindingComponent implements Component {
    private Queue<PositionComponent> path = new LinkedList<>();
    private PositionComponent target;

    public PathfindingComponent(PositionComponent target) {
        this.target = target;
    }

    public PositionComponent getTarget() { return target; }

    public void setTarget(PositionComponent target) {
        this.target = target;
        this.path.clear(); // New target = recalculate
    }

    public void setPath(List<PositionComponent> newPath) {
        path = new LinkedList<>(newPath);
    }

    public PositionComponent pollNextStep() { return path.poll(); }

    public boolean hasPath()     { return !path.isEmpty(); }
    public boolean hasTarget()   { return target != null; }
}