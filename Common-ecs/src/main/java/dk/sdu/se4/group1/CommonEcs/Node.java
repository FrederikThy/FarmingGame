package dk.sdu.se4.group1.CommonEcs;

import java.util.Objects;

public class Node implements Comparable<Node> {
    private final int x, y;
    public int gCost, hCost, fCost;
    public Node parent;

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public void calculateFCost() {
        this.fCost = this.gCost + this.hCost;
    }

    @Override // This allows the PriorityQueue to sort nodes by F-Cost
    public int compareTo(Node other) {
        return Integer.compare(this.fCost, other.fCost);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node)) return false;
        Node node = (Node) o;
        return x == node.x && y == node.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}