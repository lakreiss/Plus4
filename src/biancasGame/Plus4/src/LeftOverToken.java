package biancasGame.Plus4.src;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;


public class LeftOverToken implements Serializable {

    private int x, y, width, height;
    private Color color;

    public LeftOverToken(int x, int y, int width, int height, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRoundRect(x, y, width, height-1, 10, 10);
    }

    public Color getColor() {
        return color;
    }
    
    public void setColor(Color color) {
        this.color = color;
    }
}
