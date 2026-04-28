package dk.sdu.se4.group1.Pathfinding;

import dk.sdu.se4.group1.CommonEcs.IPathfinding;
import dk.sdu.se4.group1.CommonEcs.Node;
import dk.sdu.se4.group1.CommonEcs.World;

import java.util.*;

// Dijkstra's pathfinding algorithm — expands nodes in order of cheapest cumulative cost
// On a uniform-cost grid (all steps cost 1) this produces the same result as BFS,
// but the priority-queue structure makes it easy to extend to weighted tiles later.
// Unlike A*, there is no heuristic, so it explores more nodes but never over-estimates.
// Implements IPathfinding so it can be swapped in without touching any other system.
public class DijkstraPathfinding implements IPathfinding {

    // The four cardinal directions a robot can move: up, down, left, right
    private static final int[][] DIRECTIONS = {
            { 0, -1 }, { 0,  1 }, {-1,  0 }, { 1,  0 }
    };

    @Override
    public List<Node> findPath(int startX, int startY,int goalX,  int goalY,int mapWidth, int mapHeight, World world) {

        // Already at the goal — return a single-node path so the robot doesn't move
        if (startX == goalX && startY == goalY) {
            return List.of(new Node(startX, startY));
        }

        // Build a fresh grid; initialise all gCosts to MAX so any real distance is cheaper
        Node[][] grid = buildGrid(mapWidth, mapHeight);

        Node startNode = grid[startY][startX];
        Node goalNode  = grid[goalY][goalX];

        // Priority queue sorted by gCost (cheapest cumulative distance first)
        // Node.compareTo uses fCost, so we keep hCost = 0 and only set gCost
        PriorityQueue<Node> openSet = new PriorityQueue<>();

        // HashSet mirror for O(1) membership checks (avoids scanning the queue)
        Set<Node> openSetLookup = new HashSet<>();

        // Settled nodes — these have their final shortest distance confirmed
        Set<Node> settled = new HashSet<>();

        startNode.gCost = 0;
        startNode.hCost = 0; // no heuristic in Dijkstra
        startNode.calculateFCost();
        openSet.add(startNode);
        openSetLookup.add(startNode);

        // Main loop — always expand the node with the lowest known cost
        while (!openSet.isEmpty()) {
            Node current = openSet.poll();
            openSetLookup.remove(current);
            settled.add(current); // this node's shortest path is now final

            // Reached the goal — reconstruct and return the path
            if (current.equals(goalNode)) return reconstructPath(current);

            // Relax each neighbour
            for (int[] dir : DIRECTIONS) {
                int nx = current.getX() + dir[0];
                int ny = current.getY() + dir[1];

                if (!inBounds(nx, ny, mapWidth, mapHeight)) continue;

                Node neighbour = grid[ny][nx];

                // Skip nodes whose shortest path is already confirmed
                if (settled.contains(neighbour)) continue;

                // Allow the goal tile even if occupied — robot must be able to reach it
                boolean isGoal = (nx == goalX && ny == goalY);
                if (!isGoal && !world.isTileFreeIgnoringRobots(nx, ny)) continue;

                // Step cost: 1 per tile (extend here for weighted terrain)
                int tentativeG = current.gCost + 1;

                if (tentativeG < neighbour.gCost) {
                    neighbour.parent = current;
                    neighbour.gCost  = tentativeG;
                    neighbour.hCost  = 0; // no heuristic
                    neighbour.calculateFCost();

                    if (!openSetLookup.contains(neighbour)) {
                        openSet.add(neighbour);
                        openSetLookup.add(neighbour);
                    } else {
                        // Re-insert to trigger re-sorting with the new, cheaper cost
                        openSet.remove(neighbour);
                        openSet.add(neighbour);
                    }
                }
            }
        }

        // No path found
        return Collections.emptyList();
    }

    // Create a 2D grid with all costs initialised to MAX_VALUE
    private Node[][] buildGrid(int w, int h) {
        Node[][] grid = new Node[h][w];
        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++) {
                Node n = new Node(x, y);
                n.gCost = Integer.MAX_VALUE;
                n.calculateFCost();
                grid[y][x] = n;
            }
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
