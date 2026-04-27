package dk.sdu.se4.group1.Pathfinding;

import dk.sdu.se4.group1.CommonEcs.IPathfinding;

// Pathfinding factory

public enum PathfindingAlgorithm {
    A_STAR,
    BFS,
    DIJKSTRA;

    // Change this line to change robots algorithm - will probably be changed in the future 
    public static final PathfindingAlgorithm ACTIVE = PathfindingAlgorithm.A_STAR;

    /** Returns a fresh instance of whichever algorithm is currently ACTIVE. */
    public static IPathfinding create() {
        return switch (ACTIVE) {
            case A_STAR   -> new AStarPathfinding();
            case BFS      -> new BFSPathfinding();
            case DIJKSTRA -> new DijkstraPathfinding();
        };
    }
}
