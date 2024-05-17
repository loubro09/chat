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
            message.setTimeDeliveredToServer(LocalDateTime.now());
            sendMessageToReceiver(message);
        }
    }

    /**
     * Sends message received to all users in the receiver list.
     * @param message the message containing the chat message
     */
    private void sendMessageToReceiver(Message message) {
        List<User> receivers = message.getReceivers();
        //list of all receivers in the message
        //iterates through each user in the list
        for (User receiver : receivers) {
            System.out.println("Receiver: " + receiver.getUserName());
            User user = uc.getUserByUsername(receiver.getUserName());
            //iterates through each entry in the clients list from the UserController
            //for (Map.Entry<User, ServerNetworkBoundary.ClientHandler> entry : uc.getClients().entrySet()) {
                //gets the user associated with the current entry
                //User userInClientsMap = entry.getKey();
                //checks if the username of the user in the clients map is the same as the username of the receiver
                //if (userInClientsMap.getUserName().equals(receiver.getUserName())) {
                    //gets the client handler associated with the user
                    //ServerNetworkBoundary.ClientHandler clientHandler = entry.getValue();
                    //System.out.println(clientHandler.getUser().getUserName() + " is logged in");
                     ServerNetworkBoundary.ClientHandler clientHandler = uc.getClients().get(user);
                    if (clientHandler != null) { //if the receiver is logged in
                        //creates the message for the receiver
                        System.out.println(clientHandler.getUser().getUserName() + " is logged in");
                        Message messageToReceiver = new Message(MessageType.message, message.getText(), message.getSender(),
                                receiver, message.getTimeDeliveredToServer(), null);
                        messageToReceiver.setTimeDeliveredToClient(LocalDateTime.now());
                        //sends the message to the receiver
                        uc.getServerNetworkBoundary().sendMessage(messageToReceiver, clientHandler);
                    } else { //if the receiver is not logged in the message is stored in buffer
                        System.out.println("not online receiver");
                        uc.getServerNetworkBoundary().getUnreceivedMessage().put(receiver, message);
                    }
                }
            }
        //}
    //}
}

