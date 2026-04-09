package dk.sdu.se4.group1.Pathfinding;

import dk.sdu.se4.group1.CommonEcs.*;
import java.util.*;

public class AStarPathfinding implements IPathfinding {
    
    @Override
    public List<Node> findPath(World world, PositionComponent start, PositionComponent end) {
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingInt(n -> n.fCost));
        Set<Node> closedSet = new HashSet<>();

        Node startNode = new Node(start.getX(), start.getY());
        Node targetNode = new Node(end.getX(), end.getY());

        openSet.add(startNode);

        while (!openSet.isEmpty()) {
            Node currentNode = openSet.poll();

            if (currentNode.equals(targetNode)) {
                return retracePath(startNode, currentNode);
            }

            closedSet.add(currentNode);

            for (Node neighbor : getNodes(currentNode, world)) {
                if (closedSet.contains(neighbor)) continue;

                int newMovementCostToNeighbor = currentNode.gCost + getDistance(currentNode, neighbor);
                if (newMovementCostToNeighbor < neighbor.gCost || !openSet.contains(neighbor)) {
                    neighbor.gCost = newMovementCostToNeighbor;
                    neighbor.hCost = getDistance(neighbor, targetNode);
                    neighbor.parent = currentNode;

                    if (!openSet.contains(neighbor)) openSet.add(neighbor);
                }
            }
        }
        return null; 
    }

    private List<Node> retracePath(Node startNode, Node endNode) {
        List<Node> path = new ArrayList<>();
        Node currentNode = endNode;
        while (!currentNode.equals(startNode)) {
            path.add(currentNode);
            currentNode = currentNode.parent;
        }
        Collections.reverse(path);
        return path;
    }

    private int getDistance(Node a, Node b) {
        return Math.abs(a.getX() - b.getX()) + Math.abs(a.getY() - b.getY());
    }

       
    private List<Node> getNeighbors(Node currentNode, WorldMap worldMap) {
        List<Node> neighbors = new ArrayList<>();

        // Define the 4 cardinal directions: {x, y}
        int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

        for (int[] dir : directions) {
            int checkX = currentNode.getX() + dir[0];
            int checkY = currentNode.getY() + dir[1];

            // 1. Boundary Check: Ensure the coordinates are within the map limits
            if (checkX >= 0 && checkX < worldMap.getCols() && checkY >= 0 && checkY < worldMap.getRows()) {
                
                // 2. Walkability Check: Ensure the tile is not an obstacle (Black)
                if (worldMap.getTile(checkX, checkY).getFill() != Color.BLACK) {
                    neighbors.add(new Node(checkX, checkY));
                }
            }
        }
        return neighbors;
    }

}