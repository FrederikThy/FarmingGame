package dk.sdu.se4.group1.CommonEcs.Components;

import dk.sdu.se4.group1.CommonEcs.Component;

public class RobotStatusComponent implements Component {
    public String status;

    public RobotStatusComponent(String status) {
        this.status = status;
    }

}
