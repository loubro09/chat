package Server;

import Entity.Message;
import Entity.MessageType;
import Entity.User;

import javax.annotation.processing.Filer;
import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserController implements PropertyChangeListener {
    private ServerNetworkBoundary serverNetworkBoundary;
    private HashMap<User, ServerNetworkBoundary.ClientHandler> clients = new HashMap<>();
    private List<User> allUsers;
    private String userFileName = "userFile.txt";

    public UserController(ServerNetworkBoundary serverNetworkBoundary){
        this.serverNetworkBoundary = serverNetworkBoundary;
        serverNetworkBoundary.addPropertyChangeListener(this);
        allUsers = new ArrayList<>();
        createFile(userFileName);
        allUsers = readUsersFromFile(userFileName);
    }

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
            Message message = new Message(MessageType.loginSuccess, savedUser, allUsers);
            serverNetworkBoundary.sendMessage(message, client);
            Message message1 = new Message(MessageType.userLoggedIn, savedUser);
            for (int i = 0; i < clients.size(); i++) {
                ServerNetworkBoundary.ClientHandler reciever = clients.get(i);
                if (reciever != null) {
                    serverNetworkBoundary.sendMessage(message1, reciever);
                }
            }
        } else {
            System.out.println("User " + user.getUserName() + " does not exist.");
            Message message = new Message(MessageType.loginFail);
            serverNetworkBoundary.sendMessage(message, client);
        }
    }

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

    private void logOut(User user) {
        user.setOnline(false);
        for(User u : clients.keySet()) {
            if (u.getUserName().equals(user.getUserName())) {
                clients.remove(u);
            }
        }

        Message message1 = new Message(MessageType.userLoggedOut, user);
        for (int i = 0; i < clients.size(); i++) {
            ServerNetworkBoundary.ClientHandler reciever = clients.get(i);
            serverNetworkBoundary.sendMessage(message1,reciever);
        }
    }

    private void addUser(User user, ServerNetworkBoundary.ClientHandler client) throws IOException {
        boolean userExists = checkIfUsersExists(user);

        if (userExists == false) {
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

    /*public void saveImage(User user) throws IOException {
            // Create the directory if it doesn't exist
            File directory = new File("images");
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Get the width and height of the ImageIcon
            int width = user.getImageIcon().getIconWidth();
            int height = user.getImageIcon().getIconHeight();

            // Create a BufferedImage with the same dimensions
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

            // Draw the ImageIcon onto the BufferedImage
            Graphics2D g2d = bufferedImage.createGraphics();
            user.getImageIcon().paintIcon(null, g2d, 0, 0);
            g2d.dispose();

            // Defining the file where the image will be saved
            File imageFile = new File(directory, user.getUserName()+".png");

            // Writing the image to the file
            ImageIO.write(bufferedImage, "png", imageFile);
            user.setPathNamePicture("images/"+user.getUserName()+".png");
    }*/

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

    private void addUsersToFile(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (User user : allUsers) {
                writer.write(user.getUserName()); // Write the username
                writer.newLine(); // Add a new line
                createUserFile(user);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createUserFile(User user) {
        String userFilePath = user.getUserName() + ".txt";
        File userFile = new File(userFilePath);
        try {
            if (userFile.createNewFile()) {
                System.out.println("Created file: " + userFilePath); // Debugging
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(userFile))) {
                    writer.write(user.getUserName()); // Write the username
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("File already exists: " + userFilePath); // Debugging
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void appendUsersToFile(String username, List<User> friends) {
        String userFilePath = username + ".txt";
        File userFile = new File(userFilePath);
        try (BufferedReader reader = new BufferedReader(new FileReader(userFile))) {
            List<String> existingUsers = new ArrayList<>();
            String line;
            // Read existing usernames from the file
            while ((line = reader.readLine()) != null) {
                existingUsers.add(line.trim());
            }

            // Append only the new friends to the file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(userFile, true))) {
                writer.newLine(); // Add a new line before appending users
                for (User user : friends) {
                    String friendName = user.getUserName();
                    if (!existingUsers.contains(friendName)) {
                        writer.write(friendName); // Write the username
                        writer.newLine(); // Add a new line
                        existingUsers.add(friendName); // Update existing users list
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    public void updateFriendsListFromFile(User loggedInUser) {
        System.out.println("help");
        String userFilePath = loggedInUser.getUserName() + ".txt";
        File userFile = new File(userFilePath);
        try (BufferedReader reader = new BufferedReader(new FileReader(userFile))) {
            String line;
            System.out.println("242");
            if ((line = reader.readLine()) != null) {
                System.out.println("244");
                String userName = line.trim(); // Read the first username from the file
                System.out.println("§" + userName +"§");
                User user = getUserByUsername(userName); // Find the corresponding user object
                if (user != null) {
                    List<User> friends = new ArrayList<>();
                    while ((line = reader.readLine()) != null) {
                        String friendUserName = line.trim();
                        System.out.println("§" +friendUserName +"§");
                        User friend = getUserByUsername(friendUserName); // Find the corresponding friend user object
                        if (friend != null) {
                            friends.add(friend); // Add the friend to the list
                        }
                    }
                    loggedInUser.setFriendList(friends); // Set the friend list for the logged-in user
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private User getUserByUsername(String userName) {
        for (User user : allUsers) {
            if (user.getUserName().equals(userName)) {
                return user;
            }
        }
        return null; // User not found
    }

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
}