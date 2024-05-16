package Server;

import Entity.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * The ServerNetworkBoundary class handles the connections between the server and the clients.
 */
public class ServerNetworkBoundary {
    private ServerSocket serverSocket;
    private PropertyChangeSupport propertyChangeSupport;
    private List<ClientHandler> clientsList;
    private UnreceivedMessages unreceivedMessages;
    private ActivityController activityController;

    /**
     * Constructor of the ServerNetworkBoundary class.
     * @param port the port number that server listens to.
     */
    public ServerNetworkBoundary(int port) {
        this.propertyChangeSupport = new PropertyChangeSupport(this);
        this.unreceivedMessages = new UnreceivedMessages();
        activityController = new ActivityController();
        clientsList = new ArrayList<>();
        try {
            this.serverSocket = new ServerSocket(port); //creates a server socket for clients to connect to
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Connection().start();
    }

    /**
     * Returns an instance of the UnsentMessages class.
     * @return the instance of the UnsentMessages class.
     */
    public UnreceivedMessages getUnreceivedMessage() {
        return unreceivedMessages;
    }

    /**
     * Adds listeners to events created by this class.
     * @param pcl a listener
     */
    public void addPropertyChangeListener (PropertyChangeListener pcl) {
        propertyChangeSupport.addPropertyChangeListener(pcl);
    }

    /**
     * Sends messages to a client.
     * @param message the message being sent
     * @param client the receiver client
     */
    public void sendMessage(Message message, ClientHandler client) {
        try {
            client.getOos().writeObject(message); //writes messages to the object output stream
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Inner class that is a thread and handles the connection between the server and the clients.
     */
    private class Connection extends Thread {
        @Override
        public void run() {
            Socket socket = null;
            try {
                while (true) { //continually listens for clients
                    try {
                        socket = serverSocket.accept(); //accepts a new client
                        //creates a new ClientHandler representing the new client
                        ClientHandler clientHandler = new ClientHandler(socket);
                        clientsList.add(clientHandler); //adds the new client to a list
                        clientHandler.start(); //starts listening to messages from the client
                    } catch (IOException e) {
                        e.printStackTrace();
                        if (socket!= null)
                            socket.close();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Inner class that is a thread and handles a specific client.
     */
    public class ClientHandler extends Thread {
        private ObjectOutputStream oos;
        private ObjectInputStream ois;
        private Socket socket;
        private User user;

        /**
         * Constructor of the ClientHandler class. Creates output and input streams.
         * @param socket
         */
        public ClientHandler(Socket socket) {
            this.socket = socket;
            try {
                this.oos = new ObjectOutputStream(socket.getOutputStream());
                this.ois = new ObjectInputStream(socket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * Returns the object output stream of this client.
         * @return
         */
        public ObjectOutputStream getOos() {
            return oos;
        }

        /**
         * Method that runs when the thread is started.
         */
        @Override
        public void run() {
            try {
                while (true) {
                    Message message = (Message) ois.readObject(); //message being read from the input stream
                    MessageType messageType = message.getMessageType(); //gets the message type
                    switch (messageType) { //checks which type of message has been received
                        case message: //if a user has sent a message to another user
                            propertyChangeSupport.firePropertyChange("message", null, message);
                            activityController.writeToLogFile(message); //adds message to log file
                            break;
                        case logIn: //if a user has logged in
                            user = message.getSender();
                            propertyChangeSupport.firePropertyChange("login", this, user);
                            activityController.writeToLogFile(message); //adds message to log file
                            break;
                        case logOut: //if a user has logged out
                            propertyChangeSupport.firePropertyChange("logout",null,message);
                            activityController.writeToLogFile(message); //adds message to log file
                            break;
                        case registerUser: //if a new user has been registered
                            propertyChangeSupport.firePropertyChange("register", message, this);
                            activityController.writeToLogFile(message); //adds message to log file
                            break;
                        case addFriends: //if a user has added a new friend to their contacts list
                            propertyChangeSupport.firePropertyChange("updateFriendsList", message, this);
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    ois.close();
                    oos.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}