package dk.sdu.se4.group1.Tools;

import dk.sdu.se4.group1.CommonEcs.IUiPluginService;
import dk.sdu.se4.group1.CommonEcs.World;
import javafx.scene.Node;

public class SpeedToolUiPluginService implements IUiPluginService {

    @Override
    public Node createNode(World world) {
        return new SpeedToolPlugin(world);
    }
}
