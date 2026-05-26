package dk.sdu.se4.group1.CommonEcs.Components;

import dk.sdu.se4.group1.CommonEcs.IComponentService;

public class ShopOfferIComponentService implements IComponentService {
    private final IComponentService Type;
    private final int buyPrice;

    public ShopOfferIComponentService(IComponentService IComponentService, int buyPrice) {
        this.Type = IComponentService;
        this.buyPrice = buyPrice;
    }

    public IComponentService getComponent() {
        return Type;
    }

    public int getBuyPrice() {
        return buyPrice;
    }
}
