package dk.sdu.se4.group1.Shop;

import dk.sdu.se4.group1.CommonEcs.IUiPlugin;
import dk.sdu.se4.group1.CommonEcs.World;
import javafx.scene.Node;

/**
 * UI Plugin for Shop module - discoverable via ServiceLoader.
 * Creates the shop button UI element.
 */
public class ShopUIPlugin implements IUiPlugin {

    @Override
    public Node createNode(World world) {
        return new ShopPlugin(world);
    }
}

