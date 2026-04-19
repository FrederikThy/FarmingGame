package dk.sdu.se4.group1.Pathfinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;

import dk.sdu.se4.group1.CommonEcs.EntityID;
import dk.sdu.se4.group1.CommonEcs.IPathfinding;
import dk.sdu.se4.group1.CommonEcs.PositionComponent;
import dk.sdu.se4.group1.CommonEcs.TileComponent;
import dk.sdu.se4.group1.CommonEcs.World;

public class AStar implements IPathfinding {

    // -------------------------------------------------------------------------
    // Algorithm-internal helper — NOT an ECS citizen, lives only during search
    // -------------------------------------------------------------------------
    private static class Node {
        final int x, y;
        int gCost, hCost;
        Node parent;

        Node(int x, int y) {
            this.x = x;
            this.y = y;
        }

        int fCost() { return gCost + hCost; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Node)) return false;
            Node n = (Node) o;
            return x == n.x && y == n.y;
        }

        @Override
        public int hashCode() { return Objects.hash(x, y); }
    }

    // -------------------------------------------------------------------------
    // IPathfinding implementation
    // Returns a list of PositionComponents — callers never see Node
    // -------------------------------------------------------------------------
    @Override
    public List<PositionComponent> findPath(World world,PositionComponent start,PositionComponent end) {

        Node startNode  = new Node(start.getX(), start.getY());
        Node targetNode = new Node(end.getX(),   end.getY());

        // Pre-fetch all tile entities ONCE — avoids rescanning on every neighbor check
        Set<EntityID> tileEntities = world.getEntitiesWith(TileComponent.class);

        // Build a spatial lookup: "x,y" -> EntityID  (O(n) once, then O(1) per lookup)
        Map<String, EntityID> spatialIndex = buildSpatialIndex(world, tileEntities);

        PriorityQueue<Node> openSet   = new PriorityQueue<>(Comparator.comparingInt(Node::fCost));
        Set<Node>           closedSet = new HashSet<>();

        startNode.hCost = heuristic(startNode, targetNode);
        openSet.add(startNode);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();

            if (current.equals(targetNode)) {
                return retracePath(startNode, current);
            }

            closedSet.add(current);

            for (Node neighbor : getNeighbors(current, spatialIndex, world)) {
                if (closedSet.contains(neighbor)) continue;

                int tentativeG = current.gCost + heuristic(current, neighbor);

                if (tentativeG < neighbor.gCost || !openSet.contains(neighbor)) {
                    neighbor.gCost  = tentativeG;
                    neighbor.hCost  = heuristic(neighbor, targetNode);
                    neighbor.parent = current;

                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor);
                    }
                }
            }
        }

        return null; // No path found
    }

    // -------------------------------------------------------------------------
    // Build a coordinate -> EntityID map from tile entities (called once)
    // -------------------------------------------------------------------------
    private Map<String, EntityID> buildSpatialIndex(World world,
                                                     Set<EntityID> tileEntities) {
        Map<String, EntityID> index = new HashMap<>();

        for (EntityID entity : tileEntities) {
            if (world.hasComponent(entity, PositionComponent.class)) {
                PositionComponent pos =
                    (PositionComponent) world.GetComponent(entity, PositionComponent.class);
                index.put(pos.getX() + "," + pos.getY(), entity);
            }
        }
        return index;
    }

    // -------------------------------------------------------------------------
    // Neighbor lookup — O(1) per tile thanks to spatial index
    // -------------------------------------------------------------------------
    private List<Node> getNeighbors(Node current,
                                     Map<String, EntityID> spatialIndex,
                                     World world) {
        List<Node> neighbors = new ArrayList<>();
        int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

        for (int[] dir : directions) {
            int nx = current.x + dir[0];
            int ny = current.y + dir[1];

            EntityID entity = spatialIndex.get(nx + "," + ny);
            if (entity == null) continue;

            TileComponent tile =
                (TileComponent) world.GetComponent(entity, TileComponent.class);

            if (tile.isWalkable()) {
                neighbors.add(new Node(nx, ny));
            }
        }
        return neighbors;
    }

    // -------------------------------------------------------------------------
    // Retrace from end node back to start, return as PositionComponents
    // -------------------------------------------------------------------------
    private List<PositionComponent> retracePath(Node startNode, Node endNode) {
        List<PositionComponent> path = new ArrayList<>();
        Node current = endNode;

        while (!current.equals(startNode)) {
            path.add(new PositionComponent(current.x, current.y));
            current = current.parent;
        }

        Collections.reverse(path);
        return path;
    }

    // -------------------------------------------------------------------------
    // Manhattan heuristic — correct for 4-directional grid movement
    // -------------------------------------------------------------------------
    private int heuristic(Node a, Node b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }
}