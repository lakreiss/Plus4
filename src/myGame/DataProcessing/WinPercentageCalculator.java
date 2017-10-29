package myGame.DataProcessing;

import myGame.gameplay.CompressedGame;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by liamkreiss on 10/28/17.
 */
public class WinPercentageCalculator {
    private static final String DATA_FILE = "sorted_data1509263547617";

    //fill out every run
    private static final String MOVES_SO_FAR = "3 0";
    private static final int PLAYER_NUMBER = 1;

    public static void main(String[] args) throws FileNotFoundException {
        Scanner fullData = new Scanner(new File(DATA_FILE));
        ArrayList<CompressedGame> gamesWithSameMovesSoFar = new ArrayList<>();

        while (fullData.hasNextLine()) {
            String game = fullData.nextLine();
            if (game.substring(0, MOVES_SO_FAR.length()).equals(MOVES_SO_FAR)) {
                CompressedGame cg = new CompressedGame(game);
                gamesWithSameMovesSoFar.add(cg);
            }
        }

        double curWinPercent = calcWinPercentageInCurrentState(gamesWithSameMovesSoFar);

        System.out.printf("Your current win percentage is %.3f%n", curWinPercent * 100); //TODO

        double[] winPercentagesAfterMove = new double[4];
        for (int i = 0; i < 4; i++) {
            winPercentagesAfterMove[i] = calcWinPercentageAfterMove(i, gamesWithSameMovesSoFar);
        }

        int bestMove = 0;
        double bestScore = winPercentagesAfterMove[0];
        for (int i = 1; i < 4; i++) {
            if (winPercentagesAfterMove[i] > bestScore) {
                bestScore = winPercentagesAfterMove[i];
                bestMove = i;
            }
        }

        System.out.printf("Your best move is " + bestMove + " with a win percentage of %.3f%s %n", bestScore * 100, "%");

    }

    private static double calcWinPercentageAfterMove(int i, ArrayList<CompressedGame> gamesWithSameMovesSoFar) {
        int totalGames = 0;
        int gamesWon = 0;

        String theoreticalMoves = MOVES_SO_FAR + " " + i;

        if (MOVES_SO_FAR.equals("")) {
            theoreticalMoves = "" + i;
        }

        for (CompressedGame cg : gamesWithSameMovesSoFar) {
            if (cg.getMoves().substring(0, theoreticalMoves.length()).equals(theoreticalMoves)) {
                if (cg.getWinner() == PLAYER_NUMBER) {
                    gamesWon++;
                }
                totalGames++;
            }
        }

        System.out.println(totalGames + " games with " + i + " played. " + gamesWon + " games won.");

        return gamesWon * 1.0 / totalGames;
    }

    private static double calcWinPercentageInCurrentState(ArrayList<CompressedGame> games) {
        int totalGames = 0;
        int gamesWon = 0;

        for (CompressedGame cg : games) {
            if (cg.getWinner() == PLAYER_NUMBER) {
                gamesWon++;
            }
            totalGames++;
        }

        System.out.println(totalGames + " games played. " + gamesWon + " games won.");

        return gamesWon * 1.0 / totalGames;
    }
}
