package Entity;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class used to save the message that we're sent to offline users.
 * Saving the messages using a HashMap until the receiver user gets online.
 */
public class UnreceivedMessages implements Serializable {
    //private static final long serialVersionUID = 1L;
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
    
    public synchronized ArrayList<Message> get(User user){
        return messages.get(user);
    }

    public synchronized void saveToFile(String fileName) {
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(messages);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Unreceived messages : saveToFile ");
        }
    }
    public synchronized void loadFromFile(String fileName) {
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))){
            messages = (HashMap<User, ArrayList<Message>>) ois.readObject();

        } catch ( IOException | ClassNotFoundException e ) {
            e.printStackTrace();
            System.out.println("Unreceived message : loadFromFile Catch1");
        }
    }
}
