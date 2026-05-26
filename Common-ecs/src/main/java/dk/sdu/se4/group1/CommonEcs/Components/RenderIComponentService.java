package dk.sdu.se4.group1.CommonEcs.Components;

import dk.sdu.se4.group1.CommonEcs.IComponentService;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

//MOST COMPONENTS HAVE BEEN MOVED TO COMMONECS SINCE IF A COMPONENT NEEDS TO SHARE DATA WITH ANOTHER SYSTEM THAT IS NECESSARY

public class RenderIComponentService implements IComponentService {
    public Image sprite;
    public Color color;

    public RenderIComponentService(Color ChooseColor) {
        this.color = ChooseColor;
        this.sprite = null;
    }

    public RenderIComponentService(Image sprite) {
        this.sprite = sprite;
        this.color = null;
    }
}