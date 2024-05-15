package Client;

import Client.view.MainFrame;
import Entity.Message;
import Entity.MessageType;
import Entity.User;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class ClientMessageController implements PropertyChangeListener{
    private ClientNetworkBoundary networkBoundary;
    private ClientViewController clientViewController;
    private UnsendMessages unsendMessages;

    public ClientMessageController(ClientViewController clientViewController) {
        clientViewController.getLogController().getCnb().addPropertyChangeListener(this);
        //this.networkBoundary = new ClientNetworkBoundary(ip, port);
        //this.mainFrame = mainFrame;
        //this.unsendMessages=new UnsendMessages();
        /*this.networkBoundary.addPropertyChangeListener(e -> {
            if ("Message received".equals(e.getPropertyName())) {
                Message message = (Message) e.getNewValue();
                receiveMessage(message);
            }
        });*/
        this.clientViewController = clientViewController;
    }

    public void sendMessage(String text) {
        Message message = new Message(MessageType.message, text, clientViewController.getLogController().getLoggedInUser(), clientViewController.getContactController().getChatWith(),
                LocalDateTime.now(), LocalDateTime.now());
        clientViewController.getLogController().getCnb().sendMessage(message);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("message".equals(evt.getPropertyName())) {
            Message message = (Message) evt.getNewValue();
            receiveMessage(message);
        }
    }

    public void receiveMessage(Message message) {
        String senderName = message.getSender().getUserName();
        String text = message.getText();
        clientViewController.getMainFrame().getMainPanel().getLeftPanel().receivedMessage(senderName, text);
    }

    public class UnsendMessages {
        private HashMap<User,ArrayList<Message>> unsend;

        public synchronized void put(User user, Message message) {
            ArrayList<Message> messages = unsend.get(user);
            if (messages == null) {
                messages = new ArrayList<>();
                unsend.put(user, messages);
            }
            messages.add(message);
        }
        public synchronized ArrayList<Message> get(User user) {
            return unsend.get(user);
        }
    }
}
