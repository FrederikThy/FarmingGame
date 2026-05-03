package dk.sdu.se4.group1.CommonEcs.Components;

import dk.sdu.se4.group1.CommonApi.SeedType;
import dk.sdu.se4.group1.CommonEcs.Component;

import java.awt.*;
import java.util.EnumMap;
import java.util.Map;

public class InventoryComponent implements Component {
    private final EnumMap<SeedType, Integer> seedsStorage = new EnumMap<>(SeedType.class);
    private final EnumMap<SeedType, Integer> harvestedCrops = new EnumMap<>(SeedType.class);
    private int Wallet =500;

    public void addSeeds(SeedType seedType,int amount) {
        seedsStorage.merge(seedType, amount, Integer::sum);
    }

    public void addSeeds(SeedType seedType) {
        seedsStorage.merge(seedType, 1, Integer::sum);
    }

    public Map<SeedType, Integer> getHarvestedCrops() {
        return harvestedCrops;
    }

    /*public void RemoveItem(int entityId) {
        harvestedCrops.remove(seedType, 1, Integer::sum);
    }*/
    public Map<SeedType,Integer> getSeedStorage(){return seedsStorage;}

    public boolean removeSeedsFromStorage(SeedType seedType, int amount) {
        int currentAmount = seedsStorage.getOrDefault(seedType, 0);

        if (currentAmount < amount) {
            return false;
        }

        int newAmount = currentAmount - amount;

        // Rettede den til, at den fjernede det fra harvestedCrops til at den fjerner det fra seedsStorage.
        if (newAmount == 0) {
            seedsStorage.remove(seedType);
        } else {
            seedsStorage.put(seedType, newAmount);
        }

        return true;
    }

    public void addHarvest(SeedType seedType, int amount) {
        harvestedCrops.merge(seedType, amount, Integer::sum);
    }
    public void addHarvest(SeedType seedType) {
        harvestedCrops.merge(seedType, 1, Integer::sum);
    }
    public boolean removeHarvest(SeedType seedType, int amount) {
        int currentAmount = harvestedCrops.getOrDefault(seedType, 0);

        if (currentAmount < amount) {
            return false;
        }

        int newAmount = currentAmount - amount;

        if (newAmount == 0) {
            harvestedCrops.remove(seedType);
        } else {
            harvestedCrops.put(seedType, newAmount);
        }

        return true;
    }
    public void removeFromWallet(int amount){
        Wallet -=amount;
    }

    public void addToWallet(int amount){
        Wallet +=amount;
    }

    public int getWallet() {
        return Wallet;
    }
}
