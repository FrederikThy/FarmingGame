package dk.sdu.se4.group1.CommonEcs.Components;

import dk.sdu.se4.group1.CommonEcs.Component;
import dk.sdu.se4.group1.CommonEcs.EntityID;

public class ShopOfferComponent implements Component {
    private final EntityID shopId;
    private final int buyPrice;

    public ShopOfferComponent(EntityID shopId, int buyPrice) {
        this.shopId = shopId;
        this.buyPrice = buyPrice;
    }

    public EntityID getShopId() {
        return shopId;
    }

    public int getBuyPrice() {
        return buyPrice;
    }
}
