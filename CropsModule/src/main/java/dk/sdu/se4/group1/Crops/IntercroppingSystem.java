package dk.sdu.se4.group1.Crops;

import dk.sdu.se4.group1.CommonApi.SeedType;
import dk.sdu.se4.group1.CommonEcs.Components.CropComponent;
import dk.sdu.se4.group1.CommonEcs.Components.PositionComponent;
import dk.sdu.se4.group1.CommonEcs.Components.RenderComponent;
import dk.sdu.se4.group1.CommonEcs.EcsSystem;
import dk.sdu.se4.group1.CommonEcs.EntityID;
import dk.sdu.se4.group1.CommonEcs.SeedRequest;
import dk.sdu.se4.group1.CommonEcs.World;
import javafx.geometry.Pos;


public class IntercroppingSystem implements EcsSystem{

    @Override
    public void update(World world, double deltaTime) {

        for(EntityID crop: world.getEntitiesWith(GrowthComponent.class)){

            PositionComponent cropPos = (PositionComponent) world.GetComponent(crop, PositionComponent.class);
            GrowthComponent growth = (GrowthComponent) world.GetComponent(crop, GrowthComponent.class);
            CropComponent cropComp = (CropComponent) world.GetComponent(crop, CropComponent.class);


            for(EntityID nextCrop : world.getEntitiesWith(GrowthComponent.class)){
                PositionComponent nextCropPos = (PositionComponent) world.GetComponent(nextCrop, PositionComponent.class);

                CropComponent nextCropComp = (CropComponent) world.GetComponent(crop, CropComponent.class);

                if(cropPos.x+1 == nextCropPos.x && cropPos.y == nextCropPos.y){
                    if(nextCropComp.SeedType != CropComponent.SeedType)
                    {
                        growth.growthTime -= 2.5;
                    }
                }
                if(cropPos.x-1 == nextCropPos.x && cropPos.y == nextCropPos.y){
                    if(nextCropComp.SeedType != CropComponent.SeedType)
                    {
                        growth.growthTime -= 2.5;
                    }
                }
                if(cropPos.x == nextCropPos.x && cropPos.y+1 == nextCropPos.y){
                    if(nextCropComp.SeedType != CropComponent.SeedType)
                    {
                        growth.growthTime -= 2.5;
                    }
                }
                if(cropPos.x == nextCropPos.x && cropPos.y-1 == nextCropPos.y){
                    if(nextCropComp.SeedType != CropComponent.SeedType)
                    {
                        growth.growthTime -= 2.5;
                    }
                }
            }
        }
    }
}
