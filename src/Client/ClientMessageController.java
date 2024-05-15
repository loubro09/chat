package Client;

import Client.view.MainFrame;
import Entity.Message;
import Entity.MessageType;
import Entity.User;
import java.beans.PropertyChangeListener;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class ClientMessageController {
    private ClientNetworkBoundary networkBoundary;
    private ClientViewController clientViewController;
    private UnsendMessages unsendMessages;

    public ClientMessageController(ClientViewController clientViewController) {
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
        Message message = new Message(MessageType.message, text, con, new User("loubro"),
                LocalDateTime.now(), LocalDateTime.now()); // Skapa meddelandeobjekt med den aktuella texten
        networkBoundary.sendMessage(message);
    }

    private void receiveMessage(Message message) {
        String senderName = message.getSender() != null ? message.getSender().getUserName() : "Unknown";
        String text = message.getText();
        mainFrame.showMessage(senderName + ": " + text); // Visa meddelandet i GUI
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
