package myGame.player;

import myGame.gameplay.Board;

import java.util.Scanner;

/**
 * Created by liamkreiss on 10/28/17.
 */
public class EasyComputer extends ComputerPlayer {

    public EasyComputer() {
        super("Easy");
    }

    public EasyComputer(int playerNumber) {
        super(playerNumber);
    }

    public int getPlay(Board board) {
        int boardSize = board.getWidth();

        return ((int) (Math.random() * 10)) % boardSize;
    }

    public int getPlay(Board board, Scanner console) {
        return getPlay(board);
    }
}
