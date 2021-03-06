package myGame.player;

import com.sun.corba.se.impl.orbutil.graph.Graph;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import myGame.gameplay.Board;

import java.util.Scanner;

/**
 * Created by liamkreiss on 10/7/17.
 */
public class Player {
    private static int numPlayers = 0;
    private String fullName;
    protected int playerNumber;
    private int score;
    protected static Player[] players;

    public Player() {
        this.fullName = "Computer";
        this.playerNumber = numPlayers;
        numPlayers += 1;
        this.score = 0;
    }

    public Player(boolean placeholder) {
        this.fullName = "";
        this.score = -1;
    }

    public Player(String name, int playerNumber) {
        this.fullName = name;
        this.playerNumber = playerNumber;
        numPlayers += 1;
        this.score = 0;
    }

    public Player(String name) {
        this.playerNumber = numPlayers;
        this.fullName = name;
        numPlayers += 1;
        this.score = 0;
    }

    public String getGameName() {
        if (this.playerNumber == 0) {
            return "X";
        } else if (this.playerNumber == 1) {
            return "O";
        } else if (this.playerNumber == 2) {
            return "";
        } else {
            throw new Error("invalid player number");
        }
    }

    public int getPlay(Stage theStage, Scene theScene, GraphicsContext gc, Board gameBoard) {
        return -1;
    }

    public int getPlay(Board gameBoard, Scanner console) {
        return -1;
    }

    public int getPlay(Board gameBoard) {
        return -1;
    }

    public String getFullName() {
        return this.fullName;
    }

    public int getPlayerNumber() {
        return this.playerNumber;
    }

    public Player[] getPlayers() {
        return players;
    }

    public static void setPlayers(Player[] allPlayers) {
        players = allPlayers;
    }

    public Player getOpponent() {
        return players[(this.playerNumber + 1) % 2];
    }

    public boolean equals(Object o) {
        if (o instanceof Player) {
            return (((Player) o).playerNumber == this.getPlayerNumber());
        } else {
            return false;
        }
    }

    //TODO if I ever include lowercase letters, also include 2 point games
    public void wonGame() {
        this.score += 1;
    }

    public int getScore() {
        return this.score;
    }

    public void resetScore() {
        this.score = 0;
    }

    public String toString() {
        return this.getFullName();
    }
}
