package dk.sdu.se4.group1.CoreEngine;

import dk.sdu.se4.group1.CommonEcs.*;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


import java.util.ArrayList;
import java.util.List;

public class Game {

    // Systems loaded via ServiceLoader
    private final List<IEntityProcessingService> discoveredSystems;
    // Startup plugins. Initializing entity spawning etc.
    private final List<IGamePlugin> plugins;
    // UI plugins that inject javafx nodes into the scene
    private final List<IUiPluginService> uiPlugins;

    private final List<IMapService> renderSystems;
    private long lastTime = 0;

    Game(List<IEntityProcessingService> discoveredSystems, List<IGamePlugin> plugins, List<IUiPluginService> uiPlugins, List<IMapService> renderSystems) {
        this.discoveredSystems = discoveredSystems;
        this.plugins = plugins;
        this.uiPlugins = uiPlugins;
        this.renderSystems = renderSystems;
    }

    public void start(Stage window) {
        World world = new World();

        // Each module initializes its own entities.
        for (IGamePlugin plugin : plugins) {
            plugin.start(world);
        }

        // Build scene
        Pane root = new Pane();



        Image backgroundImage = new Image(Game.class.getResource("/Map.png").toExternalForm());
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitHeight(960);
        backgroundView.setFitWidth(960);
        backgroundView.setPreserveRatio(false);
        backgroundView.setSmooth(false);

        Canvas canvas = new Canvas(960, 960);
        root.getChildren().addAll(backgroundView, canvas);

        // A list of all systems loaded from the ServiceLoader
        List<IEntityProcessingService> allSystems = new ArrayList<>(discoveredSystems);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        for (IMapService renderSystem : renderSystems) {
            allSystems.add(renderSystem.create(gc));
        }

        // add nodes from UI plugins. if the UI also implements EcsSystem, add it to allSystems as well.
        for (IUiPluginService plugin : uiPlugins) {
            var node = plugin.createNode(world);

            root.getChildren().add(node);

            if (node instanceof IEntityProcessingService system) {
                allSystems.add(system);
            }
        }



        Scene scene = new Scene(root, 960, 960);
        window.setTitle("Farming Game");
        window.setScene(scene);
        window.show();

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastTime == 0)
                {
                    lastTime = now;
                    return;
                }
                double dt = (now - lastTime) / 1_000_000_000.0;
                lastTime = now;

                for (IEntityProcessingService system : allSystems) {
                    system.update(world, dt);
                }
            }
        }.start();
    }
}

