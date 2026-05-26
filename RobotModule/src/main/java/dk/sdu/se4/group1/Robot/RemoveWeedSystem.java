package dk.sdu.se4.group1.Robot;

import dk.sdu.se4.group1.CommonEcs.Components.PositionIComponentService;
import dk.sdu.se4.group1.CommonEcs.Components.RemoveCropIComponentService;
import dk.sdu.se4.group1.CommonEcs.Components.WeedIComponentService;
import dk.sdu.se4.group1.CommonEcs.IEntityProcessingService;
import dk.sdu.se4.group1.CommonEcs.EntityID;
import dk.sdu.se4.group1.CommonEcs.World;


public class RemoveWeedSystem implements IEntityProcessingService {


    @Override
    public void update(World world, double deltaTime) {
        for (EntityID entity : world.getEntitiesWith(RemoveCropIComponentService.class)){
            PositionIComponentService pos = (PositionIComponentService) world.GetComponent(entity, PositionIComponentService.class);

            EntityID weed = FindWeedAtPosition(world, pos);

            if (weed != null){
                world.RemoveEntity(weed);
            }
        }
    }

    // Bruger FindWeedAt til at tjekke efter weeds. Vi bruger herefter FindWeedAtPosition i update, hvor vi herefter
    // kalder RemoveEntity i world, for at fjerne weed'en.
    // Vi tjekker 4 gange. En gang for hver retning.
    // Det ser ikke så pænt ud, og det er udelukkende fordi jeg er i tvivl om en af vores design beslutninger og ikke gider at bruge mere tid på det.
    // Lige nu kan robots ikke bevæge sig oven på weeds og crops. Det giver mening når de bevæger sig randomly.
    // Men når vi får implementeret vores algoritme, giver det ikke længere mening. Der synes jeg vi skal ændre det til
    // at robots kan bevæge sig frit på mappet.
    // Det her kommer også til at se meget pænere ud efter det.
    private EntityID FindWeedAtPosition(World world, PositionIComponentService pos){
        //Oppe
        EntityID weed = FindWeedAt(world, pos.x, pos.y - 1);
        if (weed != null){
            return weed;
        }

        // Ned
        weed =  FindWeedAt(world, pos.x, pos.y + 1);
        if (weed != null){
            return weed;
        }

        // Venstre
        weed = FindWeedAt(world, pos.x - 1, pos.y);
        if (weed != null){
            return weed;
        }

        //Højre
        weed = FindWeedAt(world, pos.x + 1, pos.y);
        if (weed != null){
            return weed;
        }

        return null;
    }

    // Bruger til at finde alle entities med WeedComponent.
    private EntityID FindWeedAt(World world, int x, int y){
        for (EntityID entity : world.getEntitiesWith(WeedIComponentService.class)){
            PositionIComponentService posWeed = (PositionIComponentService) world.GetComponent(entity, PositionIComponentService.class);
            if(posWeed ==  null){
                continue;
            }
            if(posWeed.x == x && posWeed.y == y){
                return entity;
            }
        }
        return null;
    }

}
