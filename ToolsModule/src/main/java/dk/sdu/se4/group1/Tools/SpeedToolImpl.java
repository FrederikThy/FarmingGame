package dk.sdu.se4.group1.Tools;

import dk.sdu.se4.group1.CommonEcs.EntityID;
import dk.sdu.se4.group1.CommonEcs.IComponentService;
import dk.sdu.se4.group1.CommonEcs.ToolSPI;
import dk.sdu.se4.group1.CommonEcs.World;
import dk.sdu.se4.group1.CommonEcs.Components.InventoryIComponentService;
import dk.sdu.se4.group1.CommonEcs.Components.ShopOfferIComponentService;
import dk.sdu.se4.group1.CommonEcs.Components.SpeedToolIComponentService;

public class SpeedToolImpl implements ToolSPI {

    @Override
    public Class<? extends IComponentService> getToolType() {
        return SpeedToolIComponentService.class;
    }

    @Override
    public boolean applyTool(World world, InventoryIComponentService inventory,
                             ShopOfferIComponentService offer, EntityID robotEntity) {
        int price = offer.getBuyPrice();
        if (inventory.getWallet() < price) return false;
        if (!(offer.getComponent() instanceof SpeedToolIComponentService)) return false;

        if (world.hasComponent(robotEntity, SpeedToolIComponentService.class)) {
            SpeedToolIComponentService existing =
                    (SpeedToolIComponentService) world.GetComponent(robotEntity, SpeedToolIComponentService.class);
            world.addComponent(robotEntity, new SpeedToolIComponentService(existing.getSpeedMultiplier() + 1.0));
        } else {
            world.addComponent(robotEntity, new SpeedToolIComponentService(2.0));
        }

        inventory.removeFromWallet(price);
        return true;
    }
}