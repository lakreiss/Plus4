package biancasGame.Plus4.src;

import java.awt.Color;
import java.io.Serializable;

public class ClientModel implements Serializable {

    private Tokens tokens;
    private LeftOverPile pile;
    private int redID, blueID, currentPlayerID;
    private boolean inProgress;

    public ClientModel(int redID, int blueID) {
        this.redID = redID;
        this.blueID = blueID;
    }

    public void startGame() {
        tokens = new Tokens(0, 0, 400);
        pile = new LeftOverPile(0, 0, 400);
        if (Math.random() < 0.5) {
            currentPlayerID = redID;
        } else {
            currentPlayerID = blueID;
        }
        inProgress = true;
    }

    public boolean makeMove(int col) {
        Token token = tokens.getToken(0, col);
        if (token == null || !inProgress) {
            return false;
        }

        //add tokens to bottom pile as they come out
        if (!tokens.getToken(tokens.getSize() - 1, col).getColor().equals(Color.white)) {
            pile.addTokenToPile(col, tokens.getToken(tokens.getSize() - 1, col).getColor());
        }

        //move tokens in column down
        if (!tokens.getToken(0, col).getColor().equals(Color.white)) {
            for (int i = tokens.getSize() - 1; i > 0; i--) {
                tokens.getToken(i, col).setColor(tokens.getToken(i - 1, col).getColor());
            }
        }

        if (currentPlayerID == redID) {
            token.setColor(Color.red);
        } else {
            token.setColor(Color.blue);
        }

        if (isTie() || thereIsAWinner()) {
            inProgress = false;
        } else {
            if (currentPlayerID == redID) {
                currentPlayerID = blueID;
            } else {
                currentPlayerID = redID;
            }
        }
        return true;
    }

    public boolean isWinner(Color color) {
        return (checkFor4Horizontal(color) || checkFor4Vertical(color) || checkFor4Diagonal(color));
    }

    public boolean thereIsAWinner() {
        return ((isWinner(Color.red) || isWinner(Color.blue)) && !isTie());
    }

    public boolean isTie() {
        return (isWinner(Color.red) && isWinner(Color.blue));
    }

    public boolean inProgress() {
        return inProgress;
    }

    public int getCurrentPlayerID() {
        return currentPlayerID;
    }

    public Color getColor(int playerID) {
        if (playerID == redID) {
            return Color.red;
        } else {
            return Color.blue;
        }
    }

    public int getOtherPlayerID(int playerID) {
        if (playerID == redID) {
            return blueID;
        } else if (playerID == blueID) {
            return redID;
        } else {
            return -1;
        }
    }

    public Tokens getTokens() {
        return tokens;
    }

    public LeftOverPile getPile() {
        return pile;
    }

    private boolean checkFor4Horizontal(Color color) {
        for (int row = 0; row < tokens.getSize(); row++) {
            boolean fourHorizontal = true;
            for (int col = 0; col < tokens.getSize(); col++) {
                if (!tokens.getToken(row, col).getColor().equals(color)) {
                    fourHorizontal = false;
                }
            }
            if (fourHorizontal) {
                return true;
            }
        }
        return false;
    }

    private boolean checkFor4Vertical(Color color) {
        for (int col = 0; col < tokens.getSize(); col++) {
            boolean fourVertical = true;
            for (int row = 0; row < tokens.getSize(); row++) {
                if (!tokens.getToken(row, col).getColor().equals(color)) {
                    fourVertical = false;
                }
            }
            if (fourVertical) {
                return true;
            }
        }
        return false;
    }

    private boolean checkFor4Diagonal(Color color) {
        boolean fourDiagonal = true;
        //top left to bottom right
        for (int i = 0; i < tokens.getSize(); i++) {
            if (!tokens.getToken(i, i).getColor().equals(color)) {
                fourDiagonal = false;
            }
        }
        if (fourDiagonal) {
            return true;
        }

        fourDiagonal = true;
        //bottom left to top right
        for (int i = 0; i < tokens.getSize(); i++) {
            if (!tokens.getToken(tokens.getSize() - 1 - i, i).getColor().equals(color)) {
                fourDiagonal = false;
            }
        }
        if (fourDiagonal) {
            return true;
        }
        return false;
    }
}
