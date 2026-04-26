package dk.sdu.se4.group1.CommonEcs.Components;

import dk.sdu.se4.group1.CommonEcs.Component;
public class SpeedToolComponent implements Component{
    public SpeedToolComponent(double speedMultiplier)
    {
        if (speedMultiplier <=0){
            throw new IllegalArgumentException("speedMultiplier must be greater than 0");
        }
        this.speedMultiplier=speedMultiplier;
    }

    private final double speedMultiplier;

    public double getSpeedMultiplier() {return speedMultiplier;}
}
