package biancasGame.Plus4.src;

public class ServerMain {

    public static void main(String[] args) {
        ServerController serverController;
        try {
                serverController = new ServerController();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return;
        }
    }
}
