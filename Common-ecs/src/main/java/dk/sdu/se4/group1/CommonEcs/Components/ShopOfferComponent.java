package dk.sdu.se4.group1.CommonEcs.Components;

import dk.sdu.se4.group1.CommonApi.SeedType;
import dk.sdu.se4.group1.CommonEcs.Component;
import dk.sdu.se4.group1.CommonEcs.EntityID;

public class ShopOfferComponent implements Component {
    private final SeedType Type;
    private final int buyPrice;

    public ShopOfferComponent(SeedType shopId, int buyPrice) {
        this.Type = shopId;
        this.buyPrice = buyPrice;
    }

    public SeedType getShopId() {
        return Type;
    }

    public int getBuyPrice() {
        return buyPrice;
    }
}
