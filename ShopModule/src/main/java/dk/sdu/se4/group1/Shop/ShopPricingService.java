package dk.sdu.se4.group1.Shop;

import dk.sdu.se4.group1.CommonApi.SeedType;
import dk.sdu.se4.group1.CommonEcs.Components.ShopOfferIComponentService;

public class ShopPricingService {
    public boolean canAfford(int price,int wallet){
        return price <= wallet;
    }

    public int getBuyPrice(ShopOfferIComponentService offer){
        return offer.getBuyPrice();
    }

    public int getSellPrice(SeedType seedType, int amount){
        int sellsAmount = 0;
        switch (seedType)
        {
            case CHILI -> sellsAmount = 100 *amount;
            case CARROT -> sellsAmount = 150 *amount;
            case BEANSPROUT -> sellsAmount = 200 *amount;
            case TOMATO -> sellsAmount = 200 * amount;
        }
        return sellsAmount;
    }

}
