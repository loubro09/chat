package Server;

import Entity.Message;
import Entity.MessageType;
import Entity.User;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDateTime;
import java.util.List;

/**
 * The ServerMessageController class listens for changes regarding messages being sent between users, and handles the
 * messages.
 */
public class ServerMessageController implements PropertyChangeListener {
    private UserController uc;

    /**
     * Constructor for the ServerMessageController class.
     * @param uc
     */
    public ServerMessageController(UserController uc) {
        this.uc = uc;
        uc.getServerNetworkBoundary().addPropertyChangeListener(this);
    }

    /**
     * @param evt A PropertyChangeEvent object describing the event source
     *          and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("message".equals(evt.getPropertyName())) { //listens for messages
            Message message = (Message) evt.getNewValue();
            message.setTimeDeliveredToServer(LocalDateTime.now()); //timestamps
            sendMessageToReceiver(message);
        }
    }

    /**
     * Sends message received to all users in the receiver list.
     * If reciever is not logged in the messages are stored in a buffer.
     * @param message the message containing the chat message
     */
    private void sendMessageToReceiver(Message message) {
        List<User> receivers = message.getReceivers();
        for (User receiver : receivers) {
            User user = uc.getUserByUsername(receiver.getUserName());
            ServerNetworkBoundary.ClientHandler clientHandler = uc.getClients().get(user);

            if (clientHandler != null) {
                Message messageToReceiver = new Message(MessageType.message, message.getText(), message.getSender(),
                        receiver, message.getTimeDeliveredToServer(), null, message.getImage());
                messageToReceiver.setTimeDeliveredToClient(LocalDateTime.now());
                uc.getServerNetworkBoundary().sendMessage(messageToReceiver, clientHandler);

            } else {
                Message messageToReceiver = new Message(MessageType.message, message.getText(), message.getSender(),
                        receiver, message.getTimeDeliveredToServer(), null, message.getImage());
                uc.getServerNetworkBoundary().getUnreceivedMessage().put(receiver, messageToReceiver);
            }
        }
    }
}