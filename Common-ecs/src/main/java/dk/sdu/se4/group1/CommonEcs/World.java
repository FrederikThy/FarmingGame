package dk.sdu.se4.group1.CommonEcs;

import java.awt.*;
import java.util.*;

public class World {
    //Creates entity by using the EntityID record (en record er bare en simpel dataklasse så vi kan printe id ud osv)
    private final Set<EntityID> Entities = new HashSet<>();

    //For each entity store its components in a dictionary like setup. (det er en dictionary inde i en dictionary
    //Dvs for hver entity som key tilhører der en value som også er et dictionary men dette indeholder
    //typen af componenten som key og valuen vil så være den komponent som bliver hold for entitien.)
    private final Map<EntityID, Map<Class<? extends Component>, Component>> entityComponentDictionary = new HashMap<>();

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


    public void addComponent(EntityID entity, Component component){

        //gets the component dictionary for the entity inserted in the parameter
        Map<Class<? extends Component>, Component> entityComponents = entityComponentDictionary.get(entity);

        //checks if entity exists
        if(entityComponents == null){
            throw new IllegalArgumentException("Entity does not exist: " + entity);
        }


        //Hvordan sætter jeg det her ind i entitycomponentdictionary
        //her indsætter man component typen (getclass) som en key og derved component som en value. DVS:
        //Komponenten kan kun have en slags af denne type (eks. postion, croptype, etc..)
        entityComponents.put(component.getClass(), component);
    }

    public Component GetComponent(EntityID entity, Class<? extends Component> componentClass){
        Map<Class<? extends Component>, Component> entityComponents = entityComponentDictionary.get(entity);

        Component result;

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
    public boolean hasComponent(EntityID entity, Class<? extends Component> componentClass){
        Map<Class<? extends Component>, Component> entityComponents = entityComponentDictionary.get(entity);

        //checks if entity exists
        if(entityComponents == null){
            throw new IllegalArgumentException("Entity does not exist: " + entity);
        }

        return entityComponents.containsKey(componentClass);
    }

    public Set<EntityID> getEntities() {
        return Entities;
    }

    public Set<EntityID> getEntitiesWith(Class<? extends Component> componentClass) {
        Set<EntityID> result = new HashSet<>();

        for(EntityID entity : Entities){
            Map<Class<? extends Component>, Component> components =
                    entityComponentDictionary.get(entity);

            if(components != null && components.containsKey(componentClass)){

                result.add(entity);
            }
        }
        return result;
    }
}
