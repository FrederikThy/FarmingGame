package dk.sdu.se4.group1.Tools;

import dk.sdu.se4.group1.CommonEcs.Components.*;
import dk.sdu.se4.group1.CommonEcs.EntityID;
import dk.sdu.se4.group1.CommonEcs.IComponentService;
import dk.sdu.se4.group1.CommonEcs.ToolSPI;
import dk.sdu.se4.group1.CommonEcs.World;

public class PlantingToolImpl implements ToolSPI {

    @Override
    public Class<? extends IComponentService> getToolType() {
        return PlantingIComponentService.class;
    }

    @Override
    public boolean applyTool(World world, InventoryIComponentService inventory, ShopOfferIComponentService offer, EntityID robotEntity) {
        int price = offer.getBuyPrice();
        if (inventory.getWallet() < price) return false;
        if (!(offer.getComponent() instanceof PlantingIComponentService)) return false;

        PlantingIComponentService tool = world.hasComponent(robotEntity, PlantingIComponentService.class)
                ? (PlantingIComponentService) world.GetComponent(robotEntity, PlantingIComponentService.class)
                : new PlantingIComponentService();
        tool.setPlantingSpeedMultiplier(tool.getPlantingSpeedMultiplier() + 1.0);
        world.addComponent(robotEntity, tool);
        inventory.removeFromWallet(price);
        return true;
    }

    @Override
    public void addShopOffer(ShopIComponentService shop) {
        shop.addShopItem(new PlantingIComponentService(), 150);
    }
}