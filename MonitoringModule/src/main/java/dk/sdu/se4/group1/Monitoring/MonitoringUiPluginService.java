package dk.sdu.se4.group1.Monitoring;

import dk.sdu.se4.group1.CommonEcs.IUiPluginService;
import dk.sdu.se4.group1.CommonEcs.World;
import javafx.scene.Node;

public class MonitoringUiPluginService implements IUiPluginService {
    @Override
    public Node createNode(World world) {
        return new MonitoringOverlay(world);
    }
}

