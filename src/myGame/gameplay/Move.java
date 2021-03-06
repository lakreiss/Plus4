package myGame.gameplay;

import myGame.player.Player;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by liamkreiss on 10/7/17.
 */
public class Move implements Comparable {
    private final double MAX_SCORE = 10.0;
    private final double MIN_SCORE = 0.0;
    private int col;
    private double score; //out of MAX_SCORE

    /**
     * col is the column which the move places a piece at
     * startingBoard is the board state before the piece is played
     * @param startingBoard
     * @param col
     */
    public Move(Board startingBoard, int col, Player curPlayer, int movesInAdvance) {
        this.col = col;
        this.score = calcScore(startingBoard, col, curPlayer, movesInAdvance);
    }

    private double calcScore(Board startingBoard, int col, Player curPlayer, int movesInAdvance) {
        startingBoard.addPiece(curPlayer, col);
        Player winningPlayer = startingBoard.getWinningPlayer(curPlayer.getPlayers());
        String winner = winningPlayer.getFullName();
        if (winner.equals(curPlayer.getFullName())) {
            return MAX_SCORE;
        } else if (winner.equals(curPlayer.getOpponent().getFullName())) {
            return MIN_SCORE;
        } else {
            if (movesInAdvance > 1) {
                ArrayList<Move> allMoves = new ArrayList<>();
                for (int i = 0; i < startingBoard.getWidth(); i++) {
                    Board newBoard = new Board(startingBoard);
                    allMoves.add(new Move(newBoard, i, curPlayer.getOpponent(), movesInAdvance - 1));
                }

                Collections.sort(allMoves);

                //MAX_SCORE - 1 so that 'not losing' isnt as good as 'winning'
                return (MAX_SCORE - 1) - allMoves.get(0).getScore();
            } else {
                return (MAX_SCORE / 2) + (Math.random() * (MAX_SCORE - MIN_SCORE) * 2 / 3)
                        - ((MAX_SCORE - MIN_SCORE) / 3);
            }
        }
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof Move) {
            return (int) ((Move) o).score - (int) this.score;
        }
        return Integer.MIN_VALUE;
    }

    public double getScore() {
        return this.score;
    }

    public int getCol() {
        return this.col;
    }
}
