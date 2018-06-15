package myGame.gameClients;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
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

    private static final int GAMES_TO_PLAY = 10;
    private static final String DATA_FILE = "sorted_data1511502410478";
    private static boolean addDirectlyToData = true;

    public static void main(String[] args) throws FileNotFoundException {
        Player[] players = new Player[]{
                new IntelligentComputer("Computer1"),
                new IntelligentComputer("Computer2"),
                new Nobody()
        };

        Player.setPlayers(players);

        Board gameBoard = new Board();
        PrintStream output;

        if (addDirectlyToData) {
            output = new PrintStream(new FileOutputStream(DATA_FILE, true));
        } else {
            output = new PrintStream(new File("data" + System.currentTimeMillis()));
        }

        playGame(output, players, gameBoard, GAMES_TO_PLAY, 0);
    }

    private static void playGame(PrintStream output, Player[] players, Board gameBoard, int gamesToPlay, int gamesPlayed) throws FileNotFoundException {
        boolean gameOver = false;
        Player winningPlayer;
        String winner = "";
        boolean p1Turn = true;
        Player curPlayer;
        String compressedGame = "";

        while (!gameOver) {

//            System.out.println(gameBoard.toString());

            if (p1Turn) {
                curPlayer = players[0];
            } else {
                curPlayer = players[1];
            }

            int col = gameBoard.addPiece(curPlayer, curPlayer.getPlay(gameBoard));

            compressedGame += (col + " ");

            winningPlayer = gameBoard.getWinningPlayer(players);
            winner = winningPlayer.getFullName();
            p1Turn = !p1Turn;
            if (!winner.equals("")) {
                gameOver = true;
            } else if (compressedGame.length() > 19) {
                if (compressedGame.substring(compressedGame.length() - 19).equals("0 0 0 0 0 0 0 0 0 0")
                        || compressedGame.substring(compressedGame.length() - 19).equals("1 1 1 1 1 1 1 1 1 1")
                        || compressedGame.substring(compressedGame.length() - 19).equals("2 2 2 2 2 2 2 2 2 2")
                        || compressedGame.substring(compressedGame.length() - 19).equals("3 3 3 3 3 3 3 3 3 3")) {
                    gameOver = true;
                    winner = "tie";
                }
            }
        }

        if (winner.equals("tie")) {
            compressedGame += ("It's a tie!");
        } else {
            compressedGame += (winner + " wins!");
        }

        output.println(compressedGame);

        gamesPlayed++;
        if (gamesPlayed < gamesToPlay) {
            playGame(output, players, new Board(), gamesToPlay, gamesPlayed);
        }

    }
}
