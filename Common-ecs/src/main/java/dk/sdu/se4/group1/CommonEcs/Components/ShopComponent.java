package dk.sdu.se4.group1.CommonEcs.Components;

import dk.sdu.se4.group1.CommonEcs.Component;

import java.util.ArrayList;
import java.util.List;

public class ShopComponent implements Component {
    private final List<ShopOfferComponent> shopItems = new ArrayList<>();

    public void addShopItem(Component component,int pris) {
        var item = new ShopOfferComponent(component,pris);
        shopItems.add(item);
    }

    public List<ShopOfferComponent> getShopItems() {
        return shopItems;
    }

}

