package myGame.gameplay;

import java.util.Scanner;

/**
 * Created by liamkreiss on 10/28/17.
 */
public class CompressedGame {
    private int winner;
    private String moves;

    public CompressedGame(String line) {
        Scanner game = new Scanner(line);
        if (game.hasNextInt()) {
            moves = game.nextInt() + "";
        }
        while (game.hasNextInt()) {
            moves += " " + game.nextInt();
        }

        String winner = game.next();
        if (winner.equals("Computer1")) {
            this.winner = 0;
        } else if (winner.equals("Computer2")) {
            this.winner = 1;
        } else {
            this.winner = 2;
        }
    }

    public int getWinner() {
        return this.winner;
    }

    public String getMoves() {
        return this.moves;
    }
}
