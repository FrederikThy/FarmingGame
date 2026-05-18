package dk.sdu.se4.group1.Shop;

import dk.sdu.se4.group1.CommonEcs.IUiPlugin;

import dk.sdu.se4.group1.CommonEcs.World;
import javafx.scene.Node;

// Used to return ShopPlugin since it contains UI.
public class ShopUiPlugin implements IUiPlugin {
    @Override
    public Node createNode(World world){
        return new ShopPlugin(world);
    }
}
