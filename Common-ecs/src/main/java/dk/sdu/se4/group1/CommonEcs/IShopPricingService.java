package dk.sdu.se4.group1.CommonEcs;

import dk.sdu.se4.group1.CommonApi.SeedType;

public interface IShopPricingService {
    int getSellPrice(SeedType seedType, int amount);
}
