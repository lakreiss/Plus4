package myGame.player;

import myGame.gameplay.Board;

import java.util.Scanner;

/**
 * Created by liamkreiss on 10/28/17.
 */
public class MediumComputer extends ComputerPlayer {
    private final int MOVES_IN_ADVANCE = 1;

    public MediumComputer() {
        super();
    }

    public MediumComputer(int playerNumber) {
        super(playerNumber);
    }

    public int getPlay(Board board) {
        return super.playWithMovesInAdvance(board, MOVES_IN_ADVANCE);
    }

    public int getPlay(Board board, Scanner console) {
        return getPlay(board);
    }
}
