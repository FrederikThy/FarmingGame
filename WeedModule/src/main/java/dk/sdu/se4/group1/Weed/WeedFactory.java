package dk.sdu.se4.group1.Weed;

import dk.sdu.se4.group1.CommonEcs.Components.WeedComponent;
import dk.sdu.se4.group1.CommonEcs.EntityID;
import dk.sdu.se4.group1.CommonEcs.World;
import dk.sdu.se4.group1.CommonEcs.Components.PositionComponent;
import dk.sdu.se4.group1.CommonEcs.Components.RenderComponent;
import javafx.scene.image.Image;

import java.io.IOException;
import java.io.InputStream;

public class WeedFactory {
    // Da vi ikke vil have en forudbestemt x og y koordinat for weeds
    // bruger vi bare x og y som koordinater i positionComponent
    public static void CreateWeed(World world, int x, int y) {
        EntityID WeedId = world.createEntity();

        world.addComponent(WeedId, new WeedComponent());
        world.addComponent(WeedId, new PositionComponent(x, y));


        try (InputStream spriteStream = WeedFactory.class.getResourceAsStream("/Weed.png")) {
            if (spriteStream == null) {
                throw new IllegalArgumentException("Sprite not found: Weed.png");
            }
            //Here we add the render the image from the code above
            //Throws an exception if it fails to load HrFlink
            world.addComponent(WeedId, new RenderComponent(new Image(spriteStream)));
        } catch (IOException e) {
            throw new RuntimeException("Failed to close sprite stream", e);
        }
    }
}
