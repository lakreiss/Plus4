package myGame.gameClients;

import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.scene.*;
import javafx.application.Application;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.awt.TextField;
import java.awt.Label;
import myGame.gameplay.Board;
import myGame.player.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Scanner;

//TODO: Make pre-game screen, which could ask for names, difficulty level, etc.
//TODO: make it computer-playable

/**
 * Created by liamkreiss on 6/15/18.
 */
public class VisualGameClient extends Application {
    private static final int CANVAS_WIDTH = 512, CANVAS_HEIGHT = 512;
//    private static final int CANVAS_WIDTH = 600, CANVAS_HEIGHT = 600;
    private static final int BOARD_SIZE = 4, SQUARE_SIZE = 50;
    private static final int PIECE_RADIUS = (SQUARE_SIZE - 4) / 2;
    private static final int OUTLINE_RADIUS = PIECE_RADIUS + 5;
    private static final int INLINE_RADIUS = PIECE_RADIUS - 10;

    private static final int BORDER_OUTLINE_RADIUS = OUTLINE_RADIUS + 1;
    private static final int UPPER_LEFT_BOARD_X = (CANVAS_WIDTH / 2) - (2 * SQUARE_SIZE), UPPER_LEFT_BOARD_Y = (CANVAS_HEIGHT / 2) - (2 * SQUARE_SIZE);
    private static final int CENTER_X_RED = UPPER_LEFT_BOARD_X + SQUARE_SIZE, CENTER_Y_RED = UPPER_LEFT_BOARD_Y - SQUARE_SIZE;
    private static final int CENTER_X_BLUE = CENTER_X_RED + (2 * SQUARE_SIZE), CENTER_Y_BLUE = CENTER_Y_RED;
    private static final int PLAY_AGAIN_BUTTON_WIDTH = 50, PLAY_AGAIN_BUTTON_HEIGHT = 30, BUTTON_DIST_BELOW_ORIG_TEXT_Y = 60, DIST_BETWEEN_PLAY_AGAIN_BUTTONS = 10;
    private static final int ORIG_TEXT_X = (UPPER_LEFT_BOARD_X - (2 * PLAY_AGAIN_BUTTON_WIDTH) - DIST_BETWEEN_PLAY_AGAIN_BUTTONS) / 2, ORIG_TEXT_Y = UPPER_LEFT_BOARD_Y + 5, ORIG_TEXT_STEP_SIZE = 15;
    private static final int MENU_BUTTON_WIDTH = 100, MENU_BUTTON_HEIGHT = 100, UPPER_BUTTON_Y = 100, DIST_BETWEEN_DIFFICULTY_BUTTONS = 20;


    private static final boolean saveToData = false;
    private static final String DATA_FILE = "sorted_data1511502410478";
    private static final boolean GAME_TO_10 = true;

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

        Player[] players = new Player[3];

        createClickables();

        pregameScreen(theStage, theScene, gc, players, output);

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
        //TODO: change from 9 when I add more buttons
        buttons = new Rectangle[9];
        //0, 1 -> Play Agin (Yes/NO)
        //2, 3 -> Menu Buttons (How many players, who wants to go first)
        //4, 5, 6, 7 -> Difficulty buttons (easy, medium, hard, intelligent)
        //8 -> Button that takes you back to the main menu

        //makes play again buttons
        Rectangle playAgainYes = new Rectangle(ORIG_TEXT_X, ORIG_TEXT_Y + BUTTON_DIST_BELOW_ORIG_TEXT_Y, PLAY_AGAIN_BUTTON_WIDTH, PLAY_AGAIN_BUTTON_HEIGHT);
        Rectangle playAgainNo = new Rectangle(ORIG_TEXT_X + PLAY_AGAIN_BUTTON_WIDTH + 10, ORIG_TEXT_Y + BUTTON_DIST_BELOW_ORIG_TEXT_Y, PLAY_AGAIN_BUTTON_WIDTH, PLAY_AGAIN_BUTTON_HEIGHT);
        buttons[0] = playAgainYes;
        buttons[1] = playAgainNo;

        Rectangle upperLeftButton = new Rectangle(((CANVAS_WIDTH / 2) - MENU_BUTTON_WIDTH) - 50, UPPER_BUTTON_Y, MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT);
        Rectangle upperRightButton = new Rectangle((CANVAS_WIDTH / 2) + 50, UPPER_BUTTON_Y, MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT);
        buttons[2] = upperLeftButton;
        buttons[3] = upperRightButton;

        for (int i = 0; i < BOARD_SIZE; i++) {
            Rectangle difficultyButton = new Rectangle((CANVAS_WIDTH / 2) - (DIST_BETWEEN_DIFFICULTY_BUTTONS * 1.5) - (MENU_BUTTON_WIDTH * 2) + (i * (MENU_BUTTON_WIDTH + DIST_BETWEEN_DIFFICULTY_BUTTONS)), 260 + BUTTON_DIST_BELOW_ORIG_TEXT_Y, MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT );
            buttons[i + 4] = difficultyButton;
        }

        Rectangle mainMenuButton = new Rectangle(10, 10, 70, 20);
        buttons[8] = mainMenuButton;

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
    public static void showBoard(Stage theStage, Scene theScene, GraphicsContext gc, Board gameBoard, Player[] players,
                                 HashMap<String, Boolean> gameInfo, Player winningPlayer, String winner, String compressedGame, PrintStream output) {

        drawBoard(gc, gameBoard);

        theStage.show();

        if ((gameInfo.get("p1Turn") && players[0] instanceof ComputerPlayer) ||  (!gameInfo.get("p1Turn") && players[1] instanceof ComputerPlayer)) {
            //disable clicking
            theScene.setOnMouseClicked(
                    e -> {
                    }
            );

            int col = gameInfo.get("p1Turn") ? ((ComputerPlayer) players[0]).getPlay(gameBoard) : ((ComputerPlayer) players[1]).getPlay(gameBoard);
            makeMove(col, players, theStage, theScene, gc, gameBoard, gameInfo, winningPlayer, winner, compressedGame, output);

        } else {

            HashMap<String, Boolean> clickInfo = new HashMap<>();
            clickInfo.put("clicked", false);
            clickInfo.put("clickedOnColumn", false);
            clickInfo.put("clickedMainMenu", false);
            final int[] columnClickedOn = {-1};

            theScene.setOnMouseClicked(
                    e -> {
                        if (topPieces[0].contains(e.getX(), e.getY())) {
                            clickInfo.put("clicked", true);
                            clickInfo.put("clickedOnColumn", false);
                            clickInfo.put("clickedMainMenu", false);
//                            System.out.println("clicked the red piece");
                        } else if (topPieces[1].contains(e.getX(), e.getY())) {
                            clickInfo.put("clicked", true);
                            clickInfo.put("clickedOnColumn", false);
                            clickInfo.put("clickedMainMenu", false);
//                            System.out.println("clicked the blue piece");
                        } else if (columns[0].contains(e.getX(), e.getY())) {
                            clickInfo.put("clicked", true);
                            clickInfo.put("clickedOnColumn", true);
                            columnClickedOn[0] = 0;
                            clickInfo.put("clickedMainMenu", false);
//                            System.out.println("clicked column 0");
                        } else if (columns[1].contains(e.getX(), e.getY())) {
                            clickInfo.put("clicked", true);
                            clickInfo.put("clickedOnColumn", true);
                            columnClickedOn[0] = 1;
                            clickInfo.put("clickedMainMenu", false);
//                            System.out.println("clicked column 1");
                        } else if (columns[2].contains(e.getX(), e.getY())) {
                            clickInfo.put("clicked", true);
                            clickInfo.put("clickedOnColumn", true);
                            columnClickedOn[0] = 2;
                            clickInfo.put("clickedMainMenu", false);
//                            System.out.println("clicked column 2");
                        } else if (columns[3].contains(e.getX(), e.getY())) {
                            clickInfo.put("clicked", true);
                            clickInfo.put("clickedOnColumn", true);
                            columnClickedOn[0] = 3;
                            clickInfo.put("clickedMainMenu", false);
//                            System.out.println("clicked column 3");
                        } else if (buttons[8].contains(e.getX(), e.getY())) {
                            clickInfo.put("clicked", true);
                            clickInfo.put("clickedOnColumn", false);
                            clickInfo.put("clickedMainMenu", true);
//                            System.out.println("clicked main menu");
                        }

                        if (clickInfo.get("clicked")) {
                            if (clickInfo.get("clickedMainMenu")) {
                                pregameScreen(theStage, theScene, gc, new Player[3], output);
                            } else if (clickInfo.get("clickedOnColumn")) {
                                makeMove(columnClickedOn[0], players, theStage, theScene, gc, gameBoard, gameInfo, winningPlayer, winner, compressedGame, output);
                            } else {
                                showBoard(theStage, theScene, gc, gameBoard, players, gameInfo, winningPlayer, winner, compressedGame, output);
                            }
                        }
                    }
            );

//            System.out.println("clicked something clickable");
        }
        theStage.show();
    }

    private static void drawBoard(GraphicsContext gc, Board gameBoard) {
        gc.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);

        //makes the board (with black tiles)
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

        //Place blue piece and make clickable
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

        //make game pieces
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

        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(10, 10, 70, 20);
        gc.setFill(Color.BLACK);
        gc.fillText("Main Menu", 12, 24);
    }

    private static void makeMove(int column, Player[] players, Stage theStage, Scene theScene, GraphicsContext gc, Board gameBoard,
                                 HashMap<String, Boolean> gameInfo, Player winningPlayer, String winner, String compressedGame, PrintStream output) {

        Player curPlayer = players[1];
        if (gameInfo.get("p1Turn")) {
            curPlayer = players[0];
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

            if (!winner.equals("tie")) {
                winningPlayer.wonGame();
            } else {
//                System.out.println(winner + " wins!");
//                    checkGameOver(theStage, gc, players, gameInfo.get("reached10"), output);
//                playGame(theStage, theScene, gc, players, new Board(), output);

            }
            gameOverScreen(theStage, theScene, gc, players, gameBoard, winningPlayer, winner, output);


        } else {
            showBoard(theStage, theScene, gc, gameBoard, players, gameInfo, winningPlayer, winner, compressedGame, output);
        }
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

    private static void pregameScreen(Stage theStage, Scene theScene, GraphicsContext gc, Player[] players, PrintStream output) {
        gc.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);

        gc.setFill(Color.GRAY);
        gc.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);

        gc.setFill(Color.BLACK);
        gc.fillText("Welcome to Plus4", (CANVAS_WIDTH / 2) - 60, 30);
        gc.fillText("How many people want to play?", (CANVAS_WIDTH / 2) - 100, 60);

        gc.setFill(Color.WHITE);
        gc.fillRect((CANVAS_WIDTH / 2) - MENU_BUTTON_WIDTH - 50, UPPER_BUTTON_Y, MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT);
        gc.fillRect((CANVAS_WIDTH / 2) + 50, UPPER_BUTTON_Y, MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT);

        gc.setFill(Color.BLACK);
        gc.fillText("One", (CANVAS_WIDTH / 2) - MENU_BUTTON_WIDTH - 10, UPPER_BUTTON_Y + (MENU_BUTTON_HEIGHT / 2));
        gc.fillText("Two", (CANVAS_WIDTH / 2) + 85, UPPER_BUTTON_Y + (MENU_BUTTON_HEIGHT / 2));

        HashMap<String, Boolean> clickInfo = new HashMap<>();
        clickInfo.put("clicked", false);
        clickInfo.put("clickedOnOnePlayer", false);
        clickInfo.put("clickedOnTwoPlayer", false);

        theScene.setOnMouseClicked(
                e -> {
                    if (buttons[2].contains(e.getX(), e.getY())) {
                        clickInfo.put("clicked", true);
                        clickInfo.put("clickedOnOnePlayer", true);
                        clickInfo.put("clickedOnTwoPlayer", false);
//                        System.out.println("clicked one player");
                    } else if (buttons[3].contains(e.getX(), e.getY())) {
                        clickInfo.put("clicked", true);
                        clickInfo.put("clickedOnOnePlayer", false);
                        clickInfo.put("clickedOnTwoPlayer", true);
//                        System.out.println("clicked two player");
                    }

                    if (clickInfo.get("clicked")) {
                        if (clickInfo.get("clickedOnOnePlayer")) {
                            onePlayerScreen(theStage, theScene, gc, players, output);
                        } else if (clickInfo.get("clickedOnTwoPlayer")){
                            try {
                                players[0] = new HumanVisualPlayer("Player 1", 0);
                                players[1] = new HumanVisualPlayer("Player 2", 1);
                                players[2] = new Nobody();
                                Player.setPlayers(players);
                                playGame(theStage, theScene, gc, players, new Board(), output);
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                }
        );

        theStage.show();
    }

    private static void onePlayerScreen(Stage theStage, Scene theScene, GraphicsContext gc, Player[] players, PrintStream output) {
        gc.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);

        gc.setFill(Color.GRAY);
        gc.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);

        gc.setFill(Color.BLACK);
        gc.fillText("Welcome to Plus4", (CANVAS_WIDTH / 2) - 60, 30);
        gc.fillText("Would you like to go first or second?", (CANVAS_WIDTH / 2) - 120, 60);

        gc.setFill(Color.WHITE);
        gc.fillRect((CANVAS_WIDTH / 2) - MENU_BUTTON_WIDTH - 50, UPPER_BUTTON_Y, MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT);
        gc.fillRect((CANVAS_WIDTH / 2) + 50, UPPER_BUTTON_Y, MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT);

        gc.setFill(Color.BLACK);
        gc.fillText("First", (CANVAS_WIDTH / 2) - MENU_BUTTON_WIDTH - 13, UPPER_BUTTON_Y + (MENU_BUTTON_HEIGHT / 2));
        gc.fillText("Second", (CANVAS_WIDTH / 2) + 77, UPPER_BUTTON_Y + (MENU_BUTTON_HEIGHT / 2));

        HashMap<String, Boolean> clickInfo = new HashMap<>();
        clickInfo.put("clicked", false);
        clickInfo.put("clickedOnFirstPlay", false);
        clickInfo.put("clickedOnSecondPlay", false);

        theScene.setOnMouseClicked(
                e -> {
                    if (buttons[2].contains(e.getX(), e.getY())) {
                        clickInfo.put("clicked", true);
                        clickInfo.put("clickedOnFirstPlay", true);
                        clickInfo.put("clickedOnSecondPlay", false);
//                        System.out.println("clicked first play");
                    } else if (buttons[3].contains(e.getX(), e.getY())) {
                        clickInfo.put("clicked", true);
                        clickInfo.put("clickedOnFirstPlay", false);
                        clickInfo.put("clickedOnSecondPlay", true);
//                        System.out.println("clicked second play");
                    }

                    if (clickInfo.get("clicked")) {
                        boolean userGoesFirst = false;
                        if (clickInfo.get("clickedOnFirstPlay")) {
                            userGoesFirst = true;
                        } else if (clickInfo.get("clickedOnSecondPlay")){
                            userGoesFirst = false;
                        }
                        chooseDifficultyScreen(theStage, theScene, gc, players, userGoesFirst, output);
                    }
                }
        );

        theStage.show();
    }

    private static void chooseDifficultyScreen(Stage theStage, Scene theScene, GraphicsContext gc, Player[] players, boolean userGoesFirst, PrintStream output) {
        gc.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);

        gc.setFill(Color.GRAY);
        gc.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);

        gc.setFill(Color.BLACK);
        gc.fillText("Welcome to Plus4", (CANVAS_WIDTH / 2) - 60, 30);
        gc.fillText("Would you like to go first or second?", (CANVAS_WIDTH / 2) - 120, 60);

        //make turn options
        if (userGoesFirst) {
            gc.setFill(Color.YELLOW);
        } else {
            gc.setFill(Color.WHITE);
        }
        gc.fillRect((CANVAS_WIDTH / 2) - MENU_BUTTON_WIDTH - 50, UPPER_BUTTON_Y, MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT);

        if (userGoesFirst) {
            gc.setFill(Color.WHITE);
        } else {
            gc.setFill(Color.YELLOW);
        }
        gc.fillRect((CANVAS_WIDTH / 2) + 50, UPPER_BUTTON_Y, MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT);

        gc.setFill(Color.BLACK);
        gc.fillText("First", (CANVAS_WIDTH / 2) - MENU_BUTTON_WIDTH - 13, UPPER_BUTTON_Y + (MENU_BUTTON_HEIGHT / 2));
        gc.fillText("Second", (CANVAS_WIDTH / 2) + 77, UPPER_BUTTON_Y + (MENU_BUTTON_HEIGHT / 2));

        gc.fillText("What difficulty would you like to play against?", (CANVAS_WIDTH / 2) - 140, 260);

        //make difficulty buttons
        gc.setFill(Color.WHITE);
        for (int i = 0; i < BOARD_SIZE; i++) {
            gc.fillRect((CANVAS_WIDTH / 2) - (DIST_BETWEEN_DIFFICULTY_BUTTONS * 1.5) - (MENU_BUTTON_WIDTH * 2) + (i * (MENU_BUTTON_WIDTH + DIST_BETWEEN_DIFFICULTY_BUTTONS)), 260 + BUTTON_DIST_BELOW_ORIG_TEXT_Y, MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT);
        }

        gc.setFill(Color.BLACK);
        gc.fillText("Easy", 37 + (CANVAS_WIDTH / 2) - (DIST_BETWEEN_DIFFICULTY_BUTTONS * 1.5) - (MENU_BUTTON_WIDTH * 2) + (0 * (MENU_BUTTON_WIDTH + DIST_BETWEEN_DIFFICULTY_BUTTONS)), 260 + BUTTON_DIST_BELOW_ORIG_TEXT_Y + (MENU_BUTTON_HEIGHT / 2));
        gc.fillText("Medium", 28 + (CANVAS_WIDTH / 2) - (DIST_BETWEEN_DIFFICULTY_BUTTONS * 1.5) - (MENU_BUTTON_WIDTH * 2) + (1 * (MENU_BUTTON_WIDTH + DIST_BETWEEN_DIFFICULTY_BUTTONS)), 260 + BUTTON_DIST_BELOW_ORIG_TEXT_Y + (MENU_BUTTON_HEIGHT / 2));
        gc.fillText("Hard", 35 + (CANVAS_WIDTH / 2) - (DIST_BETWEEN_DIFFICULTY_BUTTONS * 1.5) - (MENU_BUTTON_WIDTH * 2) + (2 * (MENU_BUTTON_WIDTH + DIST_BETWEEN_DIFFICULTY_BUTTONS)), 260 + BUTTON_DIST_BELOW_ORIG_TEXT_Y + (MENU_BUTTON_HEIGHT / 2));
        gc.fillText("Intelligent", 22 + (CANVAS_WIDTH / 2) - (DIST_BETWEEN_DIFFICULTY_BUTTONS * 1.5) - (MENU_BUTTON_WIDTH * 2) + (3 * (MENU_BUTTON_WIDTH + DIST_BETWEEN_DIFFICULTY_BUTTONS)), 260 + BUTTON_DIST_BELOW_ORIG_TEXT_Y + (MENU_BUTTON_HEIGHT / 2));

        HashMap<String, Boolean> clickInfo = new HashMap<>();
        clickInfo.put("clicked", false);
        clickInfo.put("clickedOnFirstPlay", false);
        clickInfo.put("clickedOnSecondPlay", false);
        clickInfo.put("clickedOnEasy", false);
        clickInfo.put("clickedOnMedium", false);
        clickInfo.put("clickedOnHard", false);
        clickInfo.put("clickedOnIntelligent", false);
        clickInfo.put("clickedDifficulty", false);
        Difficulty[] difficulty = new Difficulty[1];

        theScene.setOnMouseClicked(
                e -> {
                    if (buttons[2].contains(e.getX(), e.getY())) {
                        clickInfo.put("clicked", true);
                        clickInfo.put("clickedOnFirstPlay", true);
                        clickInfo.put("clickedOnSecondPlay", false);
                        clickInfo.put("clickedOnEasy", false);
                        clickInfo.put("clickedOnMedium", false);
                        clickInfo.put("clickedOnHard", false);
                        clickInfo.put("clickedOnIntelligent", false);
                        clickInfo.put("clickedDifficulty", false);

//                        System.out.println("clicked first play");
                    } else if (buttons[3].contains(e.getX(), e.getY())) {
                        clickInfo.put("clicked", true);
                        clickInfo.put("clickedOnFirstPlay", false);
                        clickInfo.put("clickedOnSecondPlay", true);
                        clickInfo.put("clickedOnEasy", false);
                        clickInfo.put("clickedOnMedium", false);
                        clickInfo.put("clickedOnHard", false);
                        clickInfo.put("clickedOnIntelligent", false);
                        clickInfo.put("clickedDifficulty", false);

//                        System.out.println("clicked second play");
                    } else if (buttons[4].contains(e.getX(), e.getY())) {
                        clickInfo.put("clicked", true);
                        clickInfo.put("clickedOnFirstPlay", false);
                        clickInfo.put("clickedOnSecondPlay", false);
                        clickInfo.put("clickedOnEasy", true);
                        clickInfo.put("clickedOnMedium", false);
                        clickInfo.put("clickedOnHard", false);
                        clickInfo.put("clickedOnIntelligent", false);
                        clickInfo.put("clickedDifficulty", true);

//                        System.out.println("clicked easy");
                    } else if (buttons[5].contains(e.getX(), e.getY())) {
                        clickInfo.put("clicked", true);
                        clickInfo.put("clickedOnFirstPlay", false);
                        clickInfo.put("clickedOnSecondPlay", false);
                        clickInfo.put("clickedOnEasy", false);
                        clickInfo.put("clickedOnMedium", true);
                        clickInfo.put("clickedOnHard", false);
                        clickInfo.put("clickedOnIntelligent", false);
                        clickInfo.put("clickedDifficulty", true);

//                        System.out.println("clicked medium");
                    } else if (buttons[6].contains(e.getX(), e.getY())) {
                        clickInfo.put("clicked", true);
                        clickInfo.put("clickedOnFirstPlay", false);
                        clickInfo.put("clickedOnSecondPlay", false);
                        clickInfo.put("clickedOnEasy", false);
                        clickInfo.put("clickedOnMedium", false);
                        clickInfo.put("clickedOnHard", true);
                        clickInfo.put("clickedOnIntelligent", false);
                        clickInfo.put("clickedDifficulty", true);

//                        System.out.println("clicked hard");
                    } else if (buttons[7].contains(e.getX(), e.getY())) {
                        clickInfo.put("clicked", true);
                        clickInfo.put("clickedOnFirstPlay", false);
                        clickInfo.put("clickedOnSecondPlay", false);
                        clickInfo.put("clickedOnEasy", false);
                        clickInfo.put("clickedOnMedium", false);
                        clickInfo.put("clickedOnHard", false);
                        clickInfo.put("clickedOnIntelligent", true);
                        clickInfo.put("clickedDifficulty", true);

//                        System.out.println("clicked intelligent");
                    }


                    if (clickInfo.get("clicked")) {
                        if (clickInfo.get("clickedOnFirstPlay")) {
                            chooseDifficultyScreen(theStage, theScene, gc, players, true, output);
                        } else if (clickInfo.get("clickedOnSecondPlay")){
                            chooseDifficultyScreen(theStage, theScene, gc, players, false, output);
                        } else if (clickInfo.get("clickedOnEasy")) {
                            difficulty[0] = Difficulty.EASY;
                        } else if (clickInfo.get("clickedOnMedium")) {
                            difficulty[0] = Difficulty.MEDIUM;
                        } else if (clickInfo.get("clickedOnHard")) {
                            difficulty[0] = Difficulty.HARD;
                        } else if (clickInfo.get("clickedOnIntelligent")) {
                            difficulty[0] = Difficulty.INTELLIGENT;
                        }
                        if (clickInfo.get("clickedDifficulty")) {
                            try {
                                if (userGoesFirst) {
                                    players[0] = new HumanVisualPlayer("Player", 0);
                                    players[1] = getComputerPlayer(difficulty[0], 1);
                                } else {
                                    players[0] = getComputerPlayer(difficulty[0], 0);
                                    players[1] = new HumanVisualPlayer("Player", 1);
                                }

                                players[2] = new Nobody();
                                Player.setPlayers(players);

                                playGame(theStage, theScene, gc, players, new Board(), output);

                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                }
        );

        theStage.show();
    }

    private static void gameOverScreen(Stage theStage, Scene theScene, GraphicsContext gc, Player[] players,
                                       Board gameBoard, Player winningPlayer, String winner, PrintStream output) {
        drawBoard(gc, gameBoard);

        int curTextY = ORIG_TEXT_Y;

        gc.setFill(Color.BLACK);
        gc.fillText("Game Over", ORIG_TEXT_X, curTextY += ORIG_TEXT_STEP_SIZE);
        if (winner.equals("tie")) {
            gc.fillText("It's a tie! ", ORIG_TEXT_X, curTextY += ORIG_TEXT_STEP_SIZE);
        } else {
            gc.fillText(winningPlayer.getFullName() + " has won! ", ORIG_TEXT_X, curTextY += ORIG_TEXT_STEP_SIZE);
        }
        gc.fillText("Play again?", ORIG_TEXT_X, curTextY += ORIG_TEXT_STEP_SIZE);

        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(ORIG_TEXT_X, ORIG_TEXT_Y + BUTTON_DIST_BELOW_ORIG_TEXT_Y, PLAY_AGAIN_BUTTON_WIDTH, PLAY_AGAIN_BUTTON_HEIGHT);
        gc.fillRect(ORIG_TEXT_X + PLAY_AGAIN_BUTTON_WIDTH + DIST_BETWEEN_PLAY_AGAIN_BUTTONS, ORIG_TEXT_Y + BUTTON_DIST_BELOW_ORIG_TEXT_Y, PLAY_AGAIN_BUTTON_WIDTH, PLAY_AGAIN_BUTTON_HEIGHT);

        gc.setFill(Color.BLACK);
        gc.fillText("Yes", ORIG_TEXT_X + 13, ORIG_TEXT_Y + BUTTON_DIST_BELOW_ORIG_TEXT_Y + (PLAY_AGAIN_BUTTON_HEIGHT / 2) + 5);
        gc.fillText("No", ORIG_TEXT_X + PLAY_AGAIN_BUTTON_WIDTH + DIST_BETWEEN_PLAY_AGAIN_BUTTONS + 15, ORIG_TEXT_Y + BUTTON_DIST_BELOW_ORIG_TEXT_Y + (PLAY_AGAIN_BUTTON_HEIGHT / 2) + 5);


        HashMap<String, Boolean> clickInfo = new HashMap<>();
        clickInfo.put("clicked", false);
        clickInfo.put("clickedOnPlayAgain", false);
        clickInfo.put("clickedOnNotPlayAgain", false);
        clickInfo.put("clickedMainMenu", false);

        theScene.setOnMouseClicked(
                e -> {
                    if (buttons[0].contains(e.getX(), e.getY())) {
                        clickInfo.put("clicked", true);
                        clickInfo.put("clickedOnPlayAgain", true);
                        clickInfo.put("clickedOnNotPlayAgain", false);
                        clickInfo.put("clickedMainMenu", false);
//                        System.out.println("clicked play again");
                    } else if (buttons[1].contains(e.getX(), e.getY())) {
                        clickInfo.put("clicked", true);
                        clickInfo.put("clickedOnPlayAgain", false);
                        clickInfo.put("clickedOnNotPlayAgain", true);
                        clickInfo.put("clickedMainMenu", false);
//                        System.out.println("clicked not play again");
                    } else if (buttons[8].contains(e.getX(), e.getY())) {
                        clickInfo.put("clicked", true);
                        clickInfo.put("clickedOnPlayAgain", false);
                        clickInfo.put("clickedOnNotPlayAgain", false);
                        clickInfo.put("clickedMainMenu", true);
                    }

                    if (clickInfo.get("clicked")) {
                        if (clickInfo.get("clickedOnPlayAgain")) {
                            try {
                                playGame(theStage, theScene, gc, players, new Board(), output);
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                        } else if (clickInfo.get("clickedOnNotPlayAgain")){
                            System.exit(0);
                        } else if (clickInfo.get("clickedMainMenu")) {
                            pregameScreen(theStage, theScene, gc, players, output);
                        }
                    }
                }
        );

//        System.out.println("clicked something clickable on game over screen");

        theStage.show();

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

    public static Player getComputerPlayer(Difficulty difficulty, int playerNumber) {
        if (difficulty.equals(Difficulty.EASY)) {
            return new EasyComputer(playerNumber);
        } else if (difficulty.equals(Difficulty.MEDIUM)) {
            return new MediumComputer(playerNumber);
        } else if (difficulty.equals(Difficulty.HARD)) {
            return new HardComputer(playerNumber);
        } else if (difficulty.equals(Difficulty.INTELLIGENT)) {
            return new IntelligentComputer(playerNumber);
        } else {
            throw new Error("invalid difficulty");
        }
    }
}
