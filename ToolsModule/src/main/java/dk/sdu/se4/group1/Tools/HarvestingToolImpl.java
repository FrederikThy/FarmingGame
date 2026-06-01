package dk.sdu.se4.group1.Tools;

import dk.sdu.se4.group1.CommonEcs.EntityID;
import dk.sdu.se4.group1.CommonEcs.IComponentService;
import dk.sdu.se4.group1.CommonEcs.ToolSPI;
import dk.sdu.se4.group1.CommonEcs.World;
import dk.sdu.se4.group1.CommonEcs.Components.HarvestingIComponentService;
import dk.sdu.se4.group1.CommonEcs.Components.InventoryIComponentService;
import dk.sdu.se4.group1.CommonEcs.Components.ShopOfferIComponentService;

public class HarvestingToolImpl implements ToolSPI {

    @Override
    public Class<? extends IComponentService> getToolType() {
        return HarvestingIComponentService.class;
    }

    @Override
    public boolean applyTool(World world, InventoryIComponentService inventory,
                             ShopOfferIComponentService offer, EntityID robotEntity) {
        int price = offer.getBuyPrice();
        if (inventory.getWallet() < price) return false;
        if (!(offer.getComponent() instanceof HarvestingIComponentService)) return false;

        HarvestingIComponentService tool = world.hasComponent(robotEntity, HarvestingIComponentService.class)
                ? (HarvestingIComponentService) world.GetComponent(robotEntity, HarvestingIComponentService.class)
                : new HarvestingIComponentService();
        tool.setGrowthMultiplier(tool.getGrowthMultiplier() + 1.0);
        world.addComponent(robotEntity, tool);
        inventory.removeFromWallet(price);
        return true;
    }
}