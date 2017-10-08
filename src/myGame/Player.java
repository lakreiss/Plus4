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

    public Player() {
        this.fullName = "Computer";
        this.gameName = "O";
        this.playerNumber = numPlayers;
        numPlayers += 1;
    }

    public Player(String name) {
        this.playerNumber = numPlayers;
        this.fullName = name;
        if (playerNumber == 0) {
            this.gameName = "X";
        } else if (playerNumber == 1) {
            this.gameName = "O";
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
