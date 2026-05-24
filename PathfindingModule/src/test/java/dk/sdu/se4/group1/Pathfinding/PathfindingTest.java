package dk.sdu.se4.group1.Pathfinding;

import dk.sdu.se4.group1.CommonEcs.Node;
import dk.sdu.se4.group1.CommonEcs.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PathfindingTest {
private static final int W = 10, H = 10;
private World emptyWorld;
private AStarPathfinding aStar;
private BFSPathfinding   bfs;

@BeforeEach
void setUp() {
    emptyWorld = new World();
    aStar      = new AStarPathfinding();
    bfs        = new BFSPathfinding();
}

/** Test to see if algorithms work and return a valid path */
@Test
void algorithmsPathTest() {
    List<Node> aStarPath = aStar.findPath(0, 0, 4, 4, W, H, emptyWorld);
    List<Node> bfsPath   = bfs.findPath(0, 0, 4, 4, W, H, emptyWorld);
    assertFalse(aStarPath.isEmpty(), "A* must find a path on a clear grid");
    assertFalse(bfsPath.isEmpty(),   "BFS must find a path on a clear grid");
    
    // Start at 0 check
    assertEquals(0, aStarPath.get(0).getX());
    assertEquals(0, aStarPath.get(0).getY());
    assertEquals(0, bfsPath.get(0).getX());
    assertEquals(0, bfsPath.get(0).getY());
    
    // Check goal
    Node aStarGoal = aStarPath.get(aStarPath.size() - 1);
    Node bfsGoal = bfsPath.get(bfsPath.size() - 1);
    assertEquals(4, aStarGoal.getX());
    assertEquals(4, aStarGoal.getY());
    assertEquals(4, bfsGoal.getX());
    assertEquals(4, bfsGoal.getY());
}

/** Test to see if the algorithms return one node if the start and goal are the same - edge case testing */
@Test
void sameStartAndEndNodeTest() {
    List<Node> aStarPath = aStar.findPath(3, 3, 3, 3, W, H, emptyWorld);
    List<Node> bfsPath   = bfs.findPath(3, 3, 3, 3, W, H, emptyWorld);

    assertEquals(1, aStarPath.size(), "A* same-tile path must have exactly 1 node");
    assertEquals(1, bfsPath.size(),   "BFS same-tile path must have exactly 1 node");
    assertEquals(3, aStarPath.get(0).getX());
    assertEquals(3, aStarPath.get(0).getY());
}

/** Test to see if A* returns optimal path */
@Test
void aStarOptimalPathTest() {
    int startX = 0, startY = 0, goalX = 5, goalY = 0;
    int expectedLength = 6;
    List<Node> path = aStar.findPath(startX, startY, goalX, goalY, W, H, emptyWorld);
    assertEquals(expectedLength, path.size(), "A* on a clear, straight-line route must return an optimal path");
}
}
