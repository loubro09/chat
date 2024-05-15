package Client;

import Entity.Message;
import Entity.MessageType;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDateTime;

public class ClientMessageController implements PropertyChangeListener{
    private ClientViewController clientViewController;

    public ClientMessageController(ClientViewController clientViewController) {
        clientViewController.getLogController().getCnb().addPropertyChangeListener(this);
        this.clientViewController = clientViewController;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("messageReceived".equals(evt.getPropertyName())) {
            System.out.println("message recieved");
            Message message = (Message) evt.getNewValue();
            receiveMessage(message);
        }
    }

    public void sendMessage(String text) {
        Message message = new Message(MessageType.message, text, clientViewController.getLogController().getLoggedInUser(), clientViewController.getContactController().getChatWith(),
                LocalDateTime.now(), LocalDateTime.now());
        clientViewController.getLogController().getCnb().sendMessage(message);
    }

    public void receiveMessage(Message message) {
        String senderName = message.getSender().getUserName();
        String text = message.getText();
        System.out.println(senderName + ": " + text);
        clientViewController.getMainFrame().getMainPanel().getLeftPanel().receivedMessage(senderName, text);
    }
}
