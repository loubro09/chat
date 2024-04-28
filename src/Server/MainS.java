package Server;

import Entity.Message;

public class MainS {
    public static void main (String[] args) {
        ServerNetworkBoundary snb = new ServerNetworkBoundary(1234);
        UserController uc = new UserController(snb);
        MessageController mc = new MessageController(uc);
    }
}
