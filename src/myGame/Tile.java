package myGame;

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
        this.playerWithControl = new Nobody();
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
