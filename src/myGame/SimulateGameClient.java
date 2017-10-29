package myGame;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import myGame.gameplay.Board;
import myGame.player.HardComputer;
import myGame.player.IntelligentComputer;
import myGame.player.Nobody;
import myGame.player.Player;

import java.util.Scanner;

/**
 * Created by liamkreiss on 10/28/17.
 */
public class SimulateGameClient {

    private static final int GAMES_TO_PLAY = 2000;

    public static void main(String[] args) throws FileNotFoundException {
        Player[] players = new Player[]{
                new HardComputer("Computer1"),
                new HardComputer("Computer2"),
                new Nobody()
        };
        Board gameBoard = new Board();

        File outputFile = new File("data" + System.currentTimeMillis());
        PrintStream output = new PrintStream(outputFile);

        playGame(output, players, gameBoard, GAMES_TO_PLAY, 0);
    }

    private static void playGame(PrintStream output, Player[] players, Board gameBoard, int gamesToPlay, int gamesPlayed) throws FileNotFoundException {
        boolean gameOver = false;
        Player winningPlayer = null;
        String winner = "";
        boolean p1Turn = true;
        Player curPlayer;

        while (!gameOver) {

//            System.out.println(gameBoard.toString());

            if (p1Turn) {
                curPlayer = players[0];
            } else {
                curPlayer = players[1];
            }

            int col = gameBoard.addPiece(curPlayer, curPlayer.getPlay(gameBoard));

            output.print(col + " ");

            winningPlayer = gameBoard.getWinningPlayer(players);
            winner = winningPlayer.getFullName();
            p1Turn = !p1Turn;
            if (!winner.equals("")) {
                gameOver = true;
            }
        }

        if (winner.equals("tie")) {
            output.println("It's a tie!");
        } else {
            output.println(winner + " wins!");
        }

        gamesPlayed++;
        if (gamesPlayed < gamesToPlay) {
            playGame(output, players, new Board(), gamesToPlay, gamesPlayed);
        }

    }
}
