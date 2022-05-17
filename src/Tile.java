import java.awt.*;
import java.awt.image.BufferedImage;

public class Tile extends Rectangle {
    public BufferedImage icon;

    int locX;
    int locY;

    boolean wall;

    public Tile (int x,int y, int width, int height) {
        wall = false;
        this.locX = x;
        this.locY = y;
        this.x = x;
        this.y = y;
        this.width = 50;
        this.height = 50;
  
    }
    public Tile (int x, int y, String path, boolean wall) {
        this(x,y,50,50);
        this.wall = wall;
    }

}
