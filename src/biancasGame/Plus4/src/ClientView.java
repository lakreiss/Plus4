package biancasGame.Plus4.src;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class ClientView extends JFrame {

    private ClientModel game;
    private JPanel mainPanel, gamePanel, startPanel, loadingPanel, helpPanel, gameOverPanel, controlPanel, gameOverControlPanel;
    private Image welcome, loading, instructions;
    private Board board;
    private CardLayout cardLayout;
    private JButton startGame, endGame, help, returnButton, playAgain;
    private JLabel opponentMessage, turnMessage, gameOverMessage;
    private String opponent;

    public ClientView() {
        super("Plus 4");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setFocusable(true);
        setSize(450, 580);
        setLocation(400, 50);
        setResizable(false);

        mainPanel = new JPanel();
        cardLayout = new CardLayout();
        mainPanel.setLayout(cardLayout);
        setContentPane(mainPanel);

        //start panel
        startPanel = new JPanel(new BorderLayout());
        startPanel.setBackground(Color.white);
        mainPanel.add(startPanel, "startPanel");
        //welcome
        welcome = new Image("start.jpg");
        startPanel.add(welcome, BorderLayout.CENTER);
        //start button
        startGame = new JButton("Start Game");
        startPanel.add(startGame, BorderLayout.SOUTH);

        //help panel
        helpPanel = new JPanel(new BorderLayout());
        helpPanel.setBackground(Color.white);
        mainPanel.add(helpPanel, "helpPanel");
        //instructions
        instructions = new Image("instructions.jpg");
        helpPanel.add(instructions, BorderLayout.CENTER);
        //return button
        returnButton = new JButton("Return");
        helpPanel.add(returnButton, BorderLayout.SOUTH);

        //loading panel
        loadingPanel = new JPanel(new BorderLayout());
        loadingPanel.setBackground(Color.white);
        mainPanel.add(loadingPanel, "loadingPanel");
        //loading
        loading = new Image("loading.jpg");
        loadingPanel.add(loading, BorderLayout.CENTER);
        
        //game over panel
        gameOverPanel = new JPanel(new BorderLayout());
        gameOverPanel.setBackground(Color.white);
        mainPanel.add(gameOverPanel, "gameOverPanel");
        //game over
        Board gameOverBoard = new Board();
        gameOverPanel.add(gameOverBoard, BorderLayout.CENTER);
        //new game
        gameOverControlPanel = new JPanel(new GridLayout(2, 0));
        gameOverPanel.add(gameOverControlPanel, BorderLayout.SOUTH);
        playAgain = new JButton("Play Again");
        gameOverControlPanel.add(playAgain);
        gameOverMessage = new JLabel();
        gameOverMessage.setFont(new Font("Arial", Font.PLAIN, 14));
        gameOverControlPanel.add(gameOverMessage);

        //game panel
        gamePanel = new JPanel(new BorderLayout());
        gamePanel.setBackground(Color.white);
        mainPanel.add(gamePanel, "gamePanel");
        //game board
        board = new Board();
        gamePanel.add(board, BorderLayout.CENTER);
        //game bottom panel
        controlPanel = new JPanel(new GridLayout(2, 2));
        controlPanel.setBackground(Color.white);
        endGame = new JButton("Resign");
        controlPanel.add(endGame);
        help = new JButton("Help");
        controlPanel.add(help);
        opponentMessage = new JLabel();
        opponentMessage.setFont(new Font("Arial", Font.PLAIN, 14));
        controlPanel.add(opponentMessage);
        turnMessage = new JLabel();
        turnMessage.setFont(new Font("Arial", Font.PLAIN, 14));
        controlPanel.add(turnMessage);
        gamePanel.add(controlPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    public void addActionListener(ActionListener listener) {
        startGame.addActionListener(listener);
        endGame.addActionListener(listener);
        help.addActionListener(listener);
        returnButton.addActionListener(listener);
        playAgain.addActionListener(listener);
    }

    public void showPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);
    }

    public Board getBoard() {
        return board;
    }

    public boolean isTopRow(int y) {
        int rowWidth = (board.getWidth() - board.getWidth() / 9) / 4;
        return (y < rowWidth);
    }

    public int getCol(int x) {
        int colWidth = (board.getWidth() - board.getWidth() / 9) / 4;
        if (x < colWidth + board.getWidth() / 18) {
            return 0;
        } else if (x < 2 * colWidth + board.getWidth() / 18) {
            return 1;
        } else if (x < 3 * colWidth + board.getWidth() / 18) {
            return 2;
        } else {
            return 3;
        }
    }

    public void setGame(ClientModel game) {
        this.game = game;
    }

    public void setOpponentUsername(String opponent) {
        this.opponent = opponent;
        opponentMessage.setText(" You are playing against " + opponent);
    }

    public void setTurn(boolean myTurn) {
        if (myTurn) {
            turnMessage.setText("It is your turn");
        } else if (opponent != null) {
            turnMessage.setText("It is " + opponent + "'s turn");
        } else {
            turnMessage.setText("It is not your turn");
        }
    }

    public void setPlayerColor(Color color) {
        if (color.equals(Color.red)) {
            controlPanel.setBackground(new Color(255, 77, 77));
            gameOverControlPanel.setBackground(new Color(255, 77, 77));
        } else {
            controlPanel.setBackground(new Color(77, 77, 255));
            gameOverControlPanel.setBackground(new Color(77, 77, 255));
        }
    }

    public void setWinOrLose(boolean win, boolean tie) {
        if (tie) {
            gameOverMessage.setText("                                   GAME OVER   You tied!");
        } else if (win) {
            gameOverMessage.setText("                                   GAME OVER   You won!");
        } else {
            gameOverMessage.setText("                                   GAME OVER   You lost!");
        }
    }

    public void setResign(boolean resign) {
        if (resign) {
            gameOverMessage.setText("                         GAME OVER   You resigned!");
        } else {
            gameOverMessage.setText("                         GAME OVER   " + opponent + " resigned!");
        }
    }
    public class Board extends JPanel {

        @Override
        public void paintComponent(Graphics g) {
            Grid grid = new Grid(0, 0, 400);
            Tokens blankTokens = new Tokens(0, 0, 400);
            grid.draw(g);
            blankTokens.draw(g);
            if (game != null) {
                Tokens tokens = game.getTokens();
                LeftOverPile pile = game.getPile();
                tokens.draw(g);
                pile.draw(g);
            }
        }
    }

    public class Image extends JPanel {

        private String imageName;

        public Image(String imageName) {
            this.imageName = imageName;
        }
        
        @Override
        public void paintComponent(Graphics g) {
            try {
                ClassLoader cl = Image.class.getClassLoader();
                BufferedImage image;
                image = ImageIO.read(cl.getResource(imageName));
                g.drawImage(image, 0, 0, null);
            } catch (IOException e) {
            }
        }
    }
    
}
