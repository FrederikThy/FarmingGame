package dk.sdu.se4.group1.CommonEcs;


import javafx.scene.Node;

// Interface to use when instantiating UI element plugins
// Node is the base class for all UI elements
public interface IUiPlugin {
    Node createNode(World world);
}
