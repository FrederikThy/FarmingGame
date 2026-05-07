package dk.sdu.se4.group1.Crops;

import dk.sdu.se4.group1.CommonApi.SeedType;
import dk.sdu.se4.group1.CommonEcs.Components.CropComponent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CropsComponentTests {

    /** Test for initial values of each crop */
    @Test
    void growthStageTest() {
        GrowthComponent growth = new GrowthComponent();

        assertEquals(1,   growth.growthStage,"Initial growthStage must be 1");
        assertEquals(0.0, growth.elapsedGrowthTime, "Initial elapsedGrowthTime must be 0");
        assertEquals(5.0, growth.growthTime,"Default growthTime must be 5 seconds");
    }

    /** Test for stage changing time */
    @Test
    void elapsedTimeTest() {
        GrowthComponent growth = new GrowthComponent();
        growth.elapsedGrowthTime += 4.9;
        assertEquals(1, growth.growthStage,
                "Stage must remain 1 when elapsed time has not reached growthTime");
    }
}
