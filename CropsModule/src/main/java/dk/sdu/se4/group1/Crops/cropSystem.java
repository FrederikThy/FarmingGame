package dk.sdu.se4.group1.Crops;

import dk.sdu.se4.group1.CommonApi.SeedType;
import dk.sdu.se4.group1.CommonEcs.Components.CropComponent;
import dk.sdu.se4.group1.CommonEcs.Components.RenderComponent;
import dk.sdu.se4.group1.CommonEcs.EcsSystem;
import dk.sdu.se4.group1.CommonEcs.EntityID;
import dk.sdu.se4.group1.CommonEcs.SeedRequest;
import dk.sdu.se4.group1.CommonEcs.World;
import javafx.scene.image.Image;

import java.io.IOException;
import java.io.InputStream;

public class cropSystem implements EcsSystem {

    //Time for a crop to grow 1 in its growth stage (max growth stage is 4)
    private static final double CropGrowTime = 5;

    @Override
    public void update(World world, double deltaTime) {

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
        for(EntityID crop : world.getEntitiesWith(CropComponent.class)){
            GrowthComponent Growth =(GrowthComponent) world.GetComponent(crop, GrowthComponent.class);
            CropComponent Crop = (CropComponent) world.GetComponent(crop, CropComponent.class);
            RenderComponent Render = (RenderComponent) world.GetComponent(crop, RenderComponent.class);

            //Return here would stop the whole system update so we use continue instead
            if(Growth.growthStage == 4 ){
                Crop.isHarvestable = true;
                continue;
            }

            //Each crop has its own growth time contained in its growth component
            //here we add the deltatime for each system call
            Growth.elapsedGrowthTime = Growth.elapsedGrowthTime + deltaTime;

            if(Growth.elapsedGrowthTime >= CropGrowTime){
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
                    world.addComponent(crop, new RenderComponent(new Image(spriteStream)));
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
}
