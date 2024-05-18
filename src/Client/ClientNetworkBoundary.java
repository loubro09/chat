package Client;

import Entity.Message;
import Entity.MessageType;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * This class handles the communication for the client.
 * CLientNetworkBoundary connects to a server, sends messages, and listens for incoming messages.
 */
public class
ClientNetworkBoundary {
    private Socket socket;
    private PropertyChangeSupport propertyChangeSupport;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    /**
     * Constructor for ClientNetworkBoundary. Connects to server with a specified ip and port.
     * @param ip adress
     * @param port number
     */
    public ClientNetworkBoundary(String ip, int port){
        this.propertyChangeSupport=new PropertyChangeSupport(this);
        try{
            this.socket=new Socket(ip, port);
            this.oos = new ObjectOutputStream(socket.getOutputStream());
            this.ois = new ObjectInputStream(socket.getInputStream());
            new Thread(new Listener()).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a propertyChangeListener to the listener list,
     * @param propertyChangeListener to be added
     */

    public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener){
        propertyChangeSupport.addPropertyChangeListener(propertyChangeListener);
    }

    /**
     * method for sending a message to the server
     * @param message
     */
    synchronized public void sendMessage(Message message) {
        try {
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This class listens for incoming messages from the server and process them.
     */
    private class Listener implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    Message message = (Message) ois.readObject();
                    MessageType messageType = message.getMessageType();
                    switch (messageType) {
                        case registerFail:
                            propertyChangeSupport.firePropertyChange("registerFail", null, message);
                            break;
                        case registerSuccess:
                            propertyChangeSupport.firePropertyChange("registerSuccess", null, message);
                            break;
                        case loginSuccess:
                            propertyChangeSupport.firePropertyChange("loginSuccess", null, message);
                            break;
                        case loginFail:
                            propertyChangeSupport.firePropertyChange("logFail",null,message);
                            break;
                        case userLoggedIn:
                            propertyChangeSupport.firePropertyChange("userLoggedIn", null, message);
                            break;
                        case userLoggedOut:
                            propertyChangeSupport.firePropertyChange("userLoggedOut", null, message);
                            break;
                        case message:
                            propertyChangeSupport.firePropertyChange("messageReceived", null, message);
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
