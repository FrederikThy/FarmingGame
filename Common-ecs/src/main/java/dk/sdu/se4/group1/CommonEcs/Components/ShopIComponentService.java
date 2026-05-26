package dk.sdu.se4.group1.CommonEcs.Components;

import dk.sdu.se4.group1.CommonEcs.IComponentService;

import java.util.ArrayList;
import java.util.List;

public class ShopIComponentService implements IComponentService {
    private final List<ShopOfferIComponentService> shopItems = new ArrayList<>();

    public void addShopItem(IComponentService IComponentService, int pris) {
        var item = new ShopOfferIComponentService(IComponentService,pris);
        shopItems.add(item);
    }

    public List<ShopOfferIComponentService> getShopItems() {
        return shopItems;
    }

    public void removeFromShopItem(IComponentService IComponentService){
        //if(shopItems.stream().findAny().stream().
    }



}

