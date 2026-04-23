package dk.sdu.se4.group1.CommonEcs.Components;

import dk.sdu.se4.group1.CommonApi.SeedType;
import dk.sdu.se4.group1.CommonEcs.Component;

//MOST COMPONENTS HAVE BEEN MOVED TO COMMONECS SINCE IF A COMPONENT NEEDS TO SHARE DATA WITH ANOTHER SYSTEM THAT IS NECESSARY

public class CropComponent implements Component {
    public SeedType seedType;
    public boolean isHarvestable;

    public CropComponent(SeedType seedType){
        this.seedType = seedType;
        isHarvestable = false;
    }
}