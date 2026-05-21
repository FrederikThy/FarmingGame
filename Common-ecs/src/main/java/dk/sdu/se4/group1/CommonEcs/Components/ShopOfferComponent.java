package dk.sdu.se4.group1.CommonEcs.Components;

import dk.sdu.se4.group1.CommonApi.SeedType;
import dk.sdu.se4.group1.CommonEcs.Component;
import dk.sdu.se4.group1.CommonEcs.EntityID;

public class ShopOfferComponent implements Component {
    private final Component Type;
    private final int buyPrice;

    public ShopOfferComponent(Component component, int buyPrice) {
        this.Type = component;
        this.buyPrice = buyPrice;
    }

    public Component getComponent() {
        return Type;
    }

    public int getBuyPrice() {
        return buyPrice;
    }
}
