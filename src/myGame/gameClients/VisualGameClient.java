package myGame.gameClients;

import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.scene.*;
import javafx.application.Application;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import myGame.gameplay.Board;
import myGame.player.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by liamkreiss on 6/15/18.
 */
public class VisualGameClient extends Application {
    private static final int CANVAS_WIDTH = 512;
    private static final int CANVAS_HEIGHT = 512;
    private static final int BOARD_SIZE = 4;
    private static final int SQUARE_SIZE = 50;
    private static final int PIECE_RADIUS = (SQUARE_SIZE - 4) / 2;
    private static final int UPPER_LEFT_BOARD_X = 150, UPPER_LEFT_BOARD_Y = 150;
    private static final int CENTER_X_RED = 175 + (SQUARE_SIZE/2), CENTER_Y_RED = 100;
    private static final int CENTER_X_BLUE = 275 + (SQUARE_SIZE/2), CENTER_Y_BLUE = 100;


    private static final boolean saveToData = false;
    private static final String DATA_FILE = "sorted_data1511502410478";
    private static final boolean GAME_TO_10 = true;


    private static Difficulty difficulty = Difficulty.EASY;
    private static int numPlayers = 2;
    
    private static Rectangle[] columns;
    private static Circle[] gamePieces;
    private static Circle[] topPieces;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage theStage) throws FileNotFoundException, InterruptedException {
        theStage.setTitle("Plus Four");

        Group root = new Group();
        Scene theScene = new Scene(root);
        theStage.setScene( theScene );

        Canvas canvas = new Canvas( CANVAS_WIDTH, CANVAS_HEIGHT );
        root.getChildren().add( canvas );

        GraphicsContext gc = canvas.getGraphicsContext2D();

        PrintStream output = null;

        if (saveToData) {
            output = new PrintStream(new FileOutputStream(DATA_FILE, true));
        }

        Player[] players = getPlayers(numPlayers, true);

        createClickables();

        playGame(theStage, theScene, gc, players, new Board(), output);
    }

    private static void createClickables() {
        Rectangle col0 = new Rectangle(UPPER_LEFT_BOARD_X, UPPER_LEFT_BOARD_Y, SQUARE_SIZE, BOARD_SIZE * SQUARE_SIZE);
        Rectangle col1 = new Rectangle(UPPER_LEFT_BOARD_X + SQUARE_SIZE, UPPER_LEFT_BOARD_Y, SQUARE_SIZE, BOARD_SIZE * SQUARE_SIZE);
        Rectangle col2 = new Rectangle(UPPER_LEFT_BOARD_X + (2 * SQUARE_SIZE), UPPER_LEFT_BOARD_Y, SQUARE_SIZE, BOARD_SIZE * SQUARE_SIZE);
        Rectangle col3 = new Rectangle(UPPER_LEFT_BOARD_X + (3 * SQUARE_SIZE), UPPER_LEFT_BOARD_Y, SQUARE_SIZE, BOARD_SIZE * SQUARE_SIZE);
        columns = new Rectangle[]{col0, col1, col2, col3};

        Circle redPiece = new Circle(CENTER_X_RED, CENTER_Y_RED, PIECE_RADIUS);
        Circle bluePiece = new Circle(CENTER_X_BLUE, CENTER_Y_BLUE, PIECE_RADIUS);
        topPieces = new Circle[]{redPiece, bluePiece};

        int index = 0;
        gamePieces = new Circle[BOARD_SIZE * BOARD_SIZE];
        int curXCenter = UPPER_LEFT_BOARD_X + (SQUARE_SIZE / 2), curYCenter = UPPER_LEFT_BOARD_Y + (SQUARE_SIZE / 2);
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                Circle gamePiece = new Circle(curXCenter, curYCenter, PIECE_RADIUS);
                gamePieces[index] = gamePiece;
                index++;
                curXCenter += SQUARE_SIZE;
            }
            curXCenter -= BOARD_SIZE * SQUARE_SIZE;
            curYCenter += SQUARE_SIZE;
        }
    }

    /***
     *
     * @param theStage
     * @param theScene
     * @param gc
     * @param gameBoard
     * @param players
     * @param gameInfo
     * @param winningPlayer
     * @param winner
     * @param compressedGame
     */
    public static void showBoard(Stage theStage, Scene theScene, GraphicsContext gc, Board gameBoard, Player[] players, HashMap<String, Boolean> gameInfo, Player winningPlayer, String winner, String compressedGame, PrintStream output) {
        //makes the board (with black tiles)
        gc.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);

        int curX = UPPER_LEFT_BOARD_X, curY = UPPER_LEFT_BOARD_Y;
        gc.setFill(Color.BLACK);

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                gc.fillRect(curX, curY, SQUARE_SIZE - 2, SQUARE_SIZE - 2);
                curX += SQUARE_SIZE;
            }
            curX -= BOARD_SIZE * SQUARE_SIZE;
            curY += SQUARE_SIZE;
        }

        //Place red piece and make clickable
        gc.setFill(Color.RED);
        gc.fillOval(CENTER_X_RED - PIECE_RADIUS, CENTER_Y_RED - PIECE_RADIUS,
                PIECE_RADIUS * 2, PIECE_RADIUS * 2);

        //Place black piece and make clickable
        gc.setFill(Color.BLUE);
        gc.fillOval(CENTER_X_BLUE - PIECE_RADIUS, CENTER_Y_BLUE - PIECE_RADIUS,
                PIECE_RADIUS * 2, PIECE_RADIUS * 2);

        int index = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                int tileContents = gameBoard.pieceAt(i, j);
                if (tileContents != 0) {
                    if (tileContents == 1) {
                        gc.setFill(Color.RED);
                    } else if (tileContents == 2) {
                        gc.setFill(Color.BLUE);
                    } else {
                        throw new Error("invalid tile contents");
                    }
                    gc.fillOval(gamePieces[index].getCenterX() - PIECE_RADIUS,
                            gamePieces[index].getCenterY() - PIECE_RADIUS, PIECE_RADIUS * 2, PIECE_RADIUS * 2);
                }
                index++;
            }
        }

//        //TODO: make difficulty options and make clickable
//        gc.setFill(Color.GRAY);


        final boolean[] clicked = {false};
        final boolean[] clickedOnColumn = {false};
        final int[] columnClickedOn = {-1};

        theScene.setOnMouseClicked(
                e -> {
                    if (topPieces[0].contains(e.getX(), e.getY())) {
                        clicked[0] = true;
                        clickedOnColumn[0] = false;
                        System.out.println("clicked the red piece");
                    } else if (topPieces[1].contains(e.getX(), e.getY())) {
                        clicked[0] = true;
                        clickedOnColumn[0] = false;
                        System.out.println("clicked the blue piece");
                    } else if (columns[0].contains(e.getX(), e.getY())) {
                        clicked[0] = true;
                        clickedOnColumn[0] = true;
                        columnClickedOn[0] = 0;
                        System.out.println("clicked column 0");
                    } else if (columns[1].contains(e.getX(), e.getY())) {
                        clicked[0] = true;
                        clickedOnColumn[0] = true;
                        columnClickedOn[0] = 1;
                        System.out.println("clicked column 1");
                    } else if (columns[2].contains(e.getX(), e.getY())) {
                        clicked[0] = true;
                        clickedOnColumn[0] = true;
                        columnClickedOn[0] = 2;
                        System.out.println("clicked column 2");
                    } else if (columns[3].contains(e.getX(), e.getY())) {
                        clicked[0] = true;
                        clickedOnColumn[0] = true;
                        columnClickedOn[0] = 3;
                        System.out.println("clicked column 3");
                    }

                    if (clicked[0]) {
                        if (clickedOnColumn[0]) {
                            makeMove(columnClickedOn[0], players, theStage, theScene, gc, gameBoard, gameInfo, winningPlayer, winner, compressedGame, output);
                        } else {
                            showBoard(theStage, theScene, gc, gameBoard, players, gameInfo, winningPlayer, winner, compressedGame, output);
                        }
                    }
                }
        );

        System.out.println("Got here");

        theStage.show();

//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        System.out.println("Got here 2");

//        int counter = 100000000;
//        while (!clicked[0] && counter > 0) {
//            counter -= 1;
//        }


//        if (!clicked[0]) {
//            try {
//                Thread.sleep(10000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            return showBoard(theStage, theScene, gc, board);
//        }

//        System.out.println("Got here 3");
    }

    private static void makeMove(int column, Player[] players, Stage theStage, Scene theScene, GraphicsContext gc, Board gameBoard, HashMap<String, Boolean> gameInfo, Player winningPlayer, String winner, String compressedGame, PrintStream output) {


        Player curPlayer;
        if (gameInfo.get("p1Turn")) {
            curPlayer = players[0];
        } else {
            curPlayer = players[1];
        }

        gameBoard.addPiece(curPlayer, column);

        winningPlayer = gameBoard.getWinningPlayer(players);
        winner = winningPlayer.getFullName();
        gameInfo.put("p1Turn", !gameInfo.get("p1Turn"));
        compressedGame = gameBoard.getMoves();
        if (!winner.equals("")) {
            gameInfo.put("gameOver", true);
        } else if (compressedGame.length() > 19) {
            if (compressedGame.substring(compressedGame.length() - 19).equals("0 0 0 0 0 0 0 0 0 0")
                    || compressedGame.substring(compressedGame.length() - 19).equals("1 1 1 1 1 1 1 1 1 1")
                    || compressedGame.substring(compressedGame.length() - 19).equals("2 2 2 2 2 2 2 2 2 2")
                    || compressedGame.substring(compressedGame.length() - 19).equals("3 3 3 3 3 3 3 3 3 3")) {
                gameInfo.put("gameOver", true);
                winner = "tie";
            }
        }

        if (gameInfo.get("gameOver")) {
            System.out.println("");
            if (winner.equals("tie")) {
                System.out.println("It's a tie!");
            } else {
                System.out.println(winner + " wins!");
                winningPlayer.wonGame();
            }

//        System.out.println("Game log: " + gameBoard.getMoves());

            System.out.println(gameBoard.toString());


            //needs human player to be player 1
            if (saveToData && winningPlayer instanceof HumanPlayer
                    && winningPlayer.getOpponent() instanceof ComputerPlayer) {
                compressedGame = gameBoard.getMoves() + " " + "Computer" + (winningPlayer.getPlayerNumber() + 1) + " wins!";
                output.println(compressedGame);
            }

            try {
                checkGameOver(theStage, gc, players, gameInfo.get("reached10"), output);
                playGame(theStage, theScene, gc, players, gameBoard, output);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } else {
            showBoard(theStage, theScene, gc, gameBoard, players, gameInfo, winningPlayer, winner, compressedGame, output);
        }
    }

    private static void playGame(Stage theStage, Scene theScene, GraphicsContext gc, Player[] players,
                                 Board gameBoard, PrintStream output) throws InterruptedException {
        HashMap<String, Boolean> gameInfo= new HashMap<String, Boolean>();
        gameInfo.put("reached10", false);
        gameInfo.put("gameOver", false);
        gameInfo.put("p1Turn", true);
        Player winningPlayer = null;
        String winner = "";
        String compressedGame = "";

        showBoard(theStage, theScene, gc, gameBoard, players, gameInfo, winningPlayer, winner, compressedGame, output);

    }

    private static void checkGameOver(Stage theStage, GraphicsContext gc, Player[] players,
                                      boolean reached10, PrintStream output) throws InterruptedException {
        if (GAME_TO_10) {

            if (players[0].getScore() > players[1].getScore()) {
                if (players[0].getScore() >= 10) {
                    reached10 = true;
                    System.out.println(players[0].getFullName() + " scored 10 points and won!");
                }
            } else if (players[1].getScore() > players[0].getScore()){
                if (players[1].getScore() >= 10) {
                    reached10 = true;
                    System.out.println(players[1].getFullName() + " scored 10 points and won!");
                }
            } else {
                if (players[0].getScore() >= 10) {
                    reached10 = true;
                    System.out.println(players[0].getFullName() + " and " + players[1].getFullName() + " tied!");
                }
            }
        }

        if (reached10) {
            players[0].resetScore();
            players[1].resetScore();
        }

        gc.setFill(Color.WHITE);
        gc.fill();
//        System.out.println("\nScore:");
//        System.out.println(players[0] + ": " + players[0].getScore());
//        System.out.println(players[1] + ": " + players[1].getScore());
//        System.out.println();

        theStage.show();
        Thread.sleep(1000);
    }

    //numPlayers should be either 1 or 2
    private static Player[] getPlayers(int numPlayers, boolean computerFirst) {
        Player[] players = new Player[3];

        if (computerFirst && numPlayers == 1) {
            players[0] = getComputerPlayer();
            players[1] = getHumanPlayer(2);
        } else {
            for (int i = 0; i < numPlayers; i++) {
                players[i] = getHumanPlayer(i + 1);
            }
            if (numPlayers == 1) {
                players[1] = getComputerPlayer();
            }
        }

        players[2] = new Nobody();
        Player.setPlayers(players);
        return players;
    }

    //currently makes everyone named Liam, should be changed later
    public static Player getHumanPlayer(int playerNum) {
        return new HumanVisualPlayer("Liam");
    }

    public static Player getComputerPlayer() {
        if (difficulty.equals(Difficulty.EASY)) {
            return new EasyComputer();
        } else if (difficulty.equals(Difficulty.MEDIUM)) {
            return new MediumComputer();
        } else if (difficulty.equals(Difficulty.HARD)) {
            return new HardComputer();
        } else if (difficulty.equals(Difficulty.INTELLIGENT)) {
            return new IntelligentComputer();
        } else {
            throw new Error("invalid difficulty");
        }
    }
}
