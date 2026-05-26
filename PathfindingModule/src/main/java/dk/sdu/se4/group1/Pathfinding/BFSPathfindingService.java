package dk.sdu.se4.group1.Pathfinding;

import dk.sdu.se4.group1.CommonEcs.IPathfindingService;
import dk.sdu.se4.group1.CommonEcs.Node;
import dk.sdu.se4.group1.CommonEcs.World;

import java.util.*;

// Breadth-First Search pathfinding — explores tiles in wave-fronts (layer by layer)
// Guarantees the shortest path in terms of number of steps (all edges cost 1).
// Uses no heuristic, so it explores more tiles than A*, but always finds the optimal route.
public class BFSPathfindingService implements IPathfindingService {

    // The four cardinal directions a robot can move: up, down, left, right
    private static final int[][] DIRECTIONS = {
            { 0, -1 }, { 0,  1 }, {-1,  0 }, { 1,  0 }
    };

    @Override
    public List<Node> findPath(int startX, int startY,int goalX,  int goalY,int mapWidth, int mapHeight,World world) {

        // Already at the goal — return a single-node path so the robot doesn't move
        if (startX == goalX && startY == goalY) {
            return List.of(new Node(startX, startY));
        }

        // Build a fresh grid so each node can track its parent for path reconstruction
        Node[][] grid = buildGrid(mapWidth, mapHeight);

        Node startNode = grid[startY][startX];
        Node goalNode  = grid[goalY][goalX];

        // BFS frontier — each entry is the next tile to expand, in FIFO order
        Queue<Node> queue = new ArrayDeque<>();

        // Track which tiles have already been added to the queue so we never revisit
        Set<Node> visited = new HashSet<>();

        queue.add(startNode);
        visited.add(startNode);

        // Main loop — expand one node per iteration, always the oldest (shallowest) one
        while (!queue.isEmpty()) {
            Node current = queue.poll();

            // Reached the goal — reconstruct the path back to the start
            if (current.equals(goalNode)) return reconstructPath(current);

            // Enqueue all valid unvisited neighbours
            for (int[] dir : DIRECTIONS) {
                int nx = current.getX() + dir[0];
                int ny = current.getY() + dir[1];

                if (!inBounds(nx, ny, mapWidth, mapHeight)) continue;

                Node neighbour = grid[ny][nx];

                if (visited.contains(neighbour)) continue;

                // Allow the goal tile even if occupied — robot must be able to reach it
                boolean isGoal = (nx == goalX && ny == goalY);
                if (!isGoal && !world.isTileFreeIgnoringRobots(nx, ny)) continue;

                neighbour.parent = current; // record how we reached this tile
                visited.add(neighbour);
                queue.add(neighbour);
            }
        }

        // Queue emptied without reaching the goal — no path exists
        return Collections.emptyList();
    }

    // Create a 2D grid of fresh Node objects (costs are unused in BFS but kept for interface compatibility)
    private Node[][] buildGrid(int w, int h) {
        Node[][] grid = new Node[h][w];
        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++)
                grid[y][x] = new Node(x, y);
        return grid;
    }

    // Check that a tile coordinate lies within the map boundaries
    private boolean inBounds(int x, int y, int w, int h) {
        return x >= 0 && x < w && y >= 0 && y < h;
    }

    // Walk the parent chain from the goal back to the start, then reverse for start → goal order
    private List<Node> reconstructPath(Node goal) {
        List<Node> path = new ArrayList<>();
        for (Node n = goal; n != null; n = n.parent) path.add(n);
        Collections.reverse(path);
        return path;
    }
}
