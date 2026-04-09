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
                RenderComponent EntityColor = (RenderComponent) world.GetComponent(entity, RenderComponent.class);
                RenderTile(pos.x, pos.y, EntityColor.color);
            }
        }
    }


    //method for rendering each tile that needs to be rendered according to its color and component
    private void RenderTile (int x, int y, Color color){
        int tileSize = 45;

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

}
