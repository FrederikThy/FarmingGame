package dk.sdu.se4.group1.Pathfinding;

import dk.sdu.se4.group1.CommonEcs.Node;
import dk.sdu.se4.group1.CommonEcs.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class AStarPathfindingTest {

    private AStarPathfinding pathfinder;
    private World world;
    private static final int W = 10, H = 10;

    @BeforeEach void setUp() { pathfinder = new AStarPathfinding(); world = new World(); }

    @Test void trivialPath_sameStartAndGoal_returnsSingleNode() {
        List<Node> path = pathfinder.findPath(3, 3, 3, 3, W, H, world);
        assertEquals(1, path.size());
    }

    @Test void straightLine_horizontal_returnsCorrectLength() {
        List<Node> path = pathfinder.findPath(0, 0, 4, 0, W, H, world);
        assertEquals(5, path.size());
        assertEquals(4, path.get(path.size()-1).getX());
    }

    @Test void straightLine_vertical_returnsCorrectLength() {
        List<Node> path = pathfinder.findPath(0, 0, 0, 4, W, H, world);
        assertEquals(5, path.size());
    }

    @Test void diagonal_goal_pathIsOptimalManhattanLength() {
        List<Node> path = pathfinder.findPath(2, 2, 5, 6, W, H, world);
        assertEquals(8, path.size());
    }

    @Test void pathGoesAroundSingleObstacle() {
        for (int y = 0; y <= 2; y++) {
            var id = world.createEntity();
            world.addComponent(id, new dk.sdu.se4.group1.CommonEcs.Components.PositionComponent(2, y));
        }
        List<Node> path = pathfinder.findPath(0, 0, 4, 0, W, H, world);
        assertFalse(path.isEmpty());
        for (Node n : path) assertFalse(n.getX() == 2 && n.getY() <= 2, "Path through blocked tile");
    }

    @Test void goalTileOccupied_pathStillReachesGoal() {
        var id = world.createEntity();
        world.addComponent(id, new dk.sdu.se4.group1.CommonEcs.Components.PositionComponent(4, 0));
        List<Node> path = pathfinder.findPath(0, 0, 4, 0, W, H, world);
        assertFalse(path.isEmpty());
        assertEquals(4, path.get(path.size()-1).getX());
    }

    @Test void completelyBlockedStart_returnsEmptyList() {
        var r = world.createEntity(); world.addComponent(r, new dk.sdu.se4.group1.CommonEcs.Components.PositionComponent(1, 0));
        var d = world.createEntity(); world.addComponent(d, new dk.sdu.se4.group1.CommonEcs.Components.PositionComponent(0, 1));
        assertTrue(pathfinder.findPath(0, 0, 9, 9, W, H, world).isEmpty());
    }

    @Test void firstNode_isAlwaysStartPosition() {
        List<Node> path = pathfinder.findPath(1, 2, 7, 8, W, H, world);
        assertEquals(1, path.get(0).getX());
        assertEquals(2, path.get(0).getY());
    }

    @Test void consecutiveNodes_areAlwaysAdjacent() {
        List<Node> path = pathfinder.findPath(0, 0, 9, 9, W, H, world);
        for (int i = 1; i < path.size(); i++) {
            int dx = Math.abs(path.get(i).getX() - path.get(i-1).getX());
            int dy = Math.abs(path.get(i).getY() - path.get(i-1).getY());
            assertEquals(1, dx + dy);
        }
    }
}
