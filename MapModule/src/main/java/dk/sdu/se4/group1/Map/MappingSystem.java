package dk.sdu.se4.group1.Map;

import dk.sdu.se4.group1.CommonEcs.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public class MappingSystem implements EcsSystem{

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
        int [][] grid = new int[10][10];

        for(int y=0; y<grid.length; y++){
            for(int x=0; x<grid[y].length; x++){
                RenderTile(x, y, Color.GREEN);
            }
        }

        for(EntityID entity : world.getEntities()){
            if(world.hasComponent(entity, PositionComponent.class)){
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

        //If there is a sprite, then it will be rendered instead of a color
        if (renderComponent.sprite != null) {
            gc.drawImage(renderComponent.sprite, drawX, drawY, tileSize, tileSize);
            gc.setStroke(Color.BLACK);
            gc.strokeRect(drawX, drawY, tileSize, tileSize);
            return;
        }

        //If there are no sprite, then there will be rendered a color instead
        RenderTile(x, y, renderComponent.color != null ? renderComponent.color : Color.GRAY);
    }
}
