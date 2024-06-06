package Entity;

import javax.swing.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * A class representing the different messages that will be sent from and to the server.
 * implementing Serializable so that the instances can be serialized, to let them
 * be converted into a stream of bytes that can be stored in a file or sent over the network.
 * The class represents messages like login, log out and sending a text message.
 */
public class Message implements Serializable {
    private static final long serialVersionUID = -6224408929258950762L;
    private MessageType messageType;
    private String text;
    private User sender;
    private List<User> receivers;
    private User receiver;
    private LocalDateTime timeDeliveredToServer;
    private LocalDateTime timeDeliveredToClient;
    private ImageIcon image;

    /**
     * A constructor that only takes the type of message as a parameter.
     * @param messageType  The type of message being sent.
     */
    public Message(MessageType messageType) {
        this.messageType = messageType;
    }

    /**
     * A constructor that takes a message type and a user as parameters.
     * @param messageType The type of message being sent.
     * @param user  The user object for this message.
     */
    public Message(MessageType messageType, User user) {
        this.messageType = messageType;
        this.sender = user;
    }

    /**
     * A constructor that takes a message type, a user
     * and a list of all users as parameters
     * @param messageType The type of message being sent.
     * @param savedUser The logged in user.
     * @param allUsers  A list of all users in the system.
     */
    public Message(MessageType messageType, User savedUser, List<User> allUsers) {
        this.messageType = messageType;
        this.sender = savedUser;
        this.receivers = allUsers;
    }

    /**
     * A constructor that takes a message type (a message), a message text, a sender user object,
     * a list of the users that will receive the message, time for when the message got delivered and receive as parameters.
     * This constructor is used to create text message objects.
     * @param messageType They type of message being sent.
     * @param text The text that will be sent in the message.
     * @param sender The user object that's sending the message.
     * @param receivers The user object that will be receiving the message.
     * @param timeDeliveredToServer The time the message was delivered.
     * @param timeDeliveredToClient The time the message was received.
     */
    public Message(MessageType messageType, String text, User sender, List<User> receivers, LocalDateTime timeDeliveredToServer, LocalDateTime timeDeliveredToClient, ImageIcon image) {
        this.messageType = messageType;
        this.text = text;
        this.sender = sender;
        this.receivers = receivers;
        this.timeDeliveredToServer = timeDeliveredToServer;
        this.timeDeliveredToClient = timeDeliveredToClient;
        this.image = image;
    }

    /**
     * A constructor that takes a message type (a message), a message text, a sender user object,
     * the user that will receive the message, time for when the message got delivered and receive as parameters.
     * This constructor is used to create text message objects.
     * @param messageType They type of message being sent.
     * @param text The text that will be sent in the message.
     * @param sender The user object that's sending the message.
     * @param receiver The user object that's receiving the message.
     * @param timeDeliveredToServer The time the message was delivered.
     * @param timeDeliveredToClient The time the message was received.
     */
    public Message(MessageType messageType, String text, User sender, User receiver, LocalDateTime timeDeliveredToServer, LocalDateTime timeDeliveredToClient, ImageIcon image) {
        this.messageType = messageType;
        this.text = text;
        this.sender = sender;
        this.receiver = receiver;
        this.timeDeliveredToServer = timeDeliveredToServer;
        this.timeDeliveredToClient = timeDeliveredToClient;
        this.image = image;
    }

    public ImageIcon getImage() {
        return image;
    }

    /**
     * A getter for the timeDelivered time.
     * @return timeDelivered.
     */
    public LocalDateTime getTimeDeliveredToServer() {
        return timeDeliveredToServer;
    }

    /**
     * Getter for the text String.
     * @return text String.
     */
    public String getText(){
        return text;
    }

    /**
     * Getter for the sender user object.
     * @return sender User object.
     */
    public User getSender(){
        return sender;
    }

    /**
     * Getter for the messageType.
     * @return messageType Enum.
     */
    public MessageType getMessageType() {
        return messageType;
    }

    /**
     * Method to return the information as a String
     * @return String messageType and Sender's username.
     */
    @Override
    public String toString() {
        String str = "";
        switch (messageType) {
            case message:
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                str = sender.getUserName() + " sent a message to " + receiver.getUserName() + ". Time " +
                        "received by Server : " + timeDeliveredToServer.format(formatter) +
                        ". Time received by client: " + timeDeliveredToClient.format(formatter) + ". Message : " +
                        text;
                break;
            case loginSuccess:
                str = sender.getUserName() + " has logged in.";
                break;
            case addFriends:
                str = sender.getUserName() + " Added a new friend";
                break;
            case registerSuccess:
                str = sender.getUserName() + " has created an account.";
                break;
            case logOut:
                str = sender.getUserName() + " has logged out.";
        }
        return str;
    }

    /**
     * Getter for the receivers list.
     * @return receivers List.
     */
    public List<User> getReceivers() {return receivers;}

    /**
     * Sets the timeDeliveredToServer variable.
     * @param timeDeliveredToServer
     */
    public void setTimeDeliveredToServer(LocalDateTime timeDeliveredToServer) {
        this.timeDeliveredToServer = timeDeliveredToServer;
    }

    /**
     * Sets the timeDeliveredToClient variable.
     * @param timeDeliveredToClient
     */
    public void setTimeDeliveredToClient(LocalDateTime timeDeliveredToClient) {
        this.timeDeliveredToClient = timeDeliveredToClient;
    }
}
