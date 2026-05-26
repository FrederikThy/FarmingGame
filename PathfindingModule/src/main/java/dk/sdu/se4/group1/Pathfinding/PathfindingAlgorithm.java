package dk.sdu.se4.group1.Pathfinding;

import dk.sdu.se4.group1.CommonEcs.Components.PathfindingUpgradeIComponentService;
import dk.sdu.se4.group1.CommonEcs.IPathfindingService;


 // Stateless factory. Returns the IPathfinding implementation for a given tier.
 
public class PathfindingAlgorithm {

    private PathfindingAlgorithm() {}

    public static IPathfindingService create(PathfindingUpgradeIComponentService.AlgorithmTier tier) {
        return switch (tier) {
            case BFS      -> new BFSPathfindingService();
            case DIJKSTRA -> new DijkstraPathfindingService();
            case A_STAR   -> new AStarPathfindingService();
        };
    }

    public static IPathfindingService createDefault() {
        return new BFSPathfindingService();
    }
}
