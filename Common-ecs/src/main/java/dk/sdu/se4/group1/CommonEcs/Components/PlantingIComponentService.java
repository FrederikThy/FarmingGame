package dk.sdu.se4.group1.CommonEcs.Components;

import dk.sdu.se4.group1.CommonEcs.IComponentService;

public class PlantingIComponentService implements IComponentService {
    private double plantingSpeedMultiplier = 1.0;

    public double getPlantingSpeedMultiplier() { return plantingSpeedMultiplier; }
    public void setPlantingSpeedMultiplier(double multiplier) { this.plantingSpeedMultiplier = multiplier; }

    public double plantWaitTimer = 0.0;
}