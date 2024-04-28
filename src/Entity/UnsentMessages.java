package Entity;

import java.util.ArrayList;
import java.util.HashMap;

public class UnsentMessages {

    private HashMap<User, ArrayList<Message>> messages = new HashMap<>();
    // egna tillägg
    public synchronized void put(User user, Message message) {
        // Retrieve the ArrayList for the user, or create a new one if it doesn't exist
        ArrayList<Message> userMessages = messages.getOrDefault(user, new ArrayList<>());
        // Add the message to the ArrayList
        userMessages.add(message);
        // Put the ArrayList back into the map
        messages.put(user, userMessages);
    }
    public synchronized ArrayList<Message> get(User user) {
        // Retrieve and return the ArrayList for the user
        return messages.get(user);
    }
// fler synchronized-metoder som behövs
}
