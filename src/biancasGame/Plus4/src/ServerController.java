package biancasGame.Plus4.src;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;

import biancasGame.Plus4.src.netgame.common.Hub;

public class ServerController extends Hub {

    private static final int PORT = 6789;
    private ServerModel model;
    private boolean running;
    private HashSet<String> userNames = new HashSet<String>();

    public ServerController() throws IOException {
        super(PORT);
        model = new ServerModel();
        setAutoreset(true);
        new Thread() {

            public void start() {
                running = true;
                while (running) {
                    try {
                        int firstPlayer = model.getNextPlayer();
                        int secondPlayer = model.getNextPlayer();
                        ClientModel game = model.startGame(firstPlayer, secondPlayer);
                        ServerController.this.sendToOne(firstPlayer, game);
                        ServerController.this.sendToOne(secondPlayer, game);
                        ServerController.this.sendToOne(firstPlayer, game.getColor(firstPlayer));
                        ServerController.this.sendToOne(secondPlayer, game.getColor(secondPlayer));
                        System.out.println(model.getPlayerName(firstPlayer) + " vs " + model.getPlayerName(secondPlayer));
                        ServerController.this.sendToOne(firstPlayer, model.getPlayerName(secondPlayer));
                        ServerController.this.sendToOne(secondPlayer, model.getPlayerName(firstPlayer));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    protected void messageReceived(int playerID, Object message) {
        if (message instanceof Integer) {
            int col = (Integer) message;
            if (model.makeMove(col, playerID)) {
                ClientModel game = model.getGame(playerID);
                int opponentID = model.getOpponent(playerID);
                this.sendToOne(playerID, game);
                this.sendToOne(opponentID, game);
                this.sendToOne(playerID, model.isPlayersTurn(playerID));
                this.sendToOne(opponentID, model.isPlayersTurn(opponentID));
            }
        } else if (message instanceof String) {
            String command = (String) message;
            if (command.equals("newgame")) {
                this.sendToOne(playerID, "waiting");
                try {
                    model.addPlayer(playerID);
                } catch (InterruptedException ex) {
                }
            } else if (command.equals("clickedStart")) {
                int opponentID = model.getOpponent(playerID);
                this.sendToOne(opponentID, "opponentClickedStart");
            } else if (command.equals("resign")) {
                int opponentID = model.getOpponent(playerID);
                this.sendToOne(opponentID, "opponentResigned");
            }
        }
    }

    protected void playerConnected(int playerID) {
    }

    protected void playerDisconnected(int playerID) {
        int opponentID = model.getOpponent(playerID);
        userNames.remove(model.getPlayerName(playerID));
        model.remove(playerID);
        this.sendToOne(opponentID, "opponentResigned");
    }

    protected void extraHandshake(int playerID, ObjectInputStream in, ObjectOutputStream out) throws IOException {
        String name;
        try {
            name = (String) in.readObject();
            while (userNames.contains(name)) {
                out.writeObject(false);
                name = (String) in.readObject();
            }
            userNames.add(name);
            out.writeObject(true);
            model.addPlayer(playerID);
            model.setPlayerName(playerID, name);
        } catch (ClassNotFoundException e) {
            return;
        } catch (InterruptedException e) {
            return;
        }
    }
}
