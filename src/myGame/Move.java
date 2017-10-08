package myGame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

/**
 * Created by liamkreiss on 10/7/17.
 */
public class Move implements Comparable {
    private Board startingBoard;
    private int col;
    private double score; //out of 10
    private int movesInAdvance;

    /**
     * col is the column which the move places a piece at
     * startingBoard is the board state before the piece is played
     * @param startingBoard
     * @param col
     */
    public Move(Board startingBoard, int col, Player curPlayer, int movesInAdvance) {
        this.startingBoard = startingBoard;
        this.col = col;
        this.movesInAdvance = movesInAdvance;
        this.score = calcScore(startingBoard, col, curPlayer, movesInAdvance);
    }

    private double calcScore(Board startingBoard, int col, Player curPlayer, int movesInAdvance) {
        startingBoard.addPiece(curPlayer, col);
        Player winningPlayer = startingBoard.getWinningPlayer(curPlayer.getPlayers());
        String winner = winningPlayer.getFullName();
        if (winner.equals(curPlayer.getFullName())) {
            return 10.0;
        } else if (winner.equals(curPlayer.getOpponent().getFullName())) {
            return 0.0;
        } else {
            if (movesInAdvance > 1) {
                ArrayList<Move> allMoves = new ArrayList<>();
                for (int i = 0; i < startingBoard.getWidth(); i++) {
                    Board newBoard = new Board(startingBoard);
                    allMoves.add(new Move(newBoard, i, curPlayer.getOpponent(), movesInAdvance - 1));
                }

                Collections.sort(allMoves);
                return 10.0 - allMoves.get(allMoves.size() - 1).getCol();
            } else {
                return 5.0 + Math.random() * 6 - 3;
            }
        }
    }

    public int getCol() {
        return this.col;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof Move) {
            return (int) ((Move) o).score - (int) this.score;
        }
        return Integer.MIN_VALUE;
    }
}
