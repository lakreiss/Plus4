package myGame.gameplay;

import myGame.player.Player;

/**
 * Created by liamkreiss on 10/7/17.
 */
public class Board {

    private int STANDARD_SIZE = 4;
    private int STANDARD_WIDTH = STANDARD_SIZE, STANDARD_HEIGHT = STANDARD_SIZE; //gives flexibility, although non-square boards would mess up daigonal win checker


    private int width, height;
    private Tile[][] tiles;

    String moves;

    //deciding whether to have non-square boards
//    public Board(int w, int h) {
//        this.width = w;
//        this.height = h;
//        this.tiles = new Tile[height][width];
//        buildTiles(tiles);
//    }

    public Board(int n) {
        this.moves = "";
        this.width = n;
        this.height = n;
        this.tiles = new Tile[height][width];
        buildEmptyTiles(tiles);
    }

    public Board() {
        this.moves = "";
        this.width = STANDARD_WIDTH;
        this.height = STANDARD_HEIGHT;
        this.tiles = new Tile[height][width];
        buildEmptyTiles(tiles);
    }

    public Board(Board oldBoard) {
        this.moves = oldBoard.getMoves();
        this.width = oldBoard.width;
        this.height = oldBoard.height;
        this.tiles = new Tile[height][width];
        duplicateTiles(tiles, oldBoard.tiles);

    }

    private void duplicateTiles(Tile[][] theseTiles, Tile[][] oldTiles) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                theseTiles[i][j] = new Tile(oldTiles[i][j]);
            }
        }
    }

    private void buildEmptyTiles(Tile[][] tiles) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                tiles[i][j] = new Tile();
            }
        }
    }

    /**
     * j = column
     * @param player
     * @param j = column
     */
    public int addPiece(Player player, int j) {
        if (moves.equals("")) {
            this.moves = "" + j;
        } else {
            this.moves += " " + j;
        }
        int i = 0;
        if (i < height && !tiles[i][j].isEmpty()) {
            addPiece(tiles[i][j].getPlayerWithControl(), i + 1, j);
        }
        tiles[i][j].setContents(player);
        return j;
    }

    private void addPiece(Player player, int i, int j) {
        if (i == height) {
            //do nothing
        } else if (tiles[i][j].isEmpty()) {
            tiles[i][j].setContents(player);
        } else {
            addPiece(tiles[i][j].getPlayerWithControl(), i + 1, j);
            tiles[i][j].setContents(player);
        }
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public String getMoves() {
        return this.moves;
    }

    public Player getWinningPlayer(Player[] players) {
        int[] winsH = checkHorizontal();
        int[] winsV = checkVertical();
        int[] winsD = checkDiagonals();

        int[] totalWins = getTotalWins(winsH, winsD, winsV);
        if (totalWins[0] == 0 && totalWins[1] == 0) {
            return players[2];
        } else {
            return getWinner(players, totalWins);
        }
    }

    private Player getWinner(Player[] players, int[] totalWins) {
        int p1Score = totalWins[0];
        int p2Score = totalWins[1];
        if (p1Score > p2Score) {
            return players[0];
        } else if (p2Score > p1Score) {
            return players[1];
        } else {
            return players[2];
        }
    }

    private int[] checkDiagonals() {
        int[] winsD = new int[2];
        Player upDiagFirstPiece = null;
        boolean upDiagWin = true;
        Player downDiagFirstPiece = null;
        boolean downDiagWin = true;
        for (int i = 0; i < getWidth(); i++) {
            if (tiles[height - 1 - i][i].isEmpty()) {
                upDiagWin = false;
                i = getWidth();
            } else {
                if (i == 0) {
                    upDiagFirstPiece = tiles[height - 1][i].getPlayerWithControl();
                } else {
                    if (!upDiagFirstPiece.equals(tiles[height - 1 - i][i].getPlayerWithControl())) {
                        upDiagWin = false;
                        i = getWidth();
                    }
                }
            }
        }

        for (int i = 0; i < getWidth(); i++) {
            if (tiles[i][i].isEmpty()) {
                downDiagWin = false;
                i = getWidth();
            } else {
                if (i == 0) {
                    downDiagFirstPiece = tiles[i][i].getPlayerWithControl();
                } else {
                    if (!downDiagFirstPiece.equals(tiles[i][i].getPlayerWithControl())) {
                        downDiagWin = false;
                        i = getWidth();
                    }
                }
            }
        }

        if (upDiagWin) {
            winsD[upDiagFirstPiece.getPlayerNumber()] += 1;
        }

        if (downDiagWin) {
            winsD[downDiagFirstPiece.getPlayerNumber()] += 1;
        }

        return winsD;
    }

    private int[] checkVertical() {
        int[] winsV = new int[2];
        for (int col = 0; col < width; col++) {
            Player firstPiece = null;
            boolean downWin = true;
            for (int row = 0; row < height; row++) {
                if (tiles[row][col].isEmpty()) {
                    downWin = false;
                    row = height;
                } else {
                    if (row == 0) {
                        firstPiece = tiles[row][col].getPlayerWithControl();
                    } else {
                        if (!firstPiece.equals(tiles[row][col].getPlayerWithControl())) {
                            downWin = false;
                            row = height;
                        }
                    }
                }
            }
            if (downWin) {
                winsV[firstPiece.getPlayerNumber()] += 1;
            }
        }
        return winsV;
    }

    private int[] checkHorizontal() {
        int[] winsH = new int[2];
        for (int row = 0; row < height; row++) {
            Player firstPiece = null;
            boolean acrossWin = true;
            for (int col = 0; col < width; col++) {
                if (tiles[row][col].isEmpty()) {
                    acrossWin = false;
                    col = width;
                } else {
                    if (col == 0) {
                        firstPiece = tiles[row][col].getPlayerWithControl();
                    } else {
                        if (!firstPiece.equals(tiles[row][col].getPlayerWithControl())) {
                            acrossWin = false;
                            col = width;
                        }
                    }
                }
            }
            if (acrossWin) {
                winsH[firstPiece.getPlayerNumber()] += 1;
            }
        }
        return winsH;
    }

    private static int[] getTotalWins(int[] winsH, int[] winsD, int[] winsV) {
        int[] totalWins = new int[2];
        for (int i = 0; i < 2; i++) {
            totalWins[i] = winsH[i] + winsD[i] + winsV[i];
        }
        return totalWins;
    }

    public int pieceAt(int row, int col) {
        String tileContent = tiles[row][col].getContents();
        if (tileContent.equals("X")) {
            return 1;
        } else if (tileContent.equals("O")) {
            return 2;
        } else {
            return 0;
        }
    }

    public String toString() {
        String fullBoard = "\n";
        for (int i = 0; i < width; i++) {
            if (i == 0) {
                fullBoard += "|" + i + "|";
            } else {
                fullBoard += i + "|";
            }
        }

        fullBoard += "\n";

        for (int i = 0; i < 2 * width + 1; i++) {
            fullBoard += "_";
        }

        fullBoard += "\n";

        for (int i = 0 ; i < height; i++) {
            String line = "";
            for (int j = 0; j < width; j++) {
                if (j == 0) {
                    line += "|" + tiles[i][j].getContents() + "|";
                } else {
                    line += tiles[i][j].getContents() + "|";
                }
            }
            fullBoard += line + "\n";
        }
        return fullBoard;
    }
}
