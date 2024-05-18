package Client;

import Entity.Message;
import Entity.MessageType;
import Entity.User;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDateTime;
import java.util.List;


/**
 * This class handles sending and receiving messages within the chat application.
 * It listens for property changes to process incoming messages.
 */

public class ClientMessageController implements PropertyChangeListener{
    private ClientViewController clientViewController;

    /**
     * Constructor for A new clientMessageController
     * @param clientViewController
     */
    public ClientMessageController(ClientViewController clientViewController) {
        clientViewController.getLogController().getCnb().addPropertyChangeListener(this);
        this.clientViewController = clientViewController;
    }

    /**
     * Listener for "message received" events and processes the incoming messages.
     * @param evt A PropertyChangeEvent object describing the event source
     *          and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("messageReceived".equals(evt.getPropertyName())) {
            Message message = (Message) evt.getNewValue();
            receiveMessage(message);
        }
    }

    /**
     * sends a message to the current chat
     * @param text the text message
     */
    public void sendMessage(String text) {
        List<User> recievers = clientViewController.getContactController().getChatWith();
        Message message = new Message(MessageType.message, text, clientViewController.getLogController().getLoggedInUser(), recievers,
                null, null);
        clientViewController.getLogController().getCnb().sendMessage(message);
    }

    /**
     * Updates The user interface and displays the received message
     * @param message the received message
     */
    public void receiveMessage(Message message) {
        String senderName = message.getSender().getUserName();
        String text = message.getText();
        clientViewController.getMainFrame().getMainPanel().getLeftPanel().receivedMessage(senderName, text);
    }
}
