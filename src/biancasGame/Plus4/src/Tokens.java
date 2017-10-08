package biancasGame.Plus4.src;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;

public class Tokens implements Serializable {

    private Token[][] tokens = new Token[4][4];

    public Tokens(int x, int y, int dimension) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                tokens[i][j] = new Token(x + (dimension / 16) + (dimension / 32) + (j * dimension / 4),
                        y + (dimension / 32) + (i * dimension / 4),
                        3 * dimension / 16, Color.white);
            }
        }
    }

    public Token getToken(int row, int col) {
        return tokens[row][col];
    }

    public Token containsPoint(int x, int y) {
        for (int i = 0; i < tokens.length; i++) {
            for (int j = 0; j < tokens[0].length; j++) {
                if (tokens[i][j].containsPoint(x, y)) {
                    return tokens[i][j];
                }
            }
        }
        return null;
    }

    public int getSize() {
        return tokens.length;
    }

    public void setToken(int row, int col, Token token) {
        tokens[row][col] = token;
    }

    public void draw(Graphics g) {
        for (int i = 0; i < tokens.length; i++) {
            for (int j = 0; j < tokens[0].length; j++) {
                tokens[i][j].draw(g);
            }
        }
    }
}
