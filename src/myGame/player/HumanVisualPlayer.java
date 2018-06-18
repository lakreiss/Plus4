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

    public HumanVisualPlayer(String name, int playerNumber) {
        super(name, playerNumber);
    }
}
