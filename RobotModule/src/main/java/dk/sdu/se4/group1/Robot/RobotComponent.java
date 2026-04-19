package dk.sdu.se4.group1.Robot;

import dk.sdu.se4.group1.CommonEcs.Component;

public class RobotComponent implements Component {
    // x and y removed — position lives in PositionComponent only
    private final int mapLength;
    private final int mapHeight;

    public RobotComponent(int mapLength, int mapHeight) {
        this.mapLength = mapLength;
        this.mapHeight = mapHeight;
    }

    public int getMapLength() { return mapLength; }
    public int getMapHeight() { return mapHeight; }
}