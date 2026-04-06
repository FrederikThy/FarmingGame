package dk.sdu.se4.group1.Robot;

import dk.sdu.se4.group1.CommonEcs.Component;

import java.util.Random;

public class RobotComponent implements Component {

        private int x = 0;
        private int y = 0;

        private int xDestination;
        private int yDestination;

        private final int mapLength;
        private final int mapHeight;


        public RobotComponent(int mapLength, int mapHeight) {
            this.mapHeight = mapHeight;
            this.mapLength = mapLength;
        }



        //TEST METODER :)
        private boolean moveXNext = true;

        public void move(int xMove, int yMove) {
            xDestination = x + xMove;
            yDestination = y + yMove;

            // Set x or y to new value if possible depending on movement direction
            if(xDestination == x){
                if(yDestination < mapHeight && yDestination > 0){
                    y = yDestination;
                }

                else {
                    return;
                }
            }


            if(yDestination == y){
                if(xDestination < mapLength && xDestination > 0){
                    x = xDestination;
                }

                else {
                    return;
                }
            }
        }

        //TEST METODE SLUT :)




        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }
    }



