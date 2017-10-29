package myGame.player;

import myGame.gameplay.Board;

import java.util.Scanner;

/**
 * Created by liamkreiss on 10/28/17.
 */
public class EasyComputer extends ComputerPlayer {

    public EasyComputer() {
        super("easy");
    }

    public int getPlay(Board board, Scanner scanner) {
        int boardSize = board.getWidth();

        return ((int) (Math.random() * 10)) % boardSize;
    }
}
