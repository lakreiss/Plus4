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
    private String gameName;
    protected int playerNumber;
    private int score;
    protected static Player[] players;

    public Player() {
        this.fullName = "Computer";
        this.playerNumber = numPlayers;
        if (playerNumber == 0) {
            this.gameName = "X";
        } else if (playerNumber == 1) {
            this.gameName = "O";
        }
        numPlayers += 1;
        this.score = 0;
    }

    public Player(boolean placeholder) {
        this.fullName = "";
        this.gameName = "";
        this.score = -1;
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

    public int getPlay(Stage theStage, Scene theScene, GraphicsContext gc, Board gameBoard) {
        return -1;
    }

    public int getPlay(Board gameBoard, Scanner console) {
        return -1;
    }

    public int getPlay(Board gameBoard) {
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
