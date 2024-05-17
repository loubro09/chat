package Entity;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Class used to save the message that we're sent to offline users.
 * Saving the messages using a HashMap until the receiver user gets online.
 */
public class UnreceivedMessages implements Serializable {
    //private static final long serialVersionUID = 1L;
    private HashMap<User, ArrayList<Message>> messages = new HashMap<>();
    private String fileName = "unreceivedmessages.dat";

    public UnreceivedMessages() {
        loadFromFile(fileName);
    }

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
        System.out.println(message.getText());
        messages.put(user, userMessages);
        saveToFile(fileName);
    }
    public synchronized ArrayList<Message> retrieveMessages(User user) {
        ArrayList<Message> userMessages = new ArrayList<>();
        Iterator<Map.Entry<User, ArrayList<Message>>> iterator = messages.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<User, ArrayList<Message>> entry = iterator.next();
            if (entry.getKey().getUserName().equals(user.getUserName())) {
                System.out.println(user.getUserName() + " oijnq3g");
                userMessages.addAll(entry.getValue());
                for (Message message : userMessages) {
                    System.out.println("Message for user " + user.getUserName() + ": " + message.getText());
                }
                iterator.remove(); // Remove the entry from the hashmap using iterator's remove() method
                saveToFile(fileName); // Save changes after removing message
            }
        }
        return userMessages;
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
            test();

        } catch (FileNotFoundException e) {
            System.out.println("File not found. Creating a new file: " + fileName);
            saveToFile(fileName);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Unreceived message : loadFromFile Catch");
        }
    }

    private void test(){
        for (User user : messages.keySet()) {
            System.out.println("Messages for user: " + user.getUserName());
            ArrayList<Message> userMessages = messages.get(user);
            for (Message message : userMessages) {
                System.out.println("Message: " + message.getText());
            }
        }
    }
}