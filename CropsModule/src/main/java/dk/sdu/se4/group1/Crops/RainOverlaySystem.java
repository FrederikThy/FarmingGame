package dk.sdu.se4.group1.Crops;

import dk.sdu.se4.group1.CommonEcs.EcsSystem;
import dk.sdu.se4.group1.CommonEcs.EntityID;
import dk.sdu.se4.group1.CommonEcs.World;
import dk.sdu.se4.group1.CommonEcs.Components.PositionComponent;
import dk.sdu.se4.group1.CommonEcs.Components.RenderComponent;
import dk.sdu.se4.group1.CommonEcs.Components.RainOverlayComponent;
import javafx.scene.image.Image;

import java.io.InputStream;
import java.util.Random;

public class RainOverlaySystem implements EcsSystem {

    private static final double CHECK_INTERVAL_SECONDS = 20.0;
    private static final double RAIN_DURATION_SECONDS = 20.0;
    private static final double RAIN_CHANCE = 0.50;

    private static final double RAIN_GROWTH_BONUS_PER_SECOND = 0.5;

    private final Random random = new Random();
    private final Image rainOverlayImage;

    private double checkTimer = 0.0;
    private double rainTimer = 0.0;

    private boolean raining = false;
    private EntityID rainEntity = null;

    public RainOverlaySystem() {
        this.rainOverlayImage = loadImage("/RainOverlay.png");
    }

    @Override
    public void update(World world, double deltaTime) {
        if (raining) {
            rainTimer += deltaTime;

            makeCropsGrowFaster(world, deltaTime);

            if (rainTimer >= RAIN_DURATION_SECONDS) {
                stopRain(world);
            }

            return;
        }

        checkTimer += deltaTime;

        if (checkTimer >= CHECK_INTERVAL_SECONDS) {
            checkTimer -= CHECK_INTERVAL_SECONDS;

            if (random.nextDouble() < RAIN_CHANCE) {
                startRain(world);
            }
        }
    }

    private void startRain(World world) {
        if (rainEntity != null) {
            return;
        }

        raining = true;
        rainTimer = 0.0;

        rainEntity = world.createEntity();

        world.addComponent(rainEntity, new PositionComponent(0, 0));
        world.addComponent(rainEntity, new RenderComponent(rainOverlayImage));
        world.addComponent(rainEntity, new RainOverlayComponent());
    }

    private void stopRain(World world) {
        if (rainEntity != null) {
            world.RemoveEntity(rainEntity);
            rainEntity = null;
        }

        raining = false;
        rainTimer = 0.0;
        checkTimer = 0.0;
    }

    private void makeCropsGrowFaster(World world, double deltaTime) {
        for (EntityID crop : world.getEntitiesWith(GrowthComponent.class)) {
            GrowthComponent growth = (GrowthComponent) world.GetComponent(crop, GrowthComponent.class);

            growth.growthTime -= RAIN_GROWTH_BONUS_PER_SECOND * deltaTime;

            if (growth.growthTime < 0) {
                growth.growthTime = 0;
            }
        }
    }

    private Image loadImage(String path) {
        InputStream stream = RainOverlaySystem.class.getResourceAsStream(path);

        if (stream == null) {
            throw new IllegalArgumentException("Image not found: " + path);
        }

        return new Image(stream);
    }

    @Override
    public int priority() {
        return 0;
    }
}