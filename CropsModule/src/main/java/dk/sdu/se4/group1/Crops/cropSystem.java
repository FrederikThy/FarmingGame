package dk.sdu.se4.group1.Crops;

import dk.sdu.se4.group1.CommonApi.SeedType;
import dk.sdu.se4.group1.CommonEcs.Components.CropIComponentService;
import dk.sdu.se4.group1.CommonEcs.Components.RenderIComponentService;
import dk.sdu.se4.group1.CommonEcs.IEntityProcessingService;
import dk.sdu.se4.group1.CommonEcs.EntityID;
import dk.sdu.se4.group1.CommonEcs.SeedRequest;
import dk.sdu.se4.group1.CommonEcs.World;
import dk.sdu.se4.group1.CommonEcs.Components.GrowthMapIComponentService;
import javafx.scene.image.Image;

import java.io.IOException;
import java.io.InputStream;

public class cropSystem implements IEntityProcessingService {

    @Override
    public void update(World world, double deltaTime) {
        double growthRate = getGrowthRate(world);

        //Takes the first seed request from the world queue.
        //The request was put there by a robot that wants to plant a seed.
        //The reques is stored in a queue in world.
        //The seed request record contains seed type and location.
        //this lets this crop system know where and what to plant.
        SeedRequest request = world.CheckSeedQueue();


        //If there is a seed request create crop entity
        if(request != null){
            cropFactory.createCrop(world, request.x(), request.y(), request.seedType());
        }

        //Checks for each crop entity that exists on the map.
        //If that crop has been at the same growth stage for more than 5 seconds
        //Increase that growth stage and change the sprite to the new one
        //Max growth stage is 4 and then the crops isharvestable turns true
        for(EntityID crop : world.getEntitiesWith(CropIComponentService.class)){
            GrowthIComponentService Growth =(GrowthIComponentService) world.GetComponent(crop, GrowthIComponentService.class);
            CropIComponentService Crop = (CropIComponentService) world.GetComponent(crop, CropIComponentService.class);
            RenderIComponentService Render = (RenderIComponentService) world.GetComponent(crop, RenderIComponentService.class);

            //Return here would stop the whole system update so we use continue instead
            if(Growth.growthStage == 4 ){
                Crop.isHarvestable = true;
                continue;
            }

            //Each crop has its own growth time contained in its growth component
            //here we add the deltatime for each system call
            Growth.elapsedGrowthTime = Growth.elapsedGrowthTime + (deltaTime * growthRate);
            //Updated so we can multiply with our growth time

            if(Growth.elapsedGrowthTime >= Growth.growthTime){
                Growth.elapsedGrowthTime = 0;

                Growth.growthStage++;

                //Uses the helper method below to find the correct path to the new sprite for the growth stage
                String newSpritePath = getSpritePath(Crop.seedType, Growth.growthStage);


                //Changes sprite
                try (InputStream spriteStream = cropFactory.class.getResourceAsStream(newSpritePath)) {
                    if (spriteStream == null) {
                        throw new IllegalArgumentException("Sprite not found: " + newSpritePath);
                    }
                    //Here we add the render the image from the code above
                    //Throws an exception if it fails to load the crop
                    world.addComponent(crop, new RenderIComponentService(new Image(spriteStream)));
                } catch (IOException e) {
                    throw new RuntimeException("Failed to close sprite stream", e);
                }
            }
        }
    }


    //Very smart method that makes the path to the crops sprite depending on growthstage and seedtype :)
    private String getSpritePath(SeedType seedType, int growthStage) {
        return switch (seedType) {
            case TOMATO -> "/Tomato_" + growthStage + ".png";
            case CARROT -> "/Carrot_" + growthStage + ".png";
            case CHILI -> "/Chili_" + growthStage + ".png";
            case BEANSPROUT -> "/Beansprout_" + growthStage + ".png";
        };
    }

    //Checks for growth map in the world and if true then it returns the growth rate from that map
    private double getGrowthRate(World world) {
        for (EntityID entity : world.getEntitiesWith(GrowthMapIComponentService.class)) {
            GrowthMapIComponentService growthMap =
                    (GrowthMapIComponentService) world.GetComponent(entity, GrowthMapIComponentService.class);
            return growthMap.
                    getGrowthRate();
        }
        return 1.0;
    }

}
