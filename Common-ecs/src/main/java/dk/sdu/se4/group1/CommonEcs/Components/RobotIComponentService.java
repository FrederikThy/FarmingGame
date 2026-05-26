package dk.sdu.se4.group1.CommonEcs.Components;

import dk.sdu.se4.group1.CommonApi.SeedType;import dk.sdu.se4.group1.CommonEcs.IComponentService;

//MOST COMPONENTS HAVE BEEN MOVED TO COMMONECS SINCE IF A COMPONENT NEEDS TO SHARE DATA WITH ANOTHER SYSTEM THAT IS NECESSARY

public class RobotIComponentService implements IComponentService {

        private int x = 0;
        private int y = 0;
        private final int mapLength;
        private final int mapHeight;

        public final RobotType robotType;

        public RobotIComponentService(int mapLength, int mapHeight, RobotType robotType) {
            this.mapHeight = mapHeight;
            this.mapLength = mapLength;
            this.robotType = robotType;
        }

        public String GetType(){
            return switch (robotType){
                case HARVEST ->  "HARVEST";
                case PLANT ->   "PLANT";
                case WEED_REMOVER ->   "WEED";
            };
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public SeedType seedType;


    }



