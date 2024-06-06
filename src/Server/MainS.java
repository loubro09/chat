package Server;

/**
 * Starts the Server program.
 */
public class MainS {
    public static void main (String[] args) {
        ServerNetworkBoundary snb = new ServerNetworkBoundary(1234);
        UserController uc = new UserController(snb);
        new ServerMessageController(uc);
    }
}
