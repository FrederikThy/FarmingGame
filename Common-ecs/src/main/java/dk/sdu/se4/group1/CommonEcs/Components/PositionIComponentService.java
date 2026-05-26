package dk.sdu.se4.group1.CommonEcs.Components;

import dk.sdu.se4.group1.CommonEcs.IComponentService;

//MOST COMPONENTS HAVE BEEN MOVED TO COMMONECS SINCE IF A COMPONENT NEEDS TO SHARE DATA WITH ANOTHER SYSTEM THAT IS NECESSARY

public class PositionIComponentService implements IComponentService {

    public int x;
    public int y;

    public PositionIComponentService(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
