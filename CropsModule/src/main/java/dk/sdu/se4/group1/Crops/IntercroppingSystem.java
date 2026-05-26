package dk.sdu.se4.group1.Crops;

import dk.sdu.se4.group1.CommonEcs.Components.CropIComponentService;
import dk.sdu.se4.group1.CommonEcs.Components.PositionIComponentService;
import dk.sdu.se4.group1.CommonEcs.IEntityProcessingService;
import dk.sdu.se4.group1.CommonEcs.EntityID;
import dk.sdu.se4.group1.CommonEcs.World;


public class IntercroppingSystem implements IEntityProcessingService {

    @Override
    public void update(World world, double deltaTime) {

        for(EntityID crop: world.getEntitiesWith(GrowthIComponentService.class)){

            PositionIComponentService cropPos = (PositionIComponentService) world.GetComponent(crop, PositionIComponentService.class);
            GrowthIComponentService growth = (GrowthIComponentService) world.GetComponent(crop, GrowthIComponentService.class);


            for(EntityID nextCrop : world.getEntitiesWith(GrowthIComponentService.class)){
                PositionIComponentService nextCropPos = (PositionIComponentService) world.GetComponent(nextCrop, PositionIComponentService.class);

                CropIComponentService cropComp = (CropIComponentService) world.GetComponent(crop, CropIComponentService.class);
                CropIComponentService nextCropComp = (CropIComponentService) world.GetComponent(crop, CropIComponentService.class);

                if(cropPos.x+1 == nextCropPos.x && cropPos.y == nextCropPos.y){
                    if(nextCropComp.seedType != cropComp.seedType)
                    {
                        growth.growthTime -= 2.5;
                    }
                }
                if(cropPos.x-1 == nextCropPos.x && cropPos.y == nextCropPos.y){
                    if(nextCropComp.seedType != cropComp.seedType)
                    {
                        growth.growthTime -= 2.5;
                    }
                }
                if(cropPos.x == nextCropPos.x && cropPos.y+1 == nextCropPos.y){
                    if(nextCropComp.seedType != cropComp.seedType)
                    {
                        growth.growthTime -= 2.5;
                    }
                }
                if(cropPos.x == nextCropPos.x && cropPos.y-1 == nextCropPos.y){
                    if(nextCropComp.seedType != cropComp.seedType)
                    {
                        growth.growthTime -= 2.5;
                    }
                }
            }
        }
    }
}
