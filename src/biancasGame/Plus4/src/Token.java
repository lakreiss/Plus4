package biancasGame.Plus4.src;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;

public class Token implements Serializable {

    private int x;
    private int y;
    private int dimension;
    private Color color;

    public Token(int x, int y, int dimension, Color color) {
        this.x = x;
        this.y = y;
        this.dimension = dimension;
        this.color = color;
    }

    public void draw(Graphics g) {
        g.setColor(Color.GRAY);
        g.fillOval(x + 5, y + 5, dimension, dimension);
        g.setColor(color);
        g.fillOval(x, y, dimension, dimension);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean containsPoint(int x, int y) {
        return ((x - this.x - dimension / 2) * (x - this.x - dimension / 2)
                + (y - this.y - dimension / 2) * (y - this.y - dimension / 2) < dimension / 2 * dimension / 2);
    }
}
