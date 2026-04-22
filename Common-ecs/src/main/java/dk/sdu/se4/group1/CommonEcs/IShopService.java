package dk.sdu.se4.group1.CommonEcs;


import java.util.List;

public interface IShopService {
    static IShopService[] sharedInstance = {null};

    static void setInstance(IShopService instance) {
        sharedInstance[0] = instance;
    }

    static IShopService getInstance() {
        return sharedInstance[0];
    }
    void openShop();
    List<Item> getShopItems(int entityID);
    boolean buyItem(int EntityID,Item item);
    boolean buyItem(int EntityID,Item item,int quantity);
    int SellItem(int EntityID, Item item, int quantity);
    int getBuyPrice(Item item);
    int getSellPrice(int entityID,Item item);
}
