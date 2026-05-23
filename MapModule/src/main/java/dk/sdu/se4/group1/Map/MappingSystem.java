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

        for(EntityID entity : world.getEntities()){
            if(world.hasComponent(entity, PositionComponent.class) && (world.hasComponent(entity, RenderComponent.class))){
                PositionComponent pos = (PositionComponent) world.GetComponent(entity, PositionComponent.class);
                RenderComponent renderComponent = (RenderComponent) world.GetComponent(entity, RenderComponent.class);
                RenderTile(pos.x, pos.y, renderComponent);
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
        if (!world.hasComponent(entity, RobotComponent.class)) {
            return;
        }

        RobotComponent robotComponent = (RobotComponent) world.GetComponent(entity, RobotComponent.class);

        String statusText = "Idle";
        if (world.hasComponent(entity, RobotStatusComponent.class)) {
            RobotStatusComponent robotStatusComponent = (RobotStatusComponent) world.GetComponent(entity, RobotStatusComponent.class);
            statusText = robotStatusComponent.status;
        }

        String typeText = "[" + robotComponent.GetType() + "]";

        double centerX = pos.x * tileSize + tileSize / 2;
        double topY = pos.y * tileSize - 8;

        gc.setTextAlign(TextAlignment.CENTER);

        gc.setFont(Font.font("Arial", FontWeight.BOLD, 10));
        gc.setFill(Color.rgb(255, 255, 255, 0.85));
        gc.fillRoundRect(centerX - 34, topY - 12, 68, 26, 6, 6);

        gc.setStroke(Color.rgb(40, 35, 25));
        gc.strokeRoundRect(centerX - 34, topY - 12, 68, 26, 6, 6);

        gc.setFill(Color.rgb(35, 25, 15));
        gc.fillText(typeText, centerX, topY);

        gc.setFont(Font.font("Arial", FontWeight.NORMAL, 9));
        gc.fillText(statusText, centerX, topY + 11);

        gc.setTextAlign(TextAlignment.LEFT);

    }
}
