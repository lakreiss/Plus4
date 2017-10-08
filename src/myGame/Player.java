package myGame;

import java.util.Scanner;

/**
 * Created by liamkreiss on 10/7/17.
 */
public class Player {
    private static int numPlayers = 0;
    private String fullName;
    private String gameName;
    private int playerNumber;
    protected static Player[] players = new Player[2];

    public Player() {
        this.fullName = "Computer";
        this.gameName = "O";
        this.playerNumber = numPlayers;
        players[1] = this;
        numPlayers += 1;
    }

    public Player(String name) {
        this.playerNumber = numPlayers;
        this.fullName = name;
        if (playerNumber == 0) {
            this.gameName = "X";
            players[0] = this;
        } else if (playerNumber == 1) {
            this.gameName = "O";
            players[1] = this;
        }
        numPlayers += 1;
    }

    public int getPlay(Board gameBoard, Scanner console) {
        return -1;
    }

    public String getGameName() {
        return this.gameName;
    }

    public String getFullName() {
        return this.fullName;
    }

    public int getPlayerNumber() {
        return this.playerNumber;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public boolean equals(Object o) {
        if (o instanceof Player) {
            return (((Player) o).playerNumber == this.getPlayerNumber());
        } else {
            return false;
        }
    }
}
