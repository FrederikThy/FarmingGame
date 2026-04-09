package dk.sdu.se4.group1.Map;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class WorldMap {

    private int rows;
    private int cols;
    private int tileSize;
    private Rectangle[][] Map; //multidimensional array for internal logic


    public WorldMap(int rows, int cols, int tileSize) {
        this.rows = rows;
        this.cols = cols;
        this.tileSize = tileSize;
        this.Map = new Rectangle[rows][cols];
    }
    //Function that initializes the grid and adds tiles to it
    public GridPane createGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(5);
        grid.setVgap(5);
        grid.setLayoutX(5);

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {

                Rectangle tile = new Rectangle(tileSize, tileSize);
                tile.setFill(Color.GREENYELLOW); //Tile color
                tile.setStroke(Color.BLACK); //Tile outline colour
                Map[y][x] = tile;
                grid.add(tile, x, y);
            }
        }
        return grid; //Grid object that is displayed
    }
    //Function for retrieving tile object
    public Rectangle getTile(int x, int y) {
        return Map[y][x];
    }
    //Function for changing tile color
    public void makeBlack(int x, int y) {
        getTile(x,y).setFill(Color.BLACK);
    }

    public List<Rectangle> getColumn(int x) {

        if (x < 0 || x >= cols) {
        throw new IllegalArgumentException("Column index out of bounds: " + x);
    }

    List<Rectangle> columnTiles = new ArrayList<>();
    
    for (int y = 0; y < rows; y++) {
        columnTiles.add(Map[y][x]); 
    }
    
    return columnTiles;
}

}