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

    public HardComputer(int playerNumber) {
        super("Hard", playerNumber);
    }

    public HardComputer(String name, int playerNumber) {
        super(name, playerNumber);
    }

    public HardComputer(String name) {
        super(name);
    }

    public int getPlay(Board board, Scanner scanner) {
        return getPlay(board);
    }

    public int getPlay(Board board) {
        return super.playWithMovesInAdvance(board, MOVES_IN_ADVANCE);
    }
}
