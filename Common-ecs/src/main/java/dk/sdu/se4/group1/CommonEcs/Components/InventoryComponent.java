package dk.sdu.se4.group1.CommonEcs.Components;

import dk.sdu.se4.group1.CommonApi.SeedType;
import dk.sdu.se4.group1.CommonEcs.Component;

import java.awt.*;
import java.util.EnumMap;
import java.util.Map;

public class InventoryComponent implements Component {
    private final EnumMap<SeedType, Integer> harvestedCrops = new EnumMap<>(SeedType.class);
    private int Wallet =500;

    public void addHarvest(SeedType seedType) {
        harvestedCrops.merge(seedType, 1, Integer::sum);
    }

    public Map<SeedType, Integer> getHarvestedCrops() {
        return harvestedCrops;
    }

    public void removeFromWallet(int amount){
        Wallet -=amount;
    }

    public void addToWallet(int amount){
        Wallet +=amount;
    }
}
