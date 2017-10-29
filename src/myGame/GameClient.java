package myGame;

import java.util.Scanner;

/**
 * Created by liamkreiss on 10/7/17.
 */
public class GameClient {

    //TODO ask if user wants this
    private static final boolean GAME_TO_10 = true;

    public static void main(String[] args) {
        Scanner console = new Scanner(System.in);
        System.out.println(intro());
        int numPlayers = getNumPlayers(console);
        Player[] players = getPlayers(numPlayers, console);
        Board gameBoard = new Board();

        playGame(players, gameBoard, console);

    }

    private static void playGame(Player[] players, Board gameBoard, Scanner console) {
        boolean reached10 = false;
        boolean gameOver = false;
        Player winningPlayer = null;
        String winner = "";
        System.out.println("Begin! \n");
        boolean p1Turn = true;
        Player curPlayer;

        while (!gameOver) {

            System.out.println(gameBoard.toString());

            if (p1Turn) {
                curPlayer = players[0];
            } else {
                curPlayer = players[1];
            }

            gameBoard.addPiece(curPlayer, curPlayer.getPlay(gameBoard, console));

            winningPlayer = gameBoard.getWinningPlayer(players);
            winner = winningPlayer.getFullName();
            p1Turn = !p1Turn;
            if (!winner.equals("")) {
                gameOver = true;
            }
        }

        System.out.println("");
        if (winner.equals("tie")) {
            System.out.println("It's a tie!");
        } else {
            System.out.println(winner + " wins!");
            winningPlayer.wonGame();
        }

        System.out.println(gameBoard.toString());

        checkGameOver(players, reached10, console);

    }

    private static void checkGameOver(Player[] players, boolean reached10, Scanner console) {
        if (GAME_TO_10) {

            if (players[0].getScore() > players[1].getScore()) {
                if (players[0].getScore() >= 10) {
                    reached10 = true;
                    System.out.println(players[0].getFullName() + " scored 10 points and won!");
                }
            } else if (players[1].getScore() > players[0].getScore()){
                if (players[1].getScore() >= 10) {
                    reached10 = true;
                    System.out.println(players[1].getFullName() + " scored 10 points and won!");
                }
            } else {
                if (players[0].getScore() >= 10) {
                    reached10 = true;
                    System.out.println(players[0].getFullName() + " and " + players[1].getFullName() + " tied!");
                }
            }
        }

        if (playAgain(console)) {
            if (reached10) {
                players[0].resetScore();
                players[1].resetScore();
            }
            System.out.println("\nScore:");
            System.out.println(players[0] + ": " + players[0].getScore());
            System.out.println(players[1] + ": " + players[1].getScore());
            System.out.println();

            playGame(players, new Board(), console);
        } else {
            System.out.println("\nFinal Score:");
            System.out.println(players[0] + ": " + players[0].getScore());
            System.out.println(players[1] + ": " + players[1].getScore());
            System.out.println();
        }
    }

    private static boolean playAgain(Scanner console) {
        System.out.println("Play again? y for yes, anything else for no ");
        Scanner line = new Scanner(console.nextLine());
        if (line.next().equals("y")) {
            return true;
        }
        return false;
    }

    private static Player[] getPlayers(int numPlayers, Scanner console) {
        Player[] players = new Player[3];
        for (int i = 0; i < numPlayers; i++) {
            players[i] = getHumanPlayer(i + 1, console);
        }
        if (numPlayers == 1) {
            players[1] = getComputerPlayer();
        }

        players[2] = new Nobody();
        return players;
    }

    private static String intro() {
        return "Welcome to Plus4, an unoriginal game by Bianca Champenois that she won at a math competition. \n" +
                "Play by dropping pieces into one of the four columns (0, 1, 2, or 3)\n" +
                "and connect four in a row to win. Good luck!";
    }

    public static int getNumPlayers(Scanner console) {
        String errorMessage = "You entered an invalid input. You can have either 1 or 2 players.\n" +
                "Please try again: ";
        System.out.print("How many people want to play? ");
        int numPlayers = 0;
        Scanner line = new Scanner(console.nextLine());
        if (line.hasNextInt()) {
            int players = line.nextInt();
            if (players < 1 || players > 2) {
                numPlayers = getNumberBetween(1, 3, errorMessage, console);
            } else {
                numPlayers = players;
            }
        } else {
            numPlayers = getNumberBetween(1, 3, errorMessage,  console);
        }
        return numPlayers;
    }

    private static int getNumberBetween(int lowerBoundInlusive, int upperBoundExclusive, String message, Scanner console) {
        System.out.print(message);
        Scanner line = new Scanner(console.nextLine());
        if (line.hasNextInt()) {
            int players = line.nextInt();
            if (players < lowerBoundInlusive || players >= upperBoundExclusive) {
                return getNumberBetween(lowerBoundInlusive, upperBoundExclusive, message, console);
            } else {
                return players;
            }
        } else {
            return getNumberBetween(lowerBoundInlusive, upperBoundExclusive, message, console);
        }
    }

    public static Player getHumanPlayer(int playerNum, Scanner console) {
        return new HumanPlayer(getName(playerNum, console));
    }

    public static String getName(int playerNum, Scanner console) {
        System.out.print("What is Player " + playerNum + "s name? ");
        return console.nextLine();
    }

    public static Player getComputerPlayer() {
        return new ComputerPlayer();
    }
}
