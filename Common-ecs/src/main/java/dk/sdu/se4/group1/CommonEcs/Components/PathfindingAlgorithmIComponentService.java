package dk.sdu.se4.group1.CommonEcs.Components;

import dk.sdu.se4.group1.CommonEcs.IComponentService;

/**
 * Data-only marker component used as the item type for algorithm
 * upgrade offers in the shop. Carries the tier it unlocks.
 */
public class PathfindingAlgorithmIComponentService implements IComponentService {

    public final PathfindingUpgradeIComponentService.AlgorithmTier tier;

    public PathfindingAlgorithmIComponentService(PathfindingUpgradeIComponentService.AlgorithmTier tier) {
        this.tier = tier;
    }
}
