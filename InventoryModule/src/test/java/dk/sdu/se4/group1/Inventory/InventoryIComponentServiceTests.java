package dk.sdu.se4.group1.Inventory;

import dk.sdu.se4.group1.CommonApi.SeedType;
import dk.sdu.se4.group1.CommonEcs.Components.InventoryIComponentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InventoryIComponentServiceTests {

    private InventoryIComponentService inventory;

    @BeforeEach
    void setUp() {
        inventory = new InventoryIComponentService();
    }

   /** Add and remove seeds test */
    @Test
    void addAndRemoveSeedsTest() {
        inventory.addSeeds(SeedType.CARROT, 5);
        inventory.addSeeds(SeedType.CARROT, 3);

        assertEquals(8, inventory.getSeedStorage().get(SeedType.CARROT),
                "Seeds from multiple addSeeds calls should sum");

        boolean removed = inventory.removeSeedsFromStorage(SeedType.CARROT, 6);
        assertTrue(removed, "Removal should succeed when stock is sufficient");
        assertEquals(2, inventory.getSeedStorage().get(SeedType.CARROT), "Remaining stock should be 2");
    }

    /** Remove more seeds than available inventory, and remaining seeds are unchanged after removal fails */
    @Test
    void removeSeedsFromStorage() {
        inventory.addSeeds(SeedType.TOMATO, 2);

        boolean removed = inventory.removeSeedsFromStorage(SeedType.TOMATO, 5);

        assertFalse(removed, "Removal should fail");
        assertEquals(2, inventory.getSeedStorage().get(SeedType.TOMATO),"Stock must not change when removal fails");
    }

    /** Wallet starts at 10000 and should reflect any additions or removals*/
    @Test
    void addAndRemovewalletTest() {
        assertEquals(10000, inventory.getWallet(), "Wallet starts at 10000 coins");

        inventory.addToWallet(5000);
        assertEquals(15000, inventory.getWallet(), "After adding 500 coins wallet should be 15000");

        inventory.removeFromWallet(2000);
        assertEquals(13000, inventory.getWallet(), "After removing 200 coins wallet should be 13000");
    }
}