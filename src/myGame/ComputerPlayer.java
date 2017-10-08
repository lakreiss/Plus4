package myGame;

import java.util.Scanner;

/**
 * Created by liamkreiss on 10/7/17.
 */
public class ComputerPlayer extends Player {
    private String difficulty;

    public ComputerPlayer() {
        super();
        this.difficulty = "easy";
    }

    public ComputerPlayer(String difficulty) {
        this.difficulty = difficulty;
    }

    //Has the scanner variable for overriding, but isn't used
    public int getPlay(Board board, Scanner scanner) {
        if (this.difficulty.equals("easy")){
            return ((int) (Math.random() * 12)) % 4;
        } else {
            //TODO
        }

        return 1;
    }
}
