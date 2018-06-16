package myGame.player;

import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import myGame.gameClients.VisualGameClient;
import myGame.gameplay.Board;

/**
 * Created by liamkreiss on 6/15/18.
 */
public class HumanVisualPlayer extends HumanPlayer {

    public HumanVisualPlayer(String name) {
        super(name);
    }

//    public int getPlay(Stage theStage, Scene theScene, GraphicsContext gc, Board gameBoard) {
//        int col = VisualGameClient.showBoard(theStage, theScene, gc, gameBoard);
//        if (col < 0 || col > 3) {
//            throw new Error("invalid column");
//        } else {
//            return col;
//        }
//
//    }
}
