package dk.sdu.se4.group1.Monitoring;

import dk.sdu.se4.group1.CommonEcs.IMoniteringService;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class FPSCounter extends Label implements IMoniteringService {
    private int frames;
    private double framesPerSecond;
    private double timeAccumulator; //A double that is counting when input is true
                                    //And is paused when the input is false

    public FPSCounter(){
        super("FPS 0"); //"super" refers to the parent class.
        //Here it is calling the constructor of the Label class to set the text to "FPS 0".
        setTextFill(Color.LIGHTGREEN);
        setFont(Font.font("Verdana", FontWeight.LIGHT, 10));
        setLayoutX(10);
        setLayoutY(10);
        setMouseTransparent(true);
    }

    @Override
    public String getName() {
        return "FPS";
    }

    @Override
    public void reset () {
        frames = 0;
        timeAccumulator = 0;
        framesPerSecond = 0;
        setText("FPS 0");
    }

    @Override
    public void OnFrame(double dt) {
        frames++; //counting current frames
        timeAccumulator += dt;
        if (timeAccumulator >= 1.0) { //updates our fps every second
            framesPerSecond = frames / timeAccumulator; //calculate our fps
            setText(String.format("FPS: %.0f", framesPerSecond)); //Shows our fps on the screen
            frames = 0; //resetting the frame count
            timeAccumulator = 0; //resetting the time count
        }
    }

    @Override
    public double getValue () {
        return framesPerSecond;
    }
    @Override
    public String getUnit() {
        return "FPS";
    }

}
