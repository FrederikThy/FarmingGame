package dk.sdu.se4.group1.CommonEcs;

import javafx.scene.paint.Color;

public class RenderComponent implements Component{
    public Color color;

    public RenderComponent(Color ChooseColor){
        this.color = ChooseColor;
    }
}
