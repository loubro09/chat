package Entity;

import java.io.Serializable;
import java.time.LocalDateTime;
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
    public Message(MessageType messageType, String text, User sender, List<User> receivers, LocalDateTime timeDeliveredToServer, LocalDateTime timeDeliveredToClient) {
        this.messageType = messageType;
        this.text = text;
        this.sender = sender;
        this.receivers = receivers;
        this.timeDeliveredToServer = timeDeliveredToServer;
        this.timeDeliveredToClient = timeDeliveredToClient;
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
    public Message(MessageType messageType, String text, User sender, User receiver, LocalDateTime timeDeliveredToServer, LocalDateTime timeDeliveredToClient) {
        this.messageType = messageType;
        this.text = text;
        this.sender = sender;
        this.receiver = receiver;
        this.timeDeliveredToServer = timeDeliveredToServer;
        this.timeDeliveredToClient = timeDeliveredToClient;
    }

    /**
     * A getter for the timeDelivered time.
     * @return timeDelivered.
     */
    public LocalDateTime getTimeDeliveredToServer() {
        return timeDeliveredToServer;
    }

    /**
     * A getter for the timeReceived time.
     * @return timeReceived.
     */
    public LocalDateTime getTimeDeliveredToClient() {
        return timeDeliveredToClient;
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
                str = sender.getUserName() + " sent a message to " + receiver.getUserName() + "\n Time " +
                        "received by Server : " + timeDeliveredToServer + "\nTime received by client: " + timeDeliveredToClient;
                break;
            case loginSuccess:
                str = sender.getUserName() + " has logged in.";
                break;
            case registerSuccess:
                str = sender.getUserName() + " has created an account.";
                break;
            case logOut:
                str = sender.getUserName() + " has logged out.";
        }
        return str;
        //return "" + messageType + " - user: " + sender.getUserName();
    }

    /**
     * Getter for the receivers list.
     * @return receivers List.
     */
    public List<User> getReceivers() {return receivers;}

    /**
     * Getter for the receiver user object.
     * @return receiver User object.
     */

    public void setTimeDeliveredToServer(LocalDateTime timeDeliveredToServer) {
        this.timeDeliveredToServer = timeDeliveredToServer;
    }

    public void setTimeDeliveredToClient(LocalDateTime timeDeliveredToClient) {
        this.timeDeliveredToClient = timeDeliveredToClient;
    }
}
