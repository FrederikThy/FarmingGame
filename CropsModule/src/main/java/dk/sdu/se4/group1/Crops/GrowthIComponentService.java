package dk.sdu.se4.group1.Crops;

import dk.sdu.se4.group1.CommonEcs.IComponentService;

public class GrowthIComponentService implements IComponentService {

    //Data only used locally by Crops
    public int growthStage;
    public double elapsedGrowthTime;

    //Time for a crop to grow 1 in its growth stage (max growth stage is 4)
    public double growthTime;

    public GrowthIComponentService(){
        growthStage = 1;
        elapsedGrowthTime = 0;
        growthTime= 5;
    }
}
