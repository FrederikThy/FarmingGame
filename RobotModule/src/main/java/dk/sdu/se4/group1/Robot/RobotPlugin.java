package dk.sdu.se4.group1.Robot;

import java.util.Random;

public class Robot {

    //RANDOM giver random tal, som vi bruger til at give en destionation til vores robot
    private static final Random RANDOM = new Random();

    private static final int screenWidth = 800;
    private static final int screenHeight = 600;

    private int x;
    private int y;
    private final double speed;

    private int xDestination;
    private int yDestination;

    public Robot(double speed, int startX, int startY) {
        this.speed = speed;
        this.x = startX;
        this.y = startY;

        //Vi laver en random destination for at robotten kan bevæge sig
        setRandomDestination();
    }

    public void moveRandomly() {
        double dx = xDestination - x;
        double dy = yDestination - y;
        double distance = Math.hypot(dx, dy);


        //Debugg for at se om vores robot rent faktisk bevæger sig, og for at se hvor den er henne på skærmen
        System.out.println("Position: (" + x + ", " + y + ")");

        if (distance <= speed || distance == 0) {
            x = xDestination;
            y = yDestination;
            setRandomDestination();
            return;
        }
        x += (dx / distance) * speed;
        y += (dy / distance) * speed;
    }

    //Her oprettes en random destination for robotten
    private void setRandomDestination() {
        xDestination = RANDOM.nextInt(screenWidth);
        yDestination = RANDOM.nextInt(screenHeight);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getSpeed() {
        return speed;
    }
}

