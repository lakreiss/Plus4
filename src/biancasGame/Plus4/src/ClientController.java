package biancasGame.Plus4.src;//package biancasGame.Plus4.src;
//
//import java.awt.Color;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.MouseEvent;
//import java.awt.event.MouseListener;
//import java.awt.event.WindowEvent;
//import java.awt.event.WindowListener;
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import javax.swing.JOptionPane;
//import javax.swing.SwingUtilities;
//
//public class ClientController implements MouseListener, ActionListener,
//        WindowListener {

public class ClientController {

    public ClientController(ClientView view, String host) {

    }

//    private final static int PORT = 6789;
//    private ClientConnection connection;
//    private ClientView view;
//    private ClientModel game;
//    private int myID;
//    private boolean boardPanel, clickedStart, opponentClickedStart;
//
//    public ClientController(ClientView view, final String hostName) throws IOException {
//        this.view = view;
//        this.view.addMouseListener(this);
//        this.view.addActionListener(this);
//        this.connection = new ClientConnection(hostName, PORT);
//        this.myID = connection.getID();
//    }
//
//    private void updateGame(ClientModel game) {
//        this.game = game;
//        view.setGame(game);
//        if (game == null || game.getTokens() == null) {
//            return;
//        } else if (game.inProgress()) {
//            view.setTurn(myID == game.getCurrentPlayerID());
//        } else {
//            view.setWinOrLose(game.isWinner(game.getColor(myID)), game.isTie());
//            view.showPanel("gameOverPanel");
//        }
//        view.repaint();
//    }
//
//    public void mousePressed(MouseEvent ev) {
//        if (!boardPanel || game == null || game.getTokens() == null || myID != game.getCurrentPlayerID()) {
//            return;
//        }
//
//        if (view.isTopRow(ev.getY())) {
//            connection.send(view.getCol(ev.getX()));
//        }
//
//    }
//
//    public void actionPerformed(ActionEvent ev) {
//        String command = ev.getActionCommand();
//        if (command.equals("Start Game")) {
//            if (game == null) {
//                showWaitingMessage();
//            } else {
//                clickedStart = true;
//                if (opponentClickedStart) {
//                    boardPanel = true;
//                    view.showPanel("gamePanel");
//                } else {
//                    view.showPanel("loadingPanel");
//                }
//                connection.send("clickedStart");
//            }
//        } else if (command.equals("Resign")) {
//            boardPanel = false;
//            view.showPanel("gameOverPanel");
//            view.setResign(true);
//            connection.send("resign");
//        } else if (command.equals("Help")) {
//            boardPanel = false;
//            view.showPanel("helpPanel");
//        } else if (command.equals("Return")) {
//            boardPanel = true;
//            view.showPanel("gamePanel");
//        } else if (command.equals("Play Again")) {
//            boardPanel = false;
//            opponentClickedStart = false;
//            clickedStart = false;
//            view.setOpponentUsername(null);
//            view.showPanel("startPanel");
//            updateGame(null);
//            connection.send("newgame");
//        }
//    }
//
//    public void showWaitingMessage() {
//        JOptionPane.showMessageDialog(view, "Be patient!", "Waiting for an opponent.", JOptionPane.OK_OPTION);
//    }
//
//    public void windowClosing(WindowEvent we) {
//        view.dispose();
//        connection.disconnect();
//        try {
//            Thread.sleep(500);
//        } catch (InterruptedException e) {
//        }
//        System.exit(0);
//    }

//    public class ClientConnection extends netgame.common.Client {
//
//        public ClientConnection(String host, int port) throws IOException {
//            super(host, port);
//        }
//
//        @Override
//        protected void messageReceived(final Object message) {
//
//            if (message instanceof ClientModel) {
//                SwingUtilities.invokeLater(new Runnable() {
//
//                    public void run() {
//                        updateGame((ClientModel) message);
//                        view.repaint();
//                    }
//                });
//            } else if (message instanceof Boolean) {
//                view.setTurn((Boolean) message);
//                view.repaint();
//            } else if (message instanceof Color) {
//                view.setPlayerColor((Color) message);
//            } else if (message instanceof String) {
//                String messageString = (String) message;
//                if (messageString.equals("waiting")) {
//                    view.showPanel("startGame");
//                } else if (messageString.equals("opponentClickedStart")) {
//                    if (clickedStart) {
//                        boardPanel = true;
//                        view.showPanel("gamePanel");
//                    } else {
//                        opponentClickedStart = true;
//                    }
//                } else if (messageString.equals("opponentResigned")) {
//                    boardPanel = false;
//                    view.setResign(false);
//                    view.showPanel("gameOverPanel");
//                    view.repaint();
//                } else {
//                    view.setOpponentUsername(messageString);
//                    view.repaint();
//                }
//            }
//        }
//
//        protected void extraHandshake(ObjectInputStream in, ObjectOutputStream out) throws IOException {
//            String userName = JOptionPane.showInputDialog("Pick a username.");
//            try {
//                out.writeObject(userName);
//                Boolean userNameAvailable = (Boolean) in.readObject();
//                while (!userNameAvailable) {
//                    String name = JOptionPane.showInputDialog("Username is not available. Choose another one.").trim();
//                    if (name == null) {
//                        windowClosing(null);
//                    }
//                    while (name.equals("")) {
//                        name = JOptionPane.showInputDialog("Username can't be blank. Try again.");
//                    }
//                    out.writeObject(name);
//                    userNameAvailable = (Boolean) in.readObject();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            }
//
//        }
//
//        protected void serverShutDown() {
//            SwingUtilities.invokeLater(new Runnable() {
//
//                public void run() {
//                    JOptionPane.showMessageDialog(view, "The server has disconnected.\n" + "The game has ended.");
//                    System.exit(0);
//                }
//            });
//        }
//    }
//
//    public void mouseClicked(MouseEvent ev) {
//    }
//
//    public void mouseReleased(MouseEvent me) {
//    }
//
//    public void mouseEntered(MouseEvent me) {
//    }
//
//    public void mouseExited(MouseEvent me) {
//    }
//
//    public void windowOpened(WindowEvent we) {
//    }
//
//    public void windowClosed(WindowEvent we) {
//    }
//
//    public void windowIconified(WindowEvent we) {
//    }
//
//    public void windowDeiconified(WindowEvent we) {
//    }
//
//    public void windowActivated(WindowEvent we) {
//    }
//
//    public void windowDeactivated(WindowEvent we) {
//    }
}
