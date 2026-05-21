package dk.sdu.se4.group1.CommonEcs.Components;

import dk.sdu.se4.group1.CommonEcs.Component;

/**
 * Data-only marker component used as the item type for algorithm
 * upgrade offers in the shop. Carries the tier it unlocks.
 */
public class PathfindingAlgorithmComponent implements Component {

    public final PathfindingUpgradeComponent.AlgorithmTier tier;

    public PathfindingAlgorithmComponent(PathfindingUpgradeComponent.AlgorithmTier tier) {
        this.tier = tier;
    }
}
