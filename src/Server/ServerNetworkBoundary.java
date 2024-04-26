package Server;

import Entity.Buffer;
import Entity.Message;
import Entity.MessageType;
import Entity.User;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerNetworkBoundary {
    private ServerSocket serverSocket;
    private PropertyChangeSupport propertyChangeSupport;
    private Buffer<Message> messageBuffer = new Buffer<>();
    private Buffer<Message> logoutBuffer = new Buffer<>();
    private List<ClientHandler> clientsList;


    public ServerNetworkBoundary(int port) {
        this.propertyChangeSupport = new PropertyChangeSupport(this);
        clientsList = new ArrayList<>();
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Connection().start();
    }

    public void addPropertyChangeListener (PropertyChangeListener pcl) {
        propertyChangeSupport.addPropertyChangeListener(pcl);
    }

    public void sendMessage(Message message, ClientHandler client) {
        try {
            client.getOos().writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

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
                    }
                    catch (IOException e) {
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

    public class ClientHandler extends Thread {
        private ObjectOutputStream oos;
        private ObjectInputStream ois;
        private Socket socket;
        private User user;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            try {
                this.oos = new ObjectOutputStream(socket.getOutputStream());
                this.ois = new ObjectInputStream(socket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public ObjectOutputStream getOos() {
            return oos;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    Message message = (Message) ois.readObject();
                    MessageType messageType = message.getMessageType();
                    switch (messageType) {
                        case message:
                            messageBuffer.put(message);
                            break;
                        case logIn:
                            user = message.getSender();
                            propertyChangeSupport.firePropertyChange("login", this, user);
                            break;
                        case logOut:
                            //logoutBuffer.put(message);
                            propertyChangeSupport.firePropertyChange("logout",null,message);
                            break;
                        case registerUser:
                            propertyChangeSupport.firePropertyChange("register", message, this);
                            break;
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