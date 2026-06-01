package dk.sdu.se4.group1.CommonEcs;

import dk.sdu.se4.group1.CommonEcs.Components.InventoryIComponentService;
import dk.sdu.se4.group1.CommonEcs.Components.ShopIComponentService;
import dk.sdu.se4.group1.CommonEcs.Components.ShopOfferIComponentService;

public interface ToolSPI {

    Class<? extends IComponentService> getToolType();
    void addShopOffer(ShopIComponentService shop);

    boolean applyTool(World world, InventoryIComponentService inventory, ShopOfferIComponentService offer, EntityID robotEntity);


}