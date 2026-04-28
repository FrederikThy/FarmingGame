package dk.sdu.se4.group1.CommonEcs;

import java.util.List;

public interface IInventoryService {

    boolean additem(EntityID entityID);
    boolean additem(EntityID entityID,int quantity);
    void removeItem(Item item);
    void showInventory(World world);
    List<InviItme> getItems();
    boolean hasItem(Item item);
    void AddCoins(int coins);
    void RemoveCoins(int coins);
    int getWallet();
    void AddHarvest(EntityID entityID,Item crop,int quantity);
    List<InviItme> getHarvest();
    void sellAllHarvest(EntityID entityID);
}
