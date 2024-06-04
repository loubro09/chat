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
     * @param message the message containing the chat message
     */
    private void sendMessageToReceiver(Message message) {
        List<User> receivers = message.getReceivers(); //list of all receivers in the message
        //iterates through each user in the list
        for (User receiver : receivers) {
            User user = uc.getUserByUsername(receiver.getUserName());
            ServerNetworkBoundary.ClientHandler clientHandler = uc.getClients().get(user);
            if (clientHandler != null) { //if the receiver is logged in
                //creates the message for the receiver
                System.out.println(clientHandler.getUser().getUserName() + " is logged in");
                Message messageToReceiver = new Message(MessageType.message, message.getText(), message.getSender(),
                        receiver, message.getTimeDeliveredToServer(), null, message.getImage());
                messageToReceiver.setTimeDeliveredToClient(LocalDateTime.now()); //timestamps
                //sends the message to the receiver
                uc.getServerNetworkBoundary().sendMessage(messageToReceiver, clientHandler);
            } else { //if the receiver is not logged in the message is stored in buffer
                //System.out.println(clientHandler.getUser().getUserName() + " is not logged in. The message will be saved.");
                Message messageToReceiver = new Message(MessageType.message, message.getText(), message.getSender(),
                        receiver, message.getTimeDeliveredToServer(), null, message.getImage());
                uc.getServerNetworkBoundary().getUnreceivedMessage().put(receiver, messageToReceiver); //saves message in buffer
            }
        }
    }
}

