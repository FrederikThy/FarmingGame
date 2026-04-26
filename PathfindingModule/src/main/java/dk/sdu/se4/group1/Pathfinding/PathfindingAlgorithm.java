package dk.sdu.se4.group1.Pathfinding;

import dk.sdu.se4.group1.CommonEcs.IPathfinding;

/**
 * Enum that enumerates every available pathfinding algorithm.
 * To switch algorithms, change the ACTIVE constant below — one line, no other edits needed.
 *
 * <pre>
 *   A_STAR   — best performance, optimal path, uses Manhattan heuristic
 *   BFS      — simple wave-front search, optimal on uniform grids, no heuristic
 *   DIJKSTRA — cost-based expansion, no heuristic; ready for weighted terrain
 * </pre>
 */
public enum PathfindingAlgorithm {
    A_STAR,
    BFS,
    DIJKSTRA;

    // ── CHANGE THIS LINE TO SWITCH ALGORITHMS 
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
