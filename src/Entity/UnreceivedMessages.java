package Entity;

import java.io.*;
import java.util.*;

/**
 * Class used to save the message that we're sent to offline users.
 * Saving the messages using a HashMap until the receiver user gets online.
 */
public class UnreceivedMessages implements Serializable {
    private HashMap<User, ArrayList<Message>> messages = new HashMap<>();
    private String fileName = "unreceivedmessages.dat";

    /**
     * Constructor of UnreceivedMessages class.
     */
    public UnreceivedMessages() {
        loadFromFile(fileName);
    }

    /**
     * A method to retrieve the ArrayList for the user, or create a new one if it doesn't exist.
     * Adds the message to the ArrayList.
     * Puts the ArrayList back into the HashMap.
     * Saves the hashmap to file.
     * @param user The receiver user
     * @param message the unreceived message
     */
    public synchronized void put(User user, Message message) {
        ArrayList<Message> userMessages = messages.getOrDefault(user, new ArrayList<>());
        userMessages.add(message);
        messages.put(user, userMessages);
        saveToFile(fileName);
    }

    /**
     * Retrieves the messages from the file for a user that has logged in.
     * @param user the user that has logged in
     * @return the list of messages for the user
     */
    public synchronized ArrayList<Message> retrieveMessages(User user) {
        ArrayList<Message> userMessages = new ArrayList<>();
        Iterator<Map.Entry<User, ArrayList<Message>>> iterator = messages.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<User, ArrayList<Message>> entry = iterator.next();
            if (entry.getKey().getUserName().equals(user.getUserName())) {
                userMessages.addAll(entry.getValue());
                for (Message message : userMessages) {
                    System.out.println("Unreceived message for user " + user.getUserName() + ": " + message.getText());
                }
                iterator.remove();
                saveToFile(fileName);
            }
        }
        Collections.sort(userMessages, new Comparator<Message>() {
            @Override
            public int compare(Message m1, Message m2) {
                return m1.getTimeDeliveredToServer().compareTo(m2.getTimeDeliveredToServer());
            }
        });

        return userMessages;
    }

    /**
     * Saves hashmap of messages to file.
     * @param fileName the name of the file.
     */
    public synchronized void saveToFile(String fileName) {
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(messages);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads messages from file.
     * @param fileName the name of the file.
     */
    public synchronized void loadFromFile(String fileName) {
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))){
            messages = (HashMap<User, ArrayList<Message>>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("File not found. Creating a new file: " + fileName);
            saveToFile(fileName);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}