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
    }

    private void logOut(User user) {
        user.setOnline(false);
        for(User u : clients.keySet()) {
            if (u.getUserName().equals(user.getUserName())) {
                clients.remove(u);
            }
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

    /*private void addUsersToFile(String filePath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath, false))) {
            oos.writeInt(allUsers.size());
            for (User user : allUsers) {
                oos.writeObject(user);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

   /* private void addUsersToFile(String filePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, false))) {
            bw.write(allUsers.size());
            for (User user : allUsers) {
                bw.write(user.getUserName());
            }
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    /*private List<User> readUsersFromFile(String filePath) {
        List<User> userList = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            int size = ois.readInt() -1;
            for (int i = 0; i < size ; i++) {
                User user = (User) ois.readObject();
                userList.add(user);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return userList;
    }*/

   /* private List<User> readUsersFromFile(String filePath) {
        List<User> userList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader (new FileReader(filePath)) {
            int size = br.read() -1;
            for (int i = 0; i < size; i++) {
                String userName = br.readLine();
            }
        } catch (IOException | ClassNotFoundException ef) {
            ef.printStackTrace();
        return userList;
    } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } */

        private void addTestValues() {
        //testvärden för användare
        ImageIcon userImage1 = new ImageIcon("images/loubro.png");
        ImageIcon resizedImage1 = new ImageIcon(userImage1.getImage().getScaledInstance(150,150,Image.SCALE_DEFAULT));
        User user1 = new User("loubro");
        user1.setImageIcon(resizedImage1);
        ImageIcon userImage2 = new ImageIcon("images/alacol.png");
        ImageIcon resizedImage2 = new ImageIcon(userImage2.getImage().getScaledInstance(150,150,Image.SCALE_DEFAULT));
        User user2 = new User("alacol");
        user2.setImageIcon(resizedImage2);
        ImageIcon userImage3 = new ImageIcon("images/idanor.png");
        ImageIcon resizedImage3 = new ImageIcon(userImage3.getImage().getScaledInstance(150,150,Image.SCALE_DEFAULT));
        User user3 = new User("idanor");
        user3.setImageIcon(resizedImage3);
        ImageIcon userImage4 = new ImageIcon("images/kenalt.png");
        ImageIcon resizedImage4 = new ImageIcon(userImage4.getImage().getScaledInstance(150,150,Image.SCALE_DEFAULT));
        User user4 = new User("kenalt");
        //user4.setImageIcon(resizedImage4);
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