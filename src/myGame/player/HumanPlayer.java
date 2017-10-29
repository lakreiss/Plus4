package myGame.player;

import myGame.gameplay.Board;

import java.util.Scanner;

/**
 * Created by liamkreiss on 10/7/17.
 */
public class HumanPlayer extends Player {

    public HumanPlayer(String name) {
        super(name);
    }

    public int getPlay(Board gameBoard, Scanner console) {
        System.out.println("Where do you want to play?");
        String errorMessage = "You entered an invalid input. Choose a column between 0 and "
                + (gameBoard.getWidth() - 1) + ".\n" + "Please try again: ";
        int column;
        Scanner line = new Scanner(console.nextLine());
        if (line.hasNextInt()) {
            column = line.nextInt();
            if (column < 0 || column >= gameBoard.getWidth()) {
                column = getNumberBetween(0, gameBoard.getWidth(), errorMessage, console);
            }
        } else {
            column = getNumberBetween(0, gameBoard.getWidth(), errorMessage,  console);
        }
        return column;

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
}
