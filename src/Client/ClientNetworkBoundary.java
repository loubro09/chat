package Client;

import Entity.Message;
import Entity.MessageType;
import Entity.User;
import Server.ServerNetworkBoundary;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientNetworkBoundary {
    private Socket socket;
    private PropertyChangeSupport propertyChangeSupport;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

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

    public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener){
        propertyChangeSupport.addPropertyChangeListener(propertyChangeListener);
    }

    synchronized public void sendMessage(Message message) {
        try {
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
