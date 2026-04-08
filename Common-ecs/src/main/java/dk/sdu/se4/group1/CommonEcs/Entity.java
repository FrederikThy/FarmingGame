package dk.sdu.se4.group1.CommonEcs;

import java.util.UUID;

public class Entity {
    private final UUID id;

    public Entity() {
        this.id = UUID.randomUUID();
    }

    public String getID() {
        return id.toString();
    }

 
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Entity entity = (Entity) obj;
        return id.equals(entity.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}