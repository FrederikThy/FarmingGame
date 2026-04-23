package dk.sdu.se4.group1.CommonEcs;

public interface IMoniteringService {
    String getName(); //Name we want

    void reset(); //When resetting the game

    void OnFrame(double dt); //Used from GameLoop

    double getValue(); //The metric value we want

    String getUnit(); // Fx "FPS" or "%"
}
