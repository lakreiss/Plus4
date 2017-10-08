package biancasGame.Plus4.src;

import java.awt.Color;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class ServerModel {

    private ConcurrentHashMap<Integer, ClientModel> currentGames = new ConcurrentHashMap<Integer, ClientModel>();
    private ConcurrentHashMap<Integer, PlayerProfile> playerProfiles = new ConcurrentHashMap<Integer, PlayerProfile>();
    private LinkedBlockingQueue<Integer> waitingPlayers = new LinkedBlockingQueue<Integer>(50);

    public ServerModel() {
    }

    public void addPlayer(int playerID) throws InterruptedException {
        if (!playerProfiles.containsKey(playerID)) {
            playerProfiles.put(playerID, new PlayerProfile(playerID));
        }
        currentGames.remove(playerID);
        try {
            waitingPlayers.put(playerID);
        } catch (InterruptedException e) {
        }
    }

    public ClientModel startGame(int firstPlayerID, int secondPlayerID) {
        if (!playerProfiles.containsKey(firstPlayerID)) {
            playerProfiles.put(firstPlayerID, new PlayerProfile(firstPlayerID));
        }
        if (!playerProfiles.containsKey(secondPlayerID)) {
            playerProfiles.put(secondPlayerID, new PlayerProfile(secondPlayerID));
        }
        ClientModel game = new ClientModel(firstPlayerID, secondPlayerID);
        currentGames.put(firstPlayerID, game);
        currentGames.put(secondPlayerID, game);
        game.startGame();
        return game;
    }

    public boolean makeMove(int col, int playerID) {
        ClientModel game = currentGames.get(playerID);
        if (game == null || !isPlayersTurn(playerID)) {
            return false;
        }
        return game.makeMove(col);
    }

    public boolean isWinner(int playerID) {
        ClientModel game = currentGames.get(playerID);
        if (game == null) {
            return false;
        }
        return game.thereIsAWinner();
    }

    public boolean isTie(int playerID) {
        ClientModel game = currentGames.get(playerID);
        if (game == null) {
            return false;
        }
        return game.isTie();
    }

    public boolean isPlayersTurn(int playerID) {
        ClientModel model = currentGames.get(playerID);
        if (model == null) {
            return false;
        }
        return (playerID == model.getCurrentPlayerID());
    }

    public int getNextPlayer() throws InterruptedException {
        return waitingPlayers.take();
    }

    public String getPlayerName(int playerID) {
        return playerProfiles.get(playerID).getUserName();
    }
    
    public void setPlayerName(int playerID, String name) {
        if (!playerProfiles.containsKey(playerID)) {
            playerProfiles.put(playerID, new PlayerProfile(playerID));
        }
        playerProfiles.get(playerID).setUserName(name);

    }

    public int getOpponent(int playerID) {
        ClientModel game = currentGames.get(playerID);
        if (game != null) {
            return game.getOtherPlayerID(playerID);
        }
        return -1;
    }

    public ClientModel getGame(int playerID) {
        return currentGames.get(playerID);
    }

    public void remove(int playerID) {
        ClientModel game = currentGames.get(playerID);
        if (game != null) {
            int opponent = game.getOtherPlayerID(playerID);
            currentGames.remove(playerID);
            currentGames.remove(opponent);
            waitingPlayers.add(opponent);
        }
        playerProfiles.remove(playerID);
        waitingPlayers.remove(playerID);
    }

    public class PlayerProfile {

        private int ID;
        private String userName;
        private String opponent;
        private Color color;

        public PlayerProfile(int ID) {
            this.ID = ID;
        }

        public PlayerProfile(int ID, String userName) {
            this.ID = ID;
            this.userName = userName;
        }

        public int getID() {
            return ID;
        }

        public String getUserName() {
            return userName;
        }

        public Color getColor() {
            return color;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }
}
