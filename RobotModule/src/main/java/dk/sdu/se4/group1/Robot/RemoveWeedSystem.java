package dk.sdu.se4.group1.Robot;

import dk.sdu.se4.group1.CommonEcs.Components.PositionComponent;
import dk.sdu.se4.group1.CommonEcs.Components.RemoveCropComponent;
import dk.sdu.se4.group1.CommonEcs.Components.WeedComponent;
import dk.sdu.se4.group1.CommonEcs.EcsSystem;
import dk.sdu.se4.group1.CommonEcs.EntityID;
import dk.sdu.se4.group1.CommonEcs.World;


public class RemoveWeedSystem implements EcsSystem {


    @Override
    public void update(World world, double deltaTime) {
        for (EntityID entity : world.getEntitiesWith(RemoveCropComponent.class)){
            PositionComponent pos = (PositionComponent) world.GetComponent(entity, PositionComponent.class);

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
    private EntityID FindWeedAtPosition(World world, PositionComponent pos){
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
        for (EntityID entity : world.getEntitiesWith(WeedComponent.class)){
            PositionComponent posWeed = (PositionComponent) world.GetComponent(entity, PositionComponent.class);
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
