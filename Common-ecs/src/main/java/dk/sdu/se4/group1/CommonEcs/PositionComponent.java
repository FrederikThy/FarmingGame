package dk.sdu.se4.group1.CommonEcs;

public class PositionComponent implements Component {

    public int x;
    public int y;

    public PositionComponent(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // getter methods 
    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }
}
