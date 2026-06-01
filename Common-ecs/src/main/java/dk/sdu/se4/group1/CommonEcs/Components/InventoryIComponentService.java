package dk.sdu.se4.group1.CommonEcs.Components;

import dk.sdu.se4.group1.CommonApi.SeedType;
import dk.sdu.se4.group1.CommonEcs.IComponentService;

import java.util.EnumMap;
import java.util.Map;

public class InventoryIComponentService implements IComponentService {


    private int Wallet = 10000;

    private final EnumMap<SeedType, Integer> seedsStorage = new EnumMap<>(SeedType.class);
    private final EnumMap<SeedType, Integer> harvestedCrops = new EnumMap<>(SeedType.class);

    public void addSeeds(SeedType seedType,int amount) {
        seedsStorage.merge(seedType, amount, Integer::sum);
    }

    public void addSeeds(SeedType seedType) {
        seedsStorage.merge(seedType, 1, Integer::sum);
    }


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
    public Map<SeedType,Integer> getSeedStorage(){return seedsStorage;}
    public Map<SeedType,Integer> getharvestedCrops(){return harvestedCrops;}

    /*
    public void addComponentItem(EntityID entityID, Component component, int count) {
        InventoryKey key = createKey(component);

        EntityCount existing = items.get(key);

        if (existing == null) {
            items.put(key, new EntityCount(entityID, count));
            return;
        }

        items.put(key, new EntityCount(existing.entityID(), existing.count() + count));
    }

    public boolean removeComponentItem(Component component, int count) {
        InventoryKey key = createKey(component);

        EntityCount existing = items.get(key);

        if (existing == null) {
            return false;
        }

        if (existing.count() < count) {
            return false;
        }

        int newCount = existing.count() - count;

        if (newCount == 0) {
            items.remove(key);
        } else {
            items.put(key, new EntityCount(existing.entityID(), newCount));
        }

        return true;
    }


    private InventoryKey createKey(Component component) {
        if (component instanceof CropComponent cropComponent) {
            return new InventoryKey(
                    CropComponent.class,
                    cropComponent.seedType,
                    cropComponent.isHarvestable
            );
        }

        return new InventoryKey(
                component.getClass(),
                null,
                false
        );
    }

    public record EntityCount(EntityID entityID, int count) {
    }

    public Map<InventoryKey, EntityCount> getItemsByComponent() {
        return items;
    }*/

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
