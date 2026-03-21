package dk.sdu.se4.group1.CommonEcs;

import java.util.ArrayList;
import java.util.List;

public class World {
    private final List<Object> entities = new ArrayList<>();

    public void AddEntity(Object entity) {
        entities.add(entity);
    }

    public List<Object> getEntities() {
        return entities;
    }
}
