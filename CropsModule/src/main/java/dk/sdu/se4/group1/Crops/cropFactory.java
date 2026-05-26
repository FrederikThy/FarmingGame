package dk.sdu.se4.group1.Crops;

import dk.sdu.se4.group1.CommonApi.SeedType;
import dk.sdu.se4.group1.CommonEcs.Components.CropIComponentService;
import dk.sdu.se4.group1.CommonEcs.Components.PositionIComponentService;
import dk.sdu.se4.group1.CommonEcs.Components.RenderIComponentService;
import dk.sdu.se4.group1.CommonEcs.World;
import dk.sdu.se4.group1.CommonEcs.EntityID;
import javafx.scene.image.Image;

import java.io.IOException;
import java.io.InputStream;


//Crop Factory creates crops depending on what seed that crop has in its cropcomponent the crop will change sprite.
//The createCrop method gives an entity components relevant to a crop that can then be checked and altered later

public class cropFactory {
    public static void createCrop(World world,int x,int y, SeedType seedType) {
        EntityID CropId = world.createEntity();


        //Adds components
        world.addComponent(CropId, new CropIComponentService(seedType));
        world.addComponent(CropId, new GrowthIComponentService());
        world.addComponent(CropId, new PositionIComponentService(x,y));

        //We try to use the sprite to find our picture of the Crops
        //If it fails we throw an exception. We check for each seedtype to give the correct sprite
        if(seedType == SeedType.CARROT){
            try (InputStream spriteStream = cropFactory.class.getResourceAsStream("/Carrot_1.png")) {
                if (spriteStream == null) {
                    throw new IllegalArgumentException("Sprite not found: /Carrot_1.png");
                }

                world.addComponent(CropId, new RenderIComponentService(new Image(spriteStream)));
            } catch (IOException e) {
                throw new RuntimeException("Failed to close sprite stream", e);
            }
        }

        else if(seedType == SeedType.TOMATO){
            try (InputStream spriteStream = cropFactory.class.getResourceAsStream("/Tomato_1.png")) {
                if (spriteStream == null) {
                    throw new IllegalArgumentException("Sprite not found: /Tomato_1.png");
                }

                world.addComponent(CropId, new RenderIComponentService(new Image(spriteStream)));
            } catch (IOException e) {
                throw new RuntimeException("Failed to close sprite stream", e);
            }
        }


        else if(seedType == SeedType.CHILI){
            try (InputStream spriteStream = cropFactory.class.getResourceAsStream("/Chili_1.png")) {
                if (spriteStream == null) {
                    throw new IllegalArgumentException("Sprite not found: /Chili_1.png");
                }

                world.addComponent(CropId, new RenderIComponentService(new Image(spriteStream)));
            } catch (IOException e) {
                throw new RuntimeException("Failed to close sprite stream", e);
            }
        }

        else if(seedType == SeedType.BEANSPROUT){
            try (InputStream spriteStream = cropFactory.class.getResourceAsStream("/Beansprout_1.png")) {
                if (spriteStream == null) {
                    throw new IllegalArgumentException("Sprite not found: /Beansprout_1.png");
                }

                world.addComponent(CropId, new RenderIComponentService(new Image(spriteStream)));
            } catch (IOException e) {
                throw new RuntimeException("Failed to close sprite stream", e);
            }
        }
    }
}

