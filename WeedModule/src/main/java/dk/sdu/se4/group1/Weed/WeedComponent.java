package dk.sdu.se4.group1.Weed;

import dk.sdu.se4.group1.CommonEcs.Component;

public class WeedComponent implements Component {

    private int x = 0;
    private int y = 0;

    private final int mapLength;

    private final int mapHeight;

    public WeedComponent(int mapLength, int mapHeight) {
        this.mapHeight = mapHeight;
        this.mapLength = mapLength;
    }
}
