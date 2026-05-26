package dk.sdu.se4.group1.CommonEcs;


import dk.sdu.se4.group1.CommonApi.SeedType;

import java.util.List;

public interface IShopService {
    /*static IShopService[] sharedInstance = {null};

    static void setInstance(IShopService instance) {
        sharedInstance[0] = instance;
    }

    static IShopService getInstance() {
        return sharedInstance[0];
    }*/
    void openShop(World world);
    List<EntityID> getShopItems();
    boolean buyItem(int EntityID,Item item,int quantity);
    int SellItem(int EntityID, int quantity);
    int getBuyPrice(SeedType type);
    int getSellPrice(int entityID,int amount);
    int getSellPrice(SeedType seedType, int amount);
    boolean isAvailable(int price,int wallet);
}
