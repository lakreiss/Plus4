package myGame.player;

import myGame.gameplay.Board;

import java.util.Scanner;

/**
 * Created by liamkreiss on 10/28/17.
 */
public class HardComputer extends ComputerPlayer{
    private final int MOVES_IN_ADVANCE = 2;

    public HardComputer() {
        super();
    }

    public HardComputer(String name) {
        super(name);
    }

    public int getPlay(Board board, Scanner scanner) {
        return super.playWithMovesInAdvance(board, MOVES_IN_ADVANCE);
    }

    public int getPlay(Board board) {
        return getPlay(board, null);
    }
}
