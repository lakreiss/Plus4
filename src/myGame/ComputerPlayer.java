package myGame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * Created by liamkreiss on 10/7/17.
 */
public class ComputerPlayer extends Player {
    private final int MEDIUM_MOVES_IN_ADVANCE = 1;
    private final int HARD_MOVES_IN_ADVANCE = 2;

    private String difficulty;

    public ComputerPlayer() {
        super();
        this.difficulty = "hard";
    }

    public ComputerPlayer(String difficulty) {
        this.difficulty = difficulty;
    }

    //Has the scanner variable for overriding, but isn't used
    public int getPlay(Board board, Scanner scanner) {
        if (this.difficulty.equals("easy")) {
            return ((int) (Math.random() * 12)) % 4;
        } else if (this.difficulty.equals("medium")) {
            return (playNotEasy(board, MEDIUM_MOVES_IN_ADVANCE));
        } else if (this.difficulty.equals("hard")) {
            return (playNotEasy(board, HARD_MOVES_IN_ADVANCE));
        }

        return 1;
    }

    private int playNotEasy(Board board, int movesInAdvance) {
        ArrayList<Move> allMoves = new ArrayList<>();
        for (int i = 0; i < board.getWidth(); i++) {
            Board curBoardState = new Board(board);
            allMoves.add(new Move(curBoardState, i, this, movesInAdvance));
        }

        Collections.sort(allMoves);
        return allMoves.get(0).getCol();
    }
}
