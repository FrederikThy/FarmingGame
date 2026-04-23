package dk.sdu.se4.group1.Map;

import dk.sdu.se4.group1.CommonEcs.*;
import dk.sdu.se4.group1.CommonEcs.Components.PositionComponent;
import dk.sdu.se4.group1.CommonEcs.Components.RenderComponent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;




public class MappingSystem implements EcsSystem{

    private final int mapHeight = MapSize.MAP_HEIGHT;
    private final int mapWidth = MapSize.MAP_WIDTH;

    //map variables
    private static final int tileSize = 64;
    private final GraphicsContext gc;

    //constructer injecting the Grapical context
    public MappingSystem(GraphicsContext gc){
        this.gc = gc;
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
}
