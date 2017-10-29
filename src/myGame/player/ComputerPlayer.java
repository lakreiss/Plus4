package myGame.player;

import myGame.gameplay.Board;
import myGame.gameplay.Move;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * Created by liamkreiss on 10/7/17.
 */
public class ComputerPlayer extends Player {

    public int getPlay(Board board, Scanner scanner) {
        return -1;
    }

    int playWithMovesInAdvance(Board board, int movesInAdvance) {
        ArrayList<Move> allMoves = new ArrayList<>();
        for (int i = 0; i < board.getWidth(); i++) {
            Board curBoardState = new Board(board);
            allMoves.add(new Move(curBoardState, i, this, movesInAdvance));
        }

        Collections.sort(allMoves);
        return allMoves.get(0).getCol();
    }
}
