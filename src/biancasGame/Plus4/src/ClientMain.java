package biancasGame.Plus4.src;

import java.io.IOException;

import javax.swing.JOptionPane;

public class ClientMain {

    public static void main(String[] args) {
        String host = JOptionPane.showInputDialog("Enter the host name of the\ncomputer that hosts Plus 4:", "localhost");
        if (host == null || host.trim().length() == 0) {
            return;
        }
        
        ClientView view = new ClientView();
//        try {
            ClientController controller = new ClientController(view, host);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
