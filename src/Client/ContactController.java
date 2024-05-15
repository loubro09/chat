package Client;

import Entity.Message;
import Entity.MessageType;
import Entity.User;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ContactController implements PropertyChangeListener {
    private List<User> allUsers = new ArrayList<>();
    private List<User> friends = new ArrayList<>();
    private List<User> chatWith = new ArrayList<>();
    private ClientViewController controller;
    private boolean typeOfList = true; // When true = all users & when false = Friends

    public ContactController(ClientViewController controller) {
        this.controller = controller;
    }

    public List<User> getAllUsers() {
        return allUsers;
    }

    public void setAllUsers(List<User> allUsers) {
        this.allUsers = allUsers;
    }

    public List<User> getFriends() {
        return friends;
    }

    public void setChatWith(List<User> chatWith) {
        this.chatWith = chatWith;
    }

    public List<User> getChatWith() {
        return chatWith;
    }

    public boolean isTypeOfList() {
        return typeOfList;
    }

    public void setTypeOfList(boolean typeOfList) {
        this.typeOfList = typeOfList;
    }

    public void setFriends(List<User> friends) {
        this.friends = friends;
    }

    public void addNewFriend(int index) {
        if (index != -1) { // if something is selected in the left menu list
            for (int i = 0; i < allUsers.size(); i++) {
                if (i == index) {
                    friends.add(allUsers.get(i));
                }
            }
        }
    }


    public String addFriendToChat(int index) {
        if (index != -1) {
            User userToAdd = null;

            if (typeOfList) {
                if (index < allUsers.size()) {
                    userToAdd = allUsers.get(index);
                    if (!chatWith.contains(userToAdd)) {
                        chatWith.add(userToAdd);
                        return userToAdd.getUserName();
                    }
                }
            } else {
                if (index < friends.size()) {
                    userToAdd = friends.get(index);
                    if (!chatWith.contains(userToAdd)) {
                        chatWith.add(userToAdd);
                        return userToAdd.getUserName();
                    }
                }
            }
        }
        return null;
    }


    public void emptyChatWith() {
        chatWith.clear();
    }


    private void updateOnline(Message message) {
        User loggedIn = message.getSender();
        for (User u : allUsers) {
            if (loggedIn.getUserName().equals(u.getUserName())) {
                u.setOnline(true);
            }
        }
        controller.allUsersToString(allUsers);
        System.out.println(loggedIn);
    }

    private void updateOffline(Message message) {
        User loggedOut = message.getSender();
        for (User u : allUsers) {
            if (loggedOut.getUserName().equals(u.getUserName())) {
                u.setOnline(false);
            }
        }
        controller.allUsersToString(allUsers);
        System.out.println(loggedOut);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("userLoggedIn".equals(evt.getPropertyName())) {
            Message message = (Message) evt.getNewValue();
            updateOnline(message);
        } else if ("userLoggedOut".equals(evt.getPropertyName())) {
            Message message = (Message) evt.getNewValue();
            updateOffline(message);
        }
    }

    //hämta alla kontakter från server
    //hämta alla vänner från users kontaktlista

    public void setFriendsListInServer() {
        Message message = new Message(MessageType.addFriends, null, controller.getLogController().getLoggedInUser(), friends, LocalDateTime.now(), null);
        controller.getLogController().getCnb().sendMessage(message);
    }
}
