package dk.sdu.se4.group1.Inventory;

import dk.sdu.se4.group1.CommonEcs.IUiPluginService;
import dk.sdu.se4.group1.CommonEcs.World;
import javafx.scene.Node;

public class InventoryUiPluginService implements IUiPluginService {
    @Override
    public Node createNode(World world){
        return new ItemsPlugin(world);
    }
}
