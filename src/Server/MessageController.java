package Server;

import Entity.Message;
import Entity.MessageType;
import Entity.User;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Map;

public class MessageController implements PropertyChangeListener {
    private UserController uc;

    public MessageController(UserController uc) {
        this.uc = uc;
        uc.getServerNetworkBoundary().addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("message".equals(evt.getPropertyName())){
            Message message = (Message) evt.getNewValue();
            receiveMessage(message);
        }
    }

    /*private void receiveMessage(Message message) {
        List<User> receivers = message.getReceivers();
        for (User receiver : receivers) {
            for (Map.Entry<User, ServerNetworkBoundary.ClientHandler> entry : uc.getClients().entrySet()) {
                User userInClientsMap = entry.getKey();
                if (userInClientsMap.getUserName().equals(receiver.getUserName())) {
                    // Get the client handler associated with the user
                    ServerNetworkBoundary.ClientHandler clientHandler = entry.getValue();
                    if (clientHandler != null) {
                        // Send the message to the client handler
                        Message message1 = new Message(MessageType.message, message.getText(), message.getSender(), receiver, message.getTimeDelivered(), message.getTimeReceived());
                        uc.getServerNetworkBoundary().sendMessage(message1, clientHandler);
                    } else {
                        uc.getServerNetworkBoundary().getUnsentMessages().put(receiver, message);
                    }
                    break; // Stop searching after finding the matching user
                }
            }
        }
    }*/

    private void receiveMessage(Message message) {
        User receiver = message.getReceiver();
            for (Map.Entry<User, ServerNetworkBoundary.ClientHandler> entry : uc.getClients().entrySet()) {
                User userInClientsMap = entry.getKey();
                if (userInClientsMap.getUserName().equals(receiver.getUserName())) {
                    // Get the client handler associated with the user
                    ServerNetworkBoundary.ClientHandler clientHandler = entry.getValue();
                    if (clientHandler != null) {
                        // Send the message to the client handler
                        Message message1 = new Message(MessageType.message, message.getText(), message.getSender(), receiver, message.getTimeDelivered(), message.getTimeReceived());
                        uc.getServerNetworkBoundary().sendMessage(message1, clientHandler);
                    } else {
                        uc.getServerNetworkBoundary().getUnsentMessages().put(receiver, message);
                    }
                    break; // Stop searching after finding the matching user
                }
            }
        }
    }
}
