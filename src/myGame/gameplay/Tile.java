package myGame.gameplay;

import myGame.player.Nobody;
import myGame.player.Player;

/**
 * Created by liamkreiss on 10/7/17.
 */
public class Tile {
    private boolean empty;
    private Player playerWithControl;
    private String contents;


    public Tile() {
        this.empty = true;
        this.contents = " ";
        this.playerWithControl = new Nobody(true);
    }

    public Tile(Tile oldTile) {
        this.empty = oldTile.empty;
        this.contents = oldTile.contents;
        this.playerWithControl = oldTile.playerWithControl;
    }

    public void setContents(Player player) {
        this.empty = false;
        this.playerWithControl = player;
        this.contents = player.getGameName();
    }

    public String getContents() {
        return this.contents;
    }

    public Player getPlayerWithControl() {
        return this.playerWithControl;
    }

    public boolean isEmpty() {
        return this.empty;
    }
}
