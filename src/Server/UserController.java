package Server;

import Entity.Message;
import Entity.MessageType;
import Entity.User;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The UserController class handles a user registering a new account, logging in or out or adding a new friend.
 */
public class UserController implements PropertyChangeListener {

    private ServerNetworkBoundary serverNetworkBoundary;
    private HashMap<User, ServerNetworkBoundary.ClientHandler> clients = new HashMap<>();
    private List<User> allUsers;
    private String userFileName = "userFile.txt";

    /**
     * Constructor of the UserController class.
     * @param serverNetworkBoundary
     */
    public UserController(ServerNetworkBoundary serverNetworkBoundary){
        this.serverNetworkBoundary = serverNetworkBoundary;
        serverNetworkBoundary.addPropertyChangeListener(this);
        allUsers = new ArrayList<>();
        createFile(userFileName);
        allUsers = readUsersFromFile(userFileName);


    }

    /**
     * @param evt A PropertyChangeEvent object describing the event source
     *          and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("login".equals(evt.getPropertyName())){
            User user = (User) evt.getNewValue();
            ServerNetworkBoundary.ClientHandler client = (ServerNetworkBoundary.ClientHandler) evt.getOldValue();
            logIn(user, client);
        }
        else if("register".equals(evt.getPropertyName())) {
            Message message = (Message) evt.getOldValue();
            User user = message.getSender();
            ServerNetworkBoundary.ClientHandler client = (ServerNetworkBoundary.ClientHandler) evt.getNewValue();
            try {
                addUser(user, client);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if ("logout".equals(evt.getPropertyName())) {
            Message message = (Message) evt.getNewValue();
            User user = message.getSender();
            logOut(user);
        }
        else if ("updateFriendsList".equals(evt.getPropertyName())) {
            Message message = (Message) evt.getOldValue();
            String userName = message.getSender().getUserName();
            List<User> friends = message.getReceivers();
            appendUsersToFile(userName, friends);
        }
    }

    /**
     * Handles a user logging in.
     * @param user the user that has logged in.
     * @param client the user's socket connection.
     */
    public void logIn(User user, ServerNetworkBoundary.ClientHandler client) {
        if (checkIfUsersExists(user)) {
            User savedUser = null;
            String userName = user.getUserName();
            for (User u : allUsers) {
                if (u.getUserName().equals(userName)) {
                    savedUser = u;
                    updateFriendsListFromFile(savedUser);
                }
            }
            if (checkHashmapList(savedUser)) {
                clients.remove(savedUser);
                System.out.println("User " + savedUser.getUserName() + " is already logged in. Updating login information.");
            }

            clients.put(savedUser, client);
            System.out.println("User " + savedUser.getUserName() + " logged in successfully.");
            savedUser.setOnline(true);
            Message loginSuccessMessage = new Message(MessageType.loginSuccess, savedUser, allUsers);
            serverNetworkBoundary.sendMessage(loginSuccessMessage, client);
            Message userLoggedInMessage = new Message(MessageType.userLoggedIn, savedUser);
            for (ServerNetworkBoundary.ClientHandler receiver : clients.values()) {
                if (receiver != null) {
                    serverNetworkBoundary.sendMessage(userLoggedInMessage, receiver);
                }
            }
            sendUnreceivedMessages(savedUser,client);
        } else {
            System.out.println("User " + user.getUserName() + " does not exist.");
            Message loginFailMessage = new Message(MessageType.loginFail);
            serverNetworkBoundary.sendMessage(loginFailMessage, client);
        }
    }

    /**
     * Sends the user all its unreceived messages when it has logged in.
     * @param user the receiver of the messages
     * @param client the receiver's socket connection
     */
    private void sendUnreceivedMessages(User user, ServerNetworkBoundary.ClientHandler client) {
        //gets list of unreceived messages for the user
        ArrayList<Message> unreceivedMessages = serverNetworkBoundary.getUnreceivedMessage().retrieveMessages(user);
        if (unreceivedMessages != null && !unreceivedMessages.isEmpty()) {
            for (Message msg : unreceivedMessages) {
                if (msg != null) {
                    System.out.println("Unreceived message to " + user.getUserName() + " is sent: " + msg.getText());
                    msg.setTimeDeliveredToClient(LocalDateTime.now());
                    serverNetworkBoundary.sendMessage(msg, client);
                }
            }
        }
        else {
            System.out.println("No unreceived messages");
        }
    }

    /**
     * Handles a user logging out of the program.
     * @param user the user logging out.
     */
    private void logOut(User user) {
        user.setOnline(false);

        User userToRemove = null;
        for (User u : clients.keySet()) {
            if (u.getUserName().equals(user.getUserName())) {
                userToRemove = u;
                break;
            }
        }
        if (userToRemove != null) {
            clients.remove(userToRemove);
        }
        for (User u : allUsers) {
            if (u.getUserName().equals(user.getUserName())) {
                u.setOnline(false);
                break;
            }
        }

        Message userLoggedOutMessage = new Message(MessageType.userLoggedOut, user);
        for (ServerNetworkBoundary.ClientHandler receiver : clients.values()) {
            if (receiver != null) {
                serverNetworkBoundary.sendMessage(userLoggedOutMessage, receiver);
            }
        }
    }

    /**
     * Adds a new user to the program.
     * @param user the user being added
     * @param client the socket connection of the user
     * @throws IOException
     */
    private void addUser(User user, ServerNetworkBoundary.ClientHandler client) throws IOException {
        boolean userExists = checkIfUsersExists(user);

        if (!userExists) {
            allUsers.add(user);
            addUsersToFile(userFileName);
            Message message = new Message(MessageType.registerSuccess);
            serverNetworkBoundary.sendMessage(message, client);
        }
        else {
            Message message = new Message(MessageType.registerFail);
            serverNetworkBoundary.sendMessage(message, client);
        }
    }

    /**
     * Checks if a user has already made an account.
     * @param user the user being checked
     * @return true if the user exists, false if it doesn't
     */
    public boolean checkIfUsersExists(User user) {
        boolean b = false;
        for (User u : allUsers) {
            if (u.getUserName().equals(user.getUserName())) {
                return true;
            } else {
                System.out.println("Can't find user");
                b = false;
            }
        }return b;
    }

    /**
     * Checks if a user is already logged in to the program.
     * @param user the user being checked
     * @return true if the user is logged in, false if the user is logged out
     */
    public boolean checkHashmapList(User user){
        boolean b = false;
        for(User u : clients.keySet()) {
            if (u.getUserName().equals(user.getUserName())){
                b = true;
            }
            else {
                b = false;
            }
        }
        return b;
    }

    /**
     * Creates a file containing the names of all users of the program.
     * @param filePath the path of the file
     */
    private void createFile(String filePath) {
        File file = new File(filePath);
        try {
            if (file.createNewFile()) {
                addTestValues();
                addUsersToFile(filePath);
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds all saved users to a file.
     * @param filePath the path of the file
     */
    private void addUsersToFile(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (User user : allUsers) {
                writer.write(user.getUserName());
                writer.newLine();
                createUserFile(user);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a new file for a user to contain their friends list
     * @param user the user
     */
    private void createUserFile(User user) {
        String userFilePath = user.getUserName() + ".txt";
        File userFile = new File(userFilePath);
        try {
            if (userFile.createNewFile()) {
                System.out.println("Created file: " + userFilePath);
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(userFile))) {
                    writer.write(user.getUserName());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("File already exists: " + userFilePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a user's new friends to their file.
     * @param username the name of the user adding a friends
     * @param friends the list of the users friends
     */
    private void appendUsersToFile(String username, List<User> friends) {
        String userFilePath = username + ".txt";
        File userFile = new File(userFilePath);
        try (BufferedReader reader = new BufferedReader(new FileReader(userFile))) {
            List<String> existingUsers = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                existingUsers.add(line.trim());
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(userFile, true))) {
                writer.newLine();
                for (User user : friends) {
                    String friendName = user.getUserName();
                    if (!existingUsers.contains(friendName)) {
                        writer.write(friendName);
                        writer.newLine();
                        existingUsers.add(friendName);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads all users that has created an account from a file.
     * @param filePath the path of the file being read from
     * @return the list of users that has created an account in the program
     */
    private List<User> readUsersFromFile(String filePath) {
        List<User> userList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String userName = line.trim();
                User user = new User(userName);
                userList.add(user);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userList;
    }

    /**
     * Updates a user friends list.
     * @param loggedInUser the user
     */
    public void updateFriendsListFromFile(User loggedInUser) {
        String userFilePath = loggedInUser.getUserName() + ".txt";
        File userFile = new File(userFilePath);
        try (BufferedReader reader = new BufferedReader(new FileReader(userFile))) {
            String line;
            if ((line = reader.readLine()) != null) {
                String userName = line.trim();
                User user = getUserByUsername(userName);
                if (user != null) {
                    List<User> friends = new ArrayList<>();
                    while ((line = reader.readLine()) != null) {
                        String friendUserName = line.trim();
                        User friend = getUserByUsername(friendUserName);
                        if (friend != null) {
                            friends.add(friend);
                        }
                    }
                    loggedInUser.setFriendList(friends);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds test values to the program.
     */
    private void addTestValues() {
        User user1 = new User("loubro");
        User user2 = new User("alacol");
        User user3 = new User("idanor");
        User user4 = new User("kenalt");
        user1.setOnline(false);
        user2.setOnline(false);
        user3.setOnline(false);
        user4.setOnline(false);
        allUsers.add(user1);
        allUsers.add(user2);
        allUsers.add(user3);
        allUsers.add(user4);
    }

    /**
     * Returns a user with the matching username.
     * @param userName the username of the user
     * @return the user
     */
    public User getUserByUsername(String userName) {
        for (User user : allUsers) {
            if (user.getUserName().equals(userName)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Returns the Server Network Boundary instance of this class.
     * @return the Server Network Boundary instance
     */
    public ServerNetworkBoundary getServerNetworkBoundary() {
        return serverNetworkBoundary;
    }

    /**
     * Returns the hashmap of the clients
     * @return the hashmap
     */
    public HashMap<User, ServerNetworkBoundary.ClientHandler> getClients() {
        return clients;
    }
}