package dk.sdu.se4.group1.Shop;

import dk.sdu.se4.group1.CommonEcs.IUiPluginService;

import dk.sdu.se4.group1.CommonEcs.World;
import javafx.scene.Node;

// Used to return ShopPlugin since it contains UI.
public class ShopUiPluginService implements IUiPluginService {
    @Override
    public Node createNode(World world){
        return new ShopPlugin(world);
    }
}
