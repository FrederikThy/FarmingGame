package dk.sdu.se4.group1.CommonEcs.Components;

import dk.sdu.se4.group1.CommonApi.SeedType;import dk.sdu.se4.group1.CommonEcs.Component;

//MOST COMPONENTS HAVE BEEN MOVED TO COMMONECS SINCE IF A COMPONENT NEEDS TO SHARE DATA WITH ANOTHER SYSTEM THAT IS NECESSARY

public class RobotComponent implements Component {

        private int x = 0;
        private int y = 0;
        private final int mapLength;
        private final int mapHeight;


        public RobotComponent(int mapLength, int mapHeight) {
            this.mapHeight = mapHeight;
            this.mapLength = mapLength;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public SeedType seedType;


    }



