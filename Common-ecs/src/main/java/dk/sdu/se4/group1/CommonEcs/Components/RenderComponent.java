package dk.sdu.se4.group1.CommonEcs.Components;

import dk.sdu.se4.group1.CommonEcs.Component;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

//MOST COMPONENTS HAVE BEEN MOVED TO COMMONECS SINCE IF A COMPONENT NEEDS TO SHARE DATA WITH ANOTHER SYSTEM THAT IS NECESSARY

public class RenderComponent implements Component {
    public Image sprite;
    public Color color;

    public RenderComponent(Color ChooseColor) {
        this.color = ChooseColor;
        this.sprite = null;
    }

    public RenderComponent(Image sprite) {
        this.sprite = sprite;
        this.color = null;
    }
}