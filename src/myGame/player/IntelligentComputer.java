package myGame.player;

import myGame.gameplay.Board;
import myGame.gameplay.CompressedGame;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by liamkreiss on 10/28/17.
 */
public class IntelligentComputer extends HardComputer {
    private static File DATA_FILE;

    public IntelligentComputer(String dataFile) {
        super();
        DATA_FILE = new File(dataFile);
    }

    public IntelligentComputer(String name, String dataFile) {
        super(name);
        DATA_FILE = new File(dataFile);
    }

    public IntelligentComputer(int playerNumber, String dataFile) {
        super(playerNumber);
        DATA_FILE = new File(dataFile);
    }

    public int getPlay(Board board, Scanner scanner) {
        return getPlay(board);
    }

    public int getPlay(Board board) {
        Scanner fullData = null;

        try {
            fullData = new Scanner(DATA_FILE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ArrayList<CompressedGame> gamesWithSameMovesSoFar = new ArrayList<>();

        while (fullData.hasNextLine()) {
            String game = fullData.nextLine();

            if (board.getMoves().length() >= game.length()) {
                return super.getPlay(board);
            }

            if (game.substring(0, board.getMoves().length()).equals(board.getMoves())) {
                CompressedGame cg = new CompressedGame(game);
                gamesWithSameMovesSoFar.add(cg);
            }
        }

        double[] winPercentagesAfterMove = new double[4];
        for (int i = 0; i < 4; i++) {
            winPercentagesAfterMove[i] = calcWinPercentageAfterMove(board.getMoves(), i, gamesWithSameMovesSoFar);
        }

        int bestMove = 0;
        double bestScore = winPercentagesAfterMove[0];
        for (int i = 1; i < 4; i++) {
            if (winPercentagesAfterMove[i] > bestScore) {
                bestScore = winPercentagesAfterMove[i];
                bestMove = i;
            }
        }

        if (bestScore <= 0) {
            return super.getPlay(board);
        }
        return bestMove;
    }

    private double calcWinPercentageAfterMove(String movesSoFar, int i, ArrayList<CompressedGame> gamesWithSameMovesSoFar) {
        int totalGames = 0;
        int gamesWon = 0;

        String theoreticalMoves = movesSoFar + " " + i;

        if (movesSoFar.equals("")) {
            theoreticalMoves = "" + i;
        }

        for (CompressedGame cg : gamesWithSameMovesSoFar) {
            if (theoreticalMoves.length() >= cg.getMoves().length()) {
                return -1;
            }
            if (cg.getMoves().substring(0, theoreticalMoves.length()).equals(theoreticalMoves)) {
                if (cg.getWinner() == this.playerNumber) {
                    gamesWon++;
                }
                totalGames++;
            }
        }

        if (totalGames == 0) {
            return -1;
        }
        return gamesWon * 1.0 / totalGames;
    }
}
