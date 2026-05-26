package dk.sdu.se4.group1.CommonEcs;

import dk.sdu.se4.group1.CommonApi.SeedType;
import dk.sdu.se4.group1.CommonEcs.Components.PositionIComponentService;
import dk.sdu.se4.group1.CommonEcs.Components.RobotIComponentService;

import java.util.*;

public class World {
    //Creates entity by using the EntityID record (en record er bare en simpel dataklasse så vi kan printe id ud osv)
    private final Set<EntityID> Entities = new HashSet<>();

    //qeue that stores plant requests added from robots. If a request is contained it is checked the plant is planted and the request is deleted by the cropsystem
    Queue<SeedRequest> SeedQueue = new LinkedList<>();

    //For each entity store its components in a dictionary like setup. (det er en dictionary inde i en dictionary
    //Dvs for hver entity som key tilhører der en value som også er et dictionary men dette indeholder
    //typen af componenten som key og valuen vil så være den komponent som bliver hold for entitien.)
    private final Map<EntityID, Map<Class<? extends IComponentService>, IComponentService>> entityComponentDictionary = new HashMap<>();

    private int NextEntityId = 0;

    //Creates a new instance of an entity
    public EntityID createEntity(){
        // Vores Entities overskrev hinanden før, men det gør de ikke længere
        EntityID id = new EntityID(NextEntityId);
        NextEntityId++;
        Entities.add(id);

        //put the entity in the dictionary for components:
        entityComponentDictionary.put(id, new HashMap<>());

        return id;
    }


    public void addComponent(EntityID entity, IComponentService IComponentService){

        //gets the component dictionary for the entity inserted in the parameter
        Map<Class<? extends IComponentService>, IComponentService> entityComponents = entityComponentDictionary.get(entity);

        //checks if entity exists
        if(entityComponents == null){
            throw new IllegalArgumentException("Entity does not exist: " + entity);
        }


        //Hvordan sætter jeg det her ind i entitycomponentdictionary
        //her indsætter man component typen (getclass) som en key og derved component som en value. DVS:
        //Komponenten kan kun have en slags af denne type (eks. postion, croptype, etc..)
        entityComponents.put(IComponentService.getClass(), IComponentService);
    }

    public IComponentService GetComponent(EntityID entity, Class<? extends IComponentService> componentClass){
        Map<Class<? extends IComponentService>, IComponentService> entityComponents = entityComponentDictionary.get(entity);

        IComponentService result;

        if(entityComponents.containsKey(componentClass)){
            result = entityComponents.get(componentClass);
        }

        else{
            throw new IllegalArgumentException("Entity does not have that component: " + entity);
        }

        return result;
    }


    //To use this method call:
    //World.hasComponent(*entityID*, new *componentType*)
    public boolean hasComponent(EntityID entity, Class<? extends IComponentService> componentClass){
        Map<Class<? extends IComponentService>, IComponentService> entityComponents = entityComponentDictionary.get(entity);

        //checks if entity exists
        if(entityComponents == null){
            throw new IllegalArgumentException("Entity does not exist: " + entity);
        }

        return entityComponents.containsKey(componentClass);
    }

    public Set<EntityID> getEntities() {
        return Entities;
    }

    public Set<EntityID> getEntitiesWith(Class<? extends IComponentService> componentClass) {
        Set<EntityID> result = new HashSet<>();

        for(EntityID entity : Entities){
            Map<Class<? extends IComponentService>, IComponentService> components =
                    entityComponentDictionary.get(entity);

            if(components != null && components.containsKey(componentClass)){

                result.add(entity);
            }
        }
        return result;
    }

    public boolean isTileFree(int x, int y){
        for(EntityID entityID : Entities){
            if(hasComponent(entityID, PositionIComponentService.class)){
                PositionIComponentService pos =(PositionIComponentService) GetComponent(entityID, PositionIComponentService.class);
                if(pos.x == x && pos.y == y){
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * Like isTileFree but ignores entities with a RobotComponent.
     * Used by PathfindingSystem so robots can plan paths through tiles
     * occupied by other robots; only static obstacles (crops, weeds) block routes.
     */
    public boolean isTileFreeIgnoringRobots(int x, int y){
        for(EntityID entityID : Entities){
            if(hasComponent(entityID, PositionIComponentService.class)){
                if(hasComponent(entityID, RobotIComponentService.class)){
                    continue;
                }
                PositionIComponentService pos = (PositionIComponentService) GetComponent(entityID, PositionIComponentService.class);
                if(pos.x == x && pos.y == y){
                    return false;
                }
            }
        }
        return true;
    }

    //Adds the a seed request to the queue
    public void addSeedToQueue(int x, int y, SeedType seedType){
        SeedQueue.add(new SeedRequest(seedType, x, y));
    }


    //poll() takes the first added SeedRequest and returns it. It then removes it from the queue
    public SeedRequest CheckSeedQueue(){
        return SeedQueue.poll();
    }

    public void RemoveEntity(EntityID entity){
        // Fjerner entity fra listen over entities.
        Entities.remove(entity);

        // Fjerner alle components til den entity.
        entityComponentDictionary.remove(entity);
    }
}
