package dk.sdu.se4.group1.CommonEcs.Components;

import dk.sdu.se4.group1.CommonEcs.IComponentService;

public class HarvestingIComponentService implements IComponentService {
    private double growthMultiplier = 1.0;

    public double getGrowthMultiplier() { return growthMultiplier; }
    public void setGrowthMultiplier(double multiplier) { this.growthMultiplier = multiplier; }

    public double harvestWaitTimer = 0.0;
}