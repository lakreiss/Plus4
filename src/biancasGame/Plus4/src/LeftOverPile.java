package biancasGame.Plus4.src;

import biancasGame.Plus4.src.LeftOverToken;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;


public class LeftOverPile implements Serializable {

    private LeftOverToken[][] tokens;
    
    public LeftOverPile(int x, int y, int dimension) {
        this.tokens = new LeftOverToken[2][4];
        for (int i = 0; i < tokens.length; i++) {
            for (int j = 0; j < tokens[0].length; j++) {
                tokens[i][j] = new LeftOverToken(x + (dimension / 16) + (dimension / 32) + (j * dimension / 4),
                        y + dimension + (dimension / 8) + (i*dimension/16),
                        3 * dimension / 16, dimension / 16, Color.white);
            }
        }
    }

    public void draw(Graphics g) {
        for (int i = 0; i < tokens.length; i++) {
            for (int j = 0; j < tokens[0].length; j++) {
                tokens[i][j].draw(g);
            }
        }
    }

    public void addTokenToPile(int col, Color color) {
        for (int i = tokens.length - 1; i >= 0; i--) {
            if (tokens[i][col].getColor().equals(Color.white)) {
                tokens[i][col].setColor(color);
                return;
            }
        }
        tokens[1][col].setColor(tokens[0][col].getColor());
        tokens[0][col].setColor(color);
    }
    
}
