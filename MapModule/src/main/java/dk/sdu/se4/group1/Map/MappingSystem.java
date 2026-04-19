package dk.sdu.se4.group1.Map;

import dk.sdu.se4.group1.CommonEcs.EcsSystem;
import dk.sdu.se4.group1.CommonEcs.EntityID;
import dk.sdu.se4.group1.CommonEcs.GoalComponent;
import dk.sdu.se4.group1.CommonEcs.MapSize;
import dk.sdu.se4.group1.CommonEcs.PositionComponent;
import dk.sdu.se4.group1.CommonEcs.RenderComponent;
import dk.sdu.se4.group1.CommonEcs.World;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public class MappingSystem implements EcsSystem{

    private final int mapHeight = MapSize.MAP_HEIGHT;
    private final int mapWidth = MapSize.MAP_WIDTH;

    //map variables
    private static final int tileSize = 45;
    private static final int gap = 5;
    private final GraphicsContext gc;

    //constructer injecting the Grapical context
    public MappingSystem(GraphicsContext gc){
        this.gc = gc;
    }

    //update method
    @Override
    public void update(World world, double deltaTime) {
        gc.clearRect(0, 0, 800, 600);

        // Draw base green grid
        for (int y = 0; y < mapHeight; y++) {
            for (int x = 0; x < mapWidth; x++) {
                RenderTile(x, y, Color.GREEN);
            }
        }

        // Draw goal tile in gold with "B" label (drawn before robot so robot renders on top when it arrives)
        for (EntityID entity : world.getEntitiesWith(GoalComponent.class)) {
            PositionComponent pos = (PositionComponent) world.GetComponent(entity, PositionComponent.class);
            RenderTile(pos.x, pos.y, Color.GOLD);
            drawGoalMarker(pos.x, pos.y);
        }

        // Draw all renderable entities except goal markers (already drawn above)
        for (EntityID entity : world.getEntities()) {
            if (world.hasComponent(entity, GoalComponent.class)) continue;
            if (world.hasComponent(entity, PositionComponent.class) && world.hasComponent(entity, RenderComponent.class)) {
                PositionComponent pos = (PositionComponent) world.GetComponent(entity, PositionComponent.class);
                RenderComponent renderComponent = (RenderComponent) world.GetComponent(entity, RenderComponent.class);
                RenderTile(pos.x, pos.y, renderComponent);
            }
        }
    }

    // Draws a bold "B" label on the goal tile so it is clearly the destination. 
    private void drawGoalMarker(int x, int y) {
        double drawX = 40 + x * (tileSize + gap);
        double drawY = 40 + y * (tileSize + gap);
        gc.setFill(Color.BLACK);
        gc.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 18));
        gc.fillText("B", drawX + tileSize / 2.0 - 6, drawY + tileSize / 2.0 + 7);
    }


    //method for rendering each tile that needs to be rendered according to its color and component
    private void RenderTile (int x, int y, Color color){
        gc.setFill(color);

        gc.fillRect(
                40 + x * (tileSize + gap),
                40 + y * (tileSize + gap),
                tileSize,
                tileSize);


        gc.setStroke(Color.BLACK);

        gc.strokeRect(
                40 + x * (tileSize + gap),
                40 + y * (tileSize + gap),
                tileSize,
                tileSize);
    }

    private void RenderTile(int x, int y, RenderComponent renderComponent) {
        double drawX = 40 + x * (tileSize + gap);
        double drawY = 40 + y * (tileSize + gap);

  
        //If there are no sprite, then there will be rendered a color instead
        RenderTile(x, y, Color.valueOf(renderComponent.color != null ? renderComponent.color : "GRAY"));    }
}