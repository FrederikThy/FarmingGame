package dk.sdu.se4.group1.CommonEcs;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;


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