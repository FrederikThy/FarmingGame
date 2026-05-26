package dk.sdu.se4.group1.Pathfinding;

import dk.sdu.se4.group1.CommonEcs.IPathfindingService;
import dk.sdu.se4.group1.CommonEcs.Node;
import dk.sdu.se4.group1.CommonEcs.World;

import java.util.*;

// A* pathfinding algorithm — finds the shortest walkable path on the tile grid
// Implements IPathfinding so it can be swapped out without touching any other system
public class AStarPathfindingService implements IPathfindingService {

    // The four directions a robot can move: up, down, left, right
    private static final int[][] DIRECTIONS = {
            { 0, -1 }, { 0,  1 }, {-1,  0 }, { 1,  0 }
    };

    @Override
    public List<Node> findPath(int startX, int startY, int goalX,  int goalY, int mapWidth, int mapHeight, World world) {

        // Already at the goal — return a single-node path so the robot doesn't move
        if (startX == goalX && startY == goalY) {
            return List.of(new Node(startX, startY));
        }

        // Open set: nodes we have discovered but not yet fully evaluated, sorted by fCost
        PriorityQueue<Node> openSet = new PriorityQueue<>();

        // HashSet mirror of openSet for O(1) membership checks
        Set<Node> openSetLookup = new HashSet<>();

        // Closed set: nodes we have already evaluated — we never revisit these
        Set<Node> closedSet = new HashSet<>();

        // Build a fresh grid of nodes for this search — each starts with gCost = MAX so any real path is cheaper
        Node[][] grid = buildGrid(mapWidth, mapHeight);

        Node startNode = grid[startY][startX];
        Node goalNode  = grid[goalY][goalX];

        // Start node costs nothing to reach from itself
        startNode.gCost = 0;
        startNode.hCost = heuristic(startX, startY, goalX, goalY);
        startNode.calculateFCost();

        // Seed the open set with the starting node
        openSet.add(startNode);
        openSetLookup.add(startNode);

        // Main loop — keep expanding the cheapest known node until we reach the goal or run out of options
        while (!openSet.isEmpty()) {

            // Pull the node with the lowest fCost (best candidate to explore next)
            Node current = openSet.poll();
            openSetLookup.remove(current);
            closedSet.add(current); // mark as fully evaluated

            // Reached the goal — walk back up the parent chain to build the path
            if (current.equals(goalNode)) return reconstructPath(current);

            // Check all four neighbours
            for (int[] dir : DIRECTIONS) {
                int nx = current.getX() + dir[0];
                int ny = current.getY() + dir[1];

                // Ignore tiles outside the map
                if (!inBounds(nx, ny, mapWidth, mapHeight)) continue;

                Node neighbour = grid[ny][nx];

                // Skip tiles we have already fully evaluated
                if (closedSet.contains(neighbour)) continue;

                // Allow the goal tile even if something is sitting on it (robot needs to reach it)
                // Block all other occupied tiles — crops and weeds are solid obstacles
                boolean isGoal = (nx == goalX && ny == goalY);
                if (!isGoal && !world.isTileFreeIgnoringRobots(nx, ny)) continue;

                // Cost to reach this neighbour through the current node (every step costs 1)
                int tentativeG = current.gCost + 1;

                // Only update if we found a cheaper route to this neighbour
                if (tentativeG < neighbour.gCost) {
                    neighbour.parent = current; // remember how we got here
                    neighbour.gCost  = tentativeG;
                    neighbour.hCost  = heuristic(nx, ny, goalX, goalY);
                    neighbour.calculateFCost(); // fCost = gCost + hCost

                    if (!openSetLookup.contains(neighbour)) {
                        // First time discovering this neighbour — add it
                        openSet.add(neighbour);
                        openSetLookup.add(neighbour);
                    } else {
                        // Already in open set with a worse cost — re-insert so the queue re-sorts
                        openSet.remove(neighbour);
                        openSet.add(neighbour);
                    }
                }
            }
        }

        // Open set is empty and we never hit the goal — no path exists
        return Collections.emptyList();
    }

    // Create a 2D grid of fresh Node objects with all costs set to MAX_VALUE
    private Node[][] buildGrid(int w, int h) {
        Node[][] grid = new Node[h][w];
        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++) {
                Node n = new Node(x, y);
                n.gCost = Integer.MAX_VALUE; // unknown cost until we find a path to it
                n.calculateFCost();
                grid[y][x] = n;
            }
        return grid;
    }

    // Manhattan distance: |dx| + |dy| — exact for 4-directional grids, so A* always finds the optimal path
    private int heuristic(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    // Check a tile coordinate is inside the map boundaries
    private boolean inBounds(int x, int y, int w, int h) {
        return x >= 0 && x < w && y >= 0 && y < h;
    }

    // Walk the parent chain from the goal back to the start, then reverse so the list runs start → goal
    private List<Node> reconstructPath(Node goal) {
        List<Node> path = new ArrayList<>();
        for (Node n = goal; n != null; n = n.parent) path.add(n);
        Collections.reverse(path);
        return path;
    }
}
