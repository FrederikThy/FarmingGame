package dk.sdu.se4.group1.CommonEcs;

public class TileComponent implements Component {

    boolean isWalkable = true;

    public boolean isWalkable(){
        return this.isWalkable;
    }

    public void setWalkable(boolean walkable) {
        this.isWalkable = walkable;
    }
}
