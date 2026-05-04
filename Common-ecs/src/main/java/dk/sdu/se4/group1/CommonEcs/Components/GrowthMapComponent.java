package dk.sdu.se4.group1.CommonEcs.Components;

import dk.sdu.se4.group1.CommonEcs.Component;

public class GrowthMapComponent implements Component{

    private int unlockedMapLevel = 0;

    public int getUnlockedMapLevel() {
        return unlockedMapLevel;
    }

    public boolean hasUnlockMap(int level) {
    validateLevel(level);
    return unlockedMapLevel >= level;
    }

    public boolean unlockMap(int level) {
    validateLevel(level);

    if (level <= unlockedMapLevel) {
    return false; //Returns falls if the map hasn't been unlocked
        }
        unlockedMapLevel = level;
        return true; //returns true if our map has been added
    }

    public double getGrowthRate() {
        return switch (unlockedMapLevel){
            case 1 -> 1.25;
            case 2 -> 1.50;
            default -> 1.0;
        };
    }

    private void validateLevel(int level) {
        if (level < 1 || level > 2) {
            throw new IllegalArgumentException("Level must be between 1 and 2");
        } //Debugging to make sure we have the right levels
    }

}
