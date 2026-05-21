package dk.sdu.se4.group1.Pathfinding;

import dk.sdu.se4.group1.CommonEcs.Components.PathfindingUpgradeComponent;
import dk.sdu.se4.group1.CommonEcs.IPathfinding;

/**
 * Stateless factory. Returns the IPathfinding implementation for a given tier.
 * The hardcoded ACTIVE constant has been removed — the active algorithm is
 * determined at runtime from PathfindingUpgradeComponent in the world.
 */
public class PathfindingAlgorithm {

    private PathfindingAlgorithm() {}

    public static IPathfinding create(PathfindingUpgradeComponent.AlgorithmTier tier) {
        return switch (tier) {
            case BFS      -> new BFSPathfinding();
            case DIJKSTRA -> new DijkstraPathfinding();
            case A_STAR   -> new AStarPathfinding();
        };
    }

    public static IPathfinding createDefault() {
        return new BFSPathfinding();
    }
}
