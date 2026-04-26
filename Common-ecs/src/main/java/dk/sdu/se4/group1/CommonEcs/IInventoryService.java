package dk.sdu.se4.group1.CommonEcs;

import java.util.List;

public interface IInventoryService {
    static IInventoryService[] sharedInstance = {null};

    static void setInstance(IInventoryService instance) {
        sharedInstance[0] = instance;
    }

    static IInventoryService getInstance() {
        return sharedInstance[0];
    }
    boolean additem(Item item);
    boolean additem(Item item,int quantity);
    void removeItem(Item item);
    void showInvi();
    List<InviItme> getItems();
    boolean hasItem(Item item);
    void AddCoins(int coins);
    void RemoveCoins(int coins);
    int getWallet();
    void AddHarvest(EntityID entityID,Item crop,int quantity);
    List<InviItme> getHarvest();
    void sellAllHarvest(EntityID entityID);
}
