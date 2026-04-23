package dk.sdu.se4.group1.Crops;

import dk.sdu.se4.group1.CommonEcs.Component;

public class GrowthComponent implements Component {

    //Data only used locally by Crops
    public int growthStage;
    public double elapsedGrowthTime;

    public GrowthComponent(){
        growthStage = 1;
        elapsedGrowthTime = 0;
    }
}
