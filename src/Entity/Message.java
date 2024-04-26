package Entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class Message implements Serializable {
    private MessageType messageType;
    private String text;
    private User sender;
    private List<User> receivers;
    private LocalDateTime timeDelivered;
    private LocalDateTime timeReceived;

    public Message(MessageType messageType) {
        this.messageType = messageType;
    }

    public Message(MessageType messageType, User user) {
        this.messageType = messageType; sender = user;
    }

    public Message(MessageType messageType, User savedUser, List<User> allUsers) {
        this.messageType = messageType; sender = savedUser; receivers = allUsers;
    }

    public Message(MessageType messageType, String text, User sender, List<User> receivers, LocalDateTime timeDelivered, LocalDateTime timeReceived) {
        this.messageType = messageType;
        this.text = text;
        this.sender = sender;
        this.receivers = receivers;
        this.timeDelivered = timeDelivered;
        this.timeReceived = timeReceived;
    }

    public String getText(){
        return text;
    }

    public User getSender(){
        return sender;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public List<User> getReceivers() {return receivers;}
}
