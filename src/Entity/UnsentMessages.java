package Entity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class used to save the message that we're sent to offline users.
 * Saving the messages using a HashMap until the receiver user gets online.
 */
public class UnsentMessages {
    private HashMap<User, ArrayList<Message>> messages = new HashMap<>();

    /**
     * A method to retrieve the ArrayList for the user, or create a new one if it doesn't exist.
     * Adds the message to the ArrayList.
     * Puts the ArrayList back into the HashMap.
     * @param user The receiver user
     * @param message
     */
    public synchronized void put(User user, Message message) {
        ArrayList<Message> userMessages = messages.getOrDefault(user, new ArrayList<>());
        userMessages.add(message);
        messages.put(user, userMessages);
    }

    /**
     * Getter for the ArrayList for a specific user.
     * @param user the user object.
     * @return returns the ArrayList for the user object.
     */
    public synchronized ArrayList<Message> get(User user) {
        return messages.get(user);
    }

// fler synchronized-metoder som behövs
}
