package dk.sdu.se4.group1.Map;

import dk.sdu.se4.group1.CommonEcs.*;
import dk.sdu.se4.group1.CommonEcs.Components.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;


public class MappingSystem implements EcsSystem, IRenderSystem{

    private final int mapHeight = MapSize.MAP_HEIGHT;
    private final int mapWidth = MapSize.MAP_WIDTH;

    //map variables
    private static final int tileSize = 64;
    private GraphicsContext gc;

    public MappingSystem() {
    }

    // Used to instantiate GraphipcsContext
    public MappingSystem(GraphicsContext gc) {
        this.gc = gc;
    }


    @Override
    public EcsSystem create(GraphicsContext gc) {
        this.gc = gc;
        return this;
    }
    //update method
    @Override
    public void update(World world, double deltaTime) {
        // Sikrer os, at vi clearer tiles hver gang, så vi ikke kommer til at have "ghost trails"
        gc.clearRect(0, 0, 960, 960);
        int [][] grid = new int[mapHeight][mapWidth];

        for(int y=0; y<grid.length; y++){
            for(int x=0; x<grid[y].length; x++){
                RenderTile(x, y, Color.TRANSPARENT);
            }
        }

        for (EntityID entity : world.getEntities()) {
            if (world.hasComponent(entity, RainOverlayComponent.class)) {
                continue;
            }

            if (world.hasComponent(entity, PositionComponent.class) &&
                    world.hasComponent(entity, RenderComponent.class)) {

                PositionComponent pos = (PositionComponent) world.GetComponent(entity, PositionComponent.class);
                RenderComponent renderComponent = (RenderComponent) world.GetComponent(entity, RenderComponent.class);

                RenderTile(pos.x, pos.y, renderComponent);
            }
        }

        for (EntityID entity : world.getEntities()) {
            if (!world.hasComponent(entity, RainOverlayComponent.class)) {
                continue;
            }

            if (world.hasComponent(entity, RenderComponent.class)) {
                RenderComponent renderComponent = (RenderComponent) world.GetComponent(entity, RenderComponent.class);

                if (renderComponent.sprite != null) {
                    gc.drawImage(renderComponent.sprite, 0, 0, 960, 960);
                }
            }
        }

        for (EntityID entity : world.getEntities()) {
            if (world.hasComponent(entity, PositionComponent.class) &&
                    world.hasComponent(entity, RobotComponent.class)) {

                PositionComponent pos = (PositionComponent) world.GetComponent(entity, PositionComponent.class);
                RenderRobotLabel(world, entity, pos);
            }
        }
    }


    //method for rendering each tile that needs to be rendered according to its color and component
    private void RenderTile (int x, int y, Color color){
        gc.setFill(color);

        gc.fillRect(
                x * tileSize,
                y * tileSize ,
                tileSize,
                tileSize);
    }

    private void RenderTile(int x, int y, RenderComponent renderComponent) {
        double drawX =x * tileSize;
        double drawY =y * tileSize;

        //If there is a sprite, then it will be rendered instead of a color
        if (renderComponent.sprite != null) {
            gc.drawImage(renderComponent.sprite, drawX, drawY, tileSize, tileSize);
            return;
        }

        //If there are no sprite, then there will be rendered a color instead
        RenderTile(x, y, renderComponent.color != null ? renderComponent.color : Color.GRAY);
    }

    private void RenderRobotLabel(World world, EntityID entity, PositionComponent pos) {
        RobotComponent robotComponent = (RobotComponent) world.GetComponent(entity, RobotComponent.class);

        String typeText = "[" + robotComponent.GetType() + "]";

        double tileX = pos.x * tileSize;
        double tileY = pos.y * tileSize;
        double centerX = tileX + tileSize / 2;

        Color typeColor = switch (robotComponent.robotType){
            case PLANT ->  Color.LIMEGREEN;
            case HARVEST -> Color.GOLD;
            case WEED_REMOVER ->   Color.SADDLEBROWN;
        };

        gc.setTextAlign(TextAlignment.CENTER);

        gc.setFont(Font.font("Arial", FontWeight.BOLD, 9));
        gc.setFill(Color.BLACK);
        gc.fillRoundRect(tileX + 8, tileY + 4, tileSize - 16, 14, 7, 7);

        gc.setFill(typeColor);
        gc.fillText(typeText, centerX, tileY + 14);

        gc.setTextAlign(TextAlignment.LEFT);
    }
}
