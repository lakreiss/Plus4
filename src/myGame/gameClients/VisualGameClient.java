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
    private static final int CANVAS_WIDTH = 512, CANVAS_HEIGHT = 512;
    private static final int BOARD_SIZE = 4, SQUARE_SIZE = 50;
    private static final int PIECE_RADIUS = (SQUARE_SIZE - 4) / 2;
    private static final int OUTLINE_RADIUS = PIECE_RADIUS + 5;
    private static final int INLINE_RADIUS = PIECE_RADIUS - 10;

    private static final int BORDER_OUTLINE_RADIUS = OUTLINE_RADIUS + 1;
    private static final int UPPER_LEFT_BOARD_X = 150, UPPER_LEFT_BOARD_Y = 150;
    private static final int CENTER_X_RED = 175 + (SQUARE_SIZE/2), CENTER_Y_RED = 100;
    private static final int CENTER_X_BLUE = 275 + (SQUARE_SIZE/2), CENTER_Y_BLUE = 100;
    private static final int ORIG_TEXT_X = 20, ORIG_TEXT_Y = UPPER_LEFT_BOARD_Y + 5, ORIG_TEXT_STEP_SIZE = 15;
    private static final int BUTTON_WIDTH = 50, BUTTON_HEIGHT = 30, BUTTON_DIST_BELOW_ORIG_TEXT_Y = 60, DIST_BETWEEN_BUTTONS = 10;


    private static final boolean saveToData = false;
    private static final String DATA_FILE = "sorted_data1511502410478";
    private static final boolean GAME_TO_10 = true;


    private static Difficulty difficulty = Difficulty.EASY;
    private static int numPlayers = 2;
    
    private static Rectangle[] columns;
    private static Rectangle[] buttons;
    private static Circle[] gamePieces;
    private static Circle[] topPieces;
    private static HashMap<String, Boolean> gameInfo= new HashMap<String, Boolean>();


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

        //makes columns
        columns = new Rectangle[BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            Rectangle col = new Rectangle(UPPER_LEFT_BOARD_X + (i * SQUARE_SIZE), UPPER_LEFT_BOARD_Y, SQUARE_SIZE, BOARD_SIZE * SQUARE_SIZE);
            columns[i] = col;
        }

        //makes top pieces
        Circle redPiece = new Circle(CENTER_X_RED, CENTER_Y_RED, PIECE_RADIUS);
        Circle bluePiece = new Circle(CENTER_X_BLUE, CENTER_Y_BLUE, PIECE_RADIUS);
        topPieces = new Circle[]{redPiece, bluePiece};

        //makes game pieces
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

        //make buttons
        //TODO: change from 2 when I add more buttons
        buttons = new Rectangle[2];

        //makes play again buttons
        Rectangle playAgainYes = new Rectangle(ORIG_TEXT_X, ORIG_TEXT_Y + BUTTON_DIST_BELOW_ORIG_TEXT_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
        Rectangle playAgainNo = new Rectangle(ORIG_TEXT_X + BUTTON_WIDTH + 10, ORIG_TEXT_Y + BUTTON_DIST_BELOW_ORIG_TEXT_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
        buttons[0] = playAgainYes;
        buttons[1] = playAgainNo;
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

        drawBoard(gc, gameBoard);

        HashMap<String, Boolean> clickInfo = new HashMap<>();
        clickInfo.put("clicked", false);
        clickInfo.put("clickedOnColumn", false);
        final int[] columnClickedOn = {-1};

        theScene.setOnMouseClicked(
                e -> {
                    if (topPieces[0].contains(e.getX(), e.getY())) {
                        clickInfo.put("clicked", true);
                        clickInfo.put("clickedOnColumn", false);
                        int columnClickedO = 3;
                        System.out.println("clicked the red piece");
                    } else if (topPieces[1].contains(e.getX(), e.getY())) {
                        clickInfo.put("clicked", true);
                        clickInfo.put("clickedOnColumn", false);
                        System.out.println("clicked the blue piece");
                    } else if (columns[0].contains(e.getX(), e.getY())) {
                        clickInfo.put("clicked", true);
                        clickInfo.put("clickedOnColumn", true);
                        columnClickedOn[0] = 0;
                        System.out.println("clicked column 0");
                    } else if (columns[1].contains(e.getX(), e.getY())) {
                        clickInfo.put("clicked", true);
                        clickInfo.put("clickedOnColumn", true);
                        columnClickedOn[0] = 1;
                        System.out.println("clicked column 1");
                    } else if (columns[2].contains(e.getX(), e.getY())) {
                        clickInfo.put("clicked", true);
                        clickInfo.put("clickedOnColumn", true);
                        columnClickedOn[0] = 2;
                        System.out.println("clicked column 2");
                    } else if (columns[3].contains(e.getX(), e.getY())) {
                        clickInfo.put("clicked", true);
                        clickInfo.put("clickedOnColumn", true);
                        columnClickedOn[0] = 3;
                        System.out.println("clicked column 3");
                    }

                    if (clickInfo.get("clicked")) {
                        if (clickInfo.get("clickedOnColumn")) {
                            makeMove(columnClickedOn[0], players, theStage, theScene, gc, gameBoard, gameInfo, winningPlayer, winner, compressedGame, output);
                        } else {
                            showBoard(theStage, theScene, gc, gameBoard, players, gameInfo, winningPlayer, winner, compressedGame, output);
                        }
                    }
                }
        );

        System.out.println("clicked something clickable");

        theStage.show();
    }

    private static void drawBoard(GraphicsContext gc, Board gameBoard) {
        gc.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);

        int curX = UPPER_LEFT_BOARD_X, curY = UPPER_LEFT_BOARD_Y;
        gc.setFill(Color.BLACK);

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                gc.fillRect(curX + 1, curY + 1, SQUARE_SIZE - 2, SQUARE_SIZE - 2);
                curX += SQUARE_SIZE;
            }
            curX -= BOARD_SIZE * SQUARE_SIZE;
            curY += SQUARE_SIZE;
        }

        //Place yellow circle around the piece to show who's turn it is
        gc.setFill(Color.BLACK);
        if (gameInfo.get("p1Turn")) {
            gc.fillOval(CENTER_X_RED - BORDER_OUTLINE_RADIUS, CENTER_Y_RED - BORDER_OUTLINE_RADIUS,
                    (BORDER_OUTLINE_RADIUS * 2), (BORDER_OUTLINE_RADIUS * 2));
            gc.setFill(Color.YELLOW);
            gc.fillOval(CENTER_X_RED - OUTLINE_RADIUS, CENTER_Y_RED - OUTLINE_RADIUS,
                    (OUTLINE_RADIUS * 2), (OUTLINE_RADIUS * 2));
        } else {
            gc.fillOval(CENTER_X_BLUE - BORDER_OUTLINE_RADIUS, CENTER_Y_BLUE - BORDER_OUTLINE_RADIUS,
                    (BORDER_OUTLINE_RADIUS * 2), (BORDER_OUTLINE_RADIUS * 2));
            gc.setFill(Color.YELLOW);
            gc.fillOval(CENTER_X_BLUE - OUTLINE_RADIUS, CENTER_Y_BLUE - OUTLINE_RADIUS,
                    (OUTLINE_RADIUS * 2), (OUTLINE_RADIUS * 2));
        }

        //Place red piece and make clickable
        gc.setFill(Color.RED);
        gc.fillOval(CENTER_X_RED - PIECE_RADIUS, CENTER_Y_RED - PIECE_RADIUS,
                PIECE_RADIUS * 2, PIECE_RADIUS * 2);

        //Place black piece and make clickable
        gc.setFill(Color.BLUE);
        gc.fillOval(CENTER_X_BLUE - PIECE_RADIUS, CENTER_Y_BLUE - PIECE_RADIUS,
                PIECE_RADIUS * 2, PIECE_RADIUS * 2);

//        //Place yellow circle inside the piece to show who's turn it is
//        //Replaced this with the outline radius because i thought it looked better
//        //Might change back at some point, so don't delete.
//        gc.setFill(Color.YELLOW);
//        if (gameInfo.get("p1Turn")) {
//            gc.fillOval(CENTER_X_RED - INLINE_RADIUS, CENTER_Y_RED - INLINE_RADIUS,
//                    (INLINE_RADIUS * 2), (INLINE_RADIUS * 2));
//        } else {
//            gc.fillOval(CENTER_X_BLUE - INLINE_RADIUS, CENTER_Y_BLUE - INLINE_RADIUS,
//                    (INLINE_RADIUS * 2), (INLINE_RADIUS * 2));
//        }

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

//        System.out.println("Game log: " + gameBoard.getMoves());

//            System.out.println(gameBoard.toString());


            //needs human player to be player 1
            if (saveToData && winningPlayer instanceof HumanPlayer
                    && winningPlayer.getOpponent() instanceof ComputerPlayer) {
                compressedGame = gameBoard.getMoves() + " " + "Computer" + (winningPlayer.getPlayerNumber() + 1) + " wins!";
                output.println(compressedGame);
            }

            System.out.println("");
            if (winner.equals("tie")) {
                System.out.println("It's a tie!");
            } else {
                winningPlayer.wonGame();
                System.out.println(winner + " wins!");
//                    checkGameOver(theStage, gc, players, gameInfo.get("reached10"), output);
                gameOverScreen(theStage, theScene, gc, players, gameBoard, winningPlayer, output);
//                playGame(theStage, theScene, gc, players, new Board(), output);

            }

        } else {
            showBoard(theStage, theScene, gc, gameBoard, players, gameInfo, winningPlayer, winner, compressedGame, output);
        }
    }

    private static void gameOverScreen(Stage theStage, Scene theScene, GraphicsContext gc, Player[] players, Board gameBoard, Player winningPlayer, PrintStream output) {
        drawBoard(gc, gameBoard);

        int curTextY = ORIG_TEXT_Y;

        gc.setFill(Color.BLACK);
        gc.fillText("Game Over", ORIG_TEXT_X, curTextY += ORIG_TEXT_STEP_SIZE);
        gc.fillText(winningPlayer.getFullName() + " has won! ", ORIG_TEXT_X, curTextY += ORIG_TEXT_STEP_SIZE);
        gc.fillText("Play again?", ORIG_TEXT_X, curTextY += ORIG_TEXT_STEP_SIZE);

        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(ORIG_TEXT_X, ORIG_TEXT_Y + BUTTON_DIST_BELOW_ORIG_TEXT_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
        gc.fillRect(ORIG_TEXT_X + BUTTON_WIDTH + DIST_BETWEEN_BUTTONS, ORIG_TEXT_Y + BUTTON_DIST_BELOW_ORIG_TEXT_Y, BUTTON_WIDTH, BUTTON_HEIGHT);

        gc.setFill(Color.BLACK);
        gc.fillText("Yes", ORIG_TEXT_X + 13, ORIG_TEXT_Y + BUTTON_DIST_BELOW_ORIG_TEXT_Y + (BUTTON_HEIGHT / 2) + 5);
        gc.fillText("No", ORIG_TEXT_X + BUTTON_WIDTH + DIST_BETWEEN_BUTTONS + 15, ORIG_TEXT_Y + BUTTON_DIST_BELOW_ORIG_TEXT_Y + (BUTTON_HEIGHT / 2) + 5);


        HashMap<String, Boolean> clickInfo = new HashMap<>();
        clickInfo.put("clicked", false);
        clickInfo.put("clickedOnPlayAgain", false);
        clickInfo.put("clickedOnNotPlayAgain", false);

        theScene.setOnMouseClicked(
                e -> {
                    if (buttons[0].contains(e.getX(), e.getY())) {
                        clickInfo.put("clicked", true);
                        clickInfo.put("clickedOnPlayAgain", true);
                        clickInfo.put("clickedOnNotPlayAgain", false);
                        System.out.println("clicked play again");
                    } else if (buttons[1].contains(e.getX(), e.getY())) {
                        clickInfo.put("clicked", true);
                        clickInfo.put("clickedOnPlayAgain", false);
                        clickInfo.put("clickedOnNotPlayAgain", true);
                        System.out.println("clicked not play again");
                    }

                    if (clickInfo.get("clicked")) {
                        if (clickInfo.get("clickedOnPlayAgain")) {
                            try {
                                playGame(theStage, theScene, gc, players, new Board(), output);
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
//                            makeMove(columnClickedOn[0], players, theStage, theScene, gc, gameBoard, gameInfo, winningPlayer, winner, compressedGame, output);
                        } else {
                            //TODO: quit window
                            System.exit(0);
//                            showBoard(theStage, theScene, gc, gameBoard, players, gameInfo, winningPlayer, winner, compressedGame, output);
                        }
                    }
                }
        );

        System.out.println("clicked something clickable on game over screen");

        theStage.show();

    }

    private static void playGame(Stage theStage, Scene theScene, GraphicsContext gc, Player[] players,
                                 Board gameBoard, PrintStream output) throws InterruptedException {
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

//        System.out.println("\nScore:");
//        System.out.println(players[0] + ": " + players[0].getScore());
//        System.out.println(players[1] + ": " + players[1].getScore());
//        System.out.println();

        theStage.show();
        Thread.sleep(5000);
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
