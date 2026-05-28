package dk.sdu.se4.group1.Shop;

import dk.sdu.se4.group1.CommonEcs.Components.GrowthMapIComponentService;
import dk.sdu.se4.group1.CommonEcs.Components.InventoryIComponentService;
import dk.sdu.se4.group1.CommonEcs.Components.ShopIComponentService;
import dk.sdu.se4.group1.CommonEcs.Components.ShopOfferIComponentService;
import dk.sdu.se4.group1.CommonEcs.EntityID;
import dk.sdu.se4.group1.CommonEcs.World;

public class ShopController {
    private static final int SOIL_UPGRADE_PRICE =300;
    private static final double UPDATE_INTERVAL = 1.1;

    private final ShopView view;
    private final ShopPurchaseService purchaseService;

    private ShopIComponentService activeShop;
    private InventoryIComponentService activeInventory;
    private GrowthMapIComponentService activeGrowthMap;
    private double shopUpdateTimer = 0.0;

    public ShopController(){
        this.purchaseService = new ShopPurchaseService();
        this.view = new ShopView(this, new ShopItemMapper(),new ShopPricingService());
    }

    public void openShop(World world){
        activeInventory = findIventory(world);
        activeShop = findShop(world);
        activeGrowthMap = findGrowthMapComponent(world);

        if (activeInventory == null || activeShop ==null){
            return;
        }
        view.open(world,activeShop,activeInventory,activeGrowthMap,SOIL_UPGRADE_PRICE);
    }

    public void buyOffer(World world, ShopOfferIComponentService offer)
    {
        if (activeInventory == null)
        {
            return;
        }
        boolean bought = purchaseService.purchase(world,activeInventory,offer);

        if(bought){
            view.refresh(world,activeShop,activeInventory,activeGrowthMap,SOIL_UPGRADE_PRICE);
        }
    }

    public void buySpeedToolForRobot(World world, ShopOfferIComponentService offer, EntityID robotEntity) {
        if (activeInventory == null) {
            return;
        }

        boolean bought = purchaseService.buySpeedToolForRobot(
                world,
                activeInventory,
                offer,
                robotEntity
        );

        if (bought) {
            view.refresh(world, activeShop, activeInventory, activeGrowthMap, SOIL_UPGRADE_PRICE);
        }
    }


    public void buySoilUpdgrade(World world)
    {
        if (activeInventory == null)
        {
            return;
        }
        boolean bought = purchaseService.buySoilUpgrade(world,activeInventory,SOIL_UPGRADE_PRICE);

        if(bought){
            activeGrowthMap = findGrowthMapComponent(world);
            view.refresh(world,activeShop,activeInventory,activeGrowthMap,SOIL_UPGRADE_PRICE);
        }


    }

    public void closeShop()
    {
        activeShop = null;
        activeInventory = null;
        activeGrowthMap = null;
        shopUpdateTimer = 0.0;
    }

    public void update(World world, double deltaTime) {
        if (activeShop == null || activeInventory == null) {
            return;
        }

        shopUpdateTimer += deltaTime;

        if (shopUpdateTimer < UPDATE_INTERVAL) {
            return;
        }

        shopUpdateTimer = 0.0;
        view.refresh(world, activeShop, activeInventory, activeGrowthMap, SOIL_UPGRADE_PRICE);
    }

    private ShopIComponentService findShop(World world) {
        var entities = world.getEntitiesWith(ShopIComponentService.class);

        if (entities == null || !entities.iterator().hasNext()) {
            return null;
        }

        EntityID entity = entities.iterator().next();
        return (ShopIComponentService) world.GetComponent(entity, ShopIComponentService.class);
    }

    private InventoryIComponentService findIventory(World world) {
        var entities = world.getEntitiesWith(InventoryIComponentService.class);

        if (entities == null || !entities.iterator().hasNext()) {
            return null;
        }

        EntityID entity = entities.iterator().next();
        return (InventoryIComponentService) world.GetComponent(entity, InventoryIComponentService.class);
    }

    private GrowthMapIComponentService findGrowthMapComponent(World world) {
        var entities = world.getEntitiesWith(GrowthMapIComponentService.class);

        if (entities == null || !entities.iterator().hasNext()) {
            return null;
        }

        EntityID entity = entities.iterator().next();
        return (GrowthMapIComponentService) world.GetComponent(entity, GrowthMapIComponentService.class);
    }

}
