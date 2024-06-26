package Server;

import Entity.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.EOFException;
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
            this.serverSocket = new ServerSocket(port);
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
            client.getOos().writeObject(message);
            if (message.getMessageType()!= MessageType.userLoggedIn) {
                activityController.writeToLogFile(message);
            }
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
                while (true) {
                    try {
                        socket = serverSocket.accept();
                        ClientHandler clientHandler = new ClientHandler(socket);
                        clientsList.add(clientHandler);
                        clientHandler.start();
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
                try {
                    if (ois != null) ois.close();
                    if (oos != null) oos.close();
                    if (socket != null && !socket.isClosed()) socket.close();
                } catch (IOException ef) {
                    ef.printStackTrace();
                }
            }
        }

        /**
         * Returns the user belonging to this client socket.
         * @return the user.
         */
        public User getUser() {
            return user;
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
                    Message message = (Message) ois.readObject();
                    MessageType messageType = message.getMessageType();
                    switch (messageType) {
                        case message:
                            propertyChangeSupport.firePropertyChange("message", null, message);
                            break;
                        case logIn:
                            user = message.getSender();
                            propertyChangeSupport.firePropertyChange("login", this, user);
                            break;
                        case logOut:
                            propertyChangeSupport.firePropertyChange("logout", null, message);
                            break;
                        case registerUser:
                            propertyChangeSupport.firePropertyChange("register", message, this);
                            break;
                        case addFriends:
                            propertyChangeSupport.firePropertyChange("updateFriendsList", message, this);
                            activityController.writeToLogFile(message);
                    }
                }
            } catch (EOFException ef) {
                System.out.println("Client closed the connection.");
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