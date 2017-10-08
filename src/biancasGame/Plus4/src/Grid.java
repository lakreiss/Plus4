package biancasGame.Plus4.src;

import java.awt.Color;
import java.awt.Graphics;

public class Grid {

    private int x;
    private int y;
    private int dimension;

    public Grid(int x, int y, int dimension) {
        this.x = x;
        this.y = y;
        this.dimension = dimension;
    }

    public void draw(Graphics g) {
        g.setColor(Color.yellow);
        g.fill3DRect(x + (dimension / 16), y, dimension, dimension, true);
        g.fill3DRect(x, y, dimension / 16, 5 * dimension / 4, true);
        g.fill3DRect(x + dimension + (dimension / 16), y, dimension / 16, 5 * dimension / 4, true);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getDimension() {
        return dimension;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }
}
