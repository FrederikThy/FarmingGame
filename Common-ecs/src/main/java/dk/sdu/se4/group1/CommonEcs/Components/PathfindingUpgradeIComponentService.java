package dk.sdu.se4.group1.CommonEcs.Components;

import dk.sdu.se4.group1.CommonEcs.IComponentService;

/**
 * Data-only component that stores which pathfinding algorithm tier
 * the player has currently unlocked. All upgrade logic lives in
 * ShopPlugin (ECS system layer), not here.
 *
 * Tier progression: BFS (default) -> DIJKSTRA -> A_STAR
 */
public class PathfindingUpgradeIComponentService implements IComponentService {

    public enum AlgorithmTier {
        BFS(0, "BFS"),
        DIJKSTRA(1, "Dijkstra"),
        A_STAR(2, "A*");

        public final int tier;
        public final String displayName;

        AlgorithmTier(int tier, String displayName) {
            this.tier = tier;
            this.displayName = displayName;
        }
    }

    public AlgorithmTier activeTier = AlgorithmTier.BFS;
}
