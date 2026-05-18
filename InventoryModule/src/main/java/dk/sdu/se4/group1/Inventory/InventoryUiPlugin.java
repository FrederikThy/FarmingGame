package dk.sdu.se4.group1.Inventory;

import dk.sdu.se4.group1.CommonEcs.IUiPlugin;
import dk.sdu.se4.group1.CommonEcs.World;
import javafx.scene.Node;

public class InventoryUiPlugin implements IUiPlugin {
    @Override
    public Node createNode(World world){
        return new InventoryPlugin(world);
    }
}
