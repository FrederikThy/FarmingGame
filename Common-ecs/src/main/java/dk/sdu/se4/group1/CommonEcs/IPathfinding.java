package dk.sdu.se4.group1.CommonEcs;
import java.util.List;

public interface IPathfinding {
List<PositionComponent> findPath(World world, PositionComponent start, PositionComponent end);
}